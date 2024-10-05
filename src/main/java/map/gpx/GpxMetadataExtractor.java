package map.gpx;

import files.ExtractedGpxResult;
import io.jenetics.jpx.Length;
import io.jenetics.jpx.Track;
import io.jenetics.jpx.TrackSegment;
import io.jenetics.jpx.WayPoint;
import io.jenetics.jpx.geom.Geoid;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

public class GpxMetadataExtractor {
    public static ExtractedGpxResult extract(String gpxName, List<Track> tracks, List<WayPoint> wayPoints) {
        long elapsedSeconds = getDurationInSeconds(tracks);
        String formattedDuration = String.format("%d:%02d:%02d", elapsedSeconds / 3600, (elapsedSeconds % 3600) / 60, (elapsedSeconds % 60));
        int distance = wayPoints.stream().collect(Geoid.WGS84.toPathLength()).intValue();
        Integer elevation = getElevation(wayPoints);
        float speed = (distance / 1000f) / (elapsedSeconds / 3600f);
        return new ExtractedGpxResult(gpxName, formattedDuration, distance, elevation, speed);
    }

    private static Integer getElevation(List<WayPoint> wayPoints) {
        return wayPoints.stream()
                .map(wp -> wp.getElevation().orElse(Length.of(0, Length.Unit.METER)).intValue())
                .collect(new PositiveElevationCollector());
    }

    private static long getDurationInSeconds(List<Track> tracks) {
        return tracks.stream()
                .flatMap(Track::segments)
                .map(TrackSegment::points)
                .map(Stream::toList)
                .mapToLong(wayPoints -> {
                    Instant startTime = wayPoints.getFirst().getTime().orElse(Instant.EPOCH);
                    Instant endTime = wayPoints.getLast().getTime().orElse(Instant.EPOCH);
                    return Duration.between(startTime, endTime).getSeconds();
                })
                .sum();
    }
}
