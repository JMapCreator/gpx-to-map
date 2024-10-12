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

    private static final float M_TO_KM = 1000f;
    private static final float SEC_TO_HOUR_F = 3600;
    private static final int SEC_TO_HOUR_I = 3600;
    private static final int MIN_PER_HOUR = 60;
    private static final String DATE_FORMAT = "%d:%02d:%02d";

    /**
     * Extracts metadata from GPX file and stores them in a wrapper record.
     * These results can then be used for any kind of operations especially during the {@link folder.GpxToMapWalker#postVisitDirectory} step.
     *
     * @param gpxName   name of the GPX field being read
     * @param tracks    list of tracks found in the GPX file
     * @param wayPoints list of waypoints found in the tracks
     * @return an {@link ExtractedGpxResult} wrapper containing the extracted metadata (gpxName, duration, distance, elevation, average speed)
     */
    public static ExtractedGpxResult extract(String gpxName, List<Track> tracks, List<WayPoint> wayPoints) {
        long elapsedSeconds = getDurationInSeconds(tracks);
        String formattedDuration = String.format(DATE_FORMAT, elapsedSeconds / SEC_TO_HOUR_I, (elapsedSeconds % SEC_TO_HOUR_I) / MIN_PER_HOUR,
                (elapsedSeconds % MIN_PER_HOUR));
        int distance = wayPoints.stream().collect(Geoid.WGS84.toPathLength()).intValue();
        Integer elevation = getElevation(wayPoints);
        float speed = (distance / M_TO_KM) / (elapsedSeconds / SEC_TO_HOUR_F);
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
