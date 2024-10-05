package map.gpx;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.Track;
import io.jenetics.jpx.TrackSegment;
import io.jenetics.jpx.WayPoint;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class GpxParser {
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
                .forEach(gpxBuilder::addTrack);
        GPX gpx = gpxBuilder.build();
        final var indent = new GPX.Writer.Indent("    ");
        Path concatenatedFilePath = outputFolder.resolve("concatenated.gpx");
        GPX.Writer.of(indent).write(gpx, concatenatedFilePath);
        return concatenatedFilePath.toFile();
    }

    private static GPX readGpx(Path path) {
        try {
            return GPX.read(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
