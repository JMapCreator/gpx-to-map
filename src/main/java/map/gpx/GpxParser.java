package map.gpx;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.Track;
import io.jenetics.jpx.TrackSegment;
import io.jenetics.jpx.WayPoint;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class GpxParser {

    public static final String INDENT = "    ";

    public static List<Track> getTracks(File gpxFile) throws IOException {
        return GPX.read(Path.of(gpxFile.toURI())).tracks()
                .toList();
    }

    public static List<WayPoint> getWayPoints(List<Track> tracks) {
        return tracks.stream()
                .flatMap(Track::segments)
                .flatMap(TrackSegment::points)
                .toList();
    }

    public static File concatGpxFiles(Stream<File> gpxFiles, Path outputFolder) throws IOException {
        GPX.Builder gpxBuilder = GPX.builder();
        gpxFiles
                .map(File::toPath)
                .map(GpxParser::readGpx)
                .flatMap(GPX::tracks)
                .sorted(getTrackComparator())
                .forEach(gpxBuilder::addTrack);
        GPX gpx = gpxBuilder.build();
        final var indent = new GPX.Writer.Indent(INDENT);
        Path tempGpxFile = Files.createTempFile(null, null);
        GPX.Writer.of(indent).write(gpx, tempGpxFile);
        return tempGpxFile.toFile();
    }

    private static Comparator<Track> getTrackComparator() {
        return (t1, t2) -> {
            Optional<WayPoint> firstWp1 = t1.segments().flatMap(TrackSegment::points).min(getWayPointComparator());
            Optional<WayPoint> firstWp2 = t2.segments().flatMap(TrackSegment::points).min(getWayPointComparator());
            Instant wpT1 = firstWp1.map(wp -> wp.getTime().orElse(Instant.EPOCH)).orElse(Instant.EPOCH);
            Instant wpT2 = firstWp2.map(wp -> wp.getTime().orElse(Instant.EPOCH)).orElse(Instant.EPOCH);
            return wpT1.compareTo(wpT2);
        };
    }

    private static Comparator<WayPoint> getWayPointComparator() {
        return (p1, p2) -> {
            Instant t1 = p1.getTime().orElse(Instant.EPOCH);
            Instant t2 = p2.getTime().orElse(Instant.EPOCH);
            return t1.compareTo(t2);
        };
    }

    private static GPX readGpx(Path path) {
        try {
            return GPX.read(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
