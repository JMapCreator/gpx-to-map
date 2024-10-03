package map;

import files.ExtractedGpxResult;
import io.jenetics.jpx.Length;
import io.jenetics.jpx.WayPoint;
import io.jenetics.jpx.geom.Geoid;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class GpxMetadataExtractor {
    public static ExtractedGpxResult extract(String gpxName, List<WayPoint> wayPoints) {
        Instant startTime = wayPoints.getFirst().getTime().orElse(Instant.EPOCH);
        Instant endTime = wayPoints.getLast().getTime().orElse(Instant.EPOCH);
        long elapsedSeconds = Duration.between(startTime, endTime).getSeconds();
        String formattedDuration = String.format("%d:%02d:%02d", elapsedSeconds / 3600, (elapsedSeconds % 3600) / 60, (elapsedSeconds % 60));
        int distance = wayPoints.stream().collect(Geoid.WGS84.toPathLength()).intValue();
        Integer elevation = wayPoints.stream()
                .map(wp -> wp.getElevation().orElse(Length.of(0, Length.Unit.METER)).intValue())
                .reduce((integer, integer2) -> integer2 > integer ? integer + (integer2 - integer) : integer)
                .orElse(0);
        float vitesse = (distance / 1000f) / (elapsedSeconds / 3600f);
        return new ExtractedGpxResult(gpxName, formattedDuration, distance, elevation, vitesse);
    }
}
