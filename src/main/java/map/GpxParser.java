package map;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.Track;
import io.jenetics.jpx.TrackSegment;
import io.jenetics.jpx.WayPoint;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class GpxParser {
    static List<WayPoint> getWayPoints(File gpxFile) throws IOException {
        return GPX.read(Path.of(gpxFile.toURI())).tracks()
                .flatMap(Track::segments)
                .flatMap(TrackSegment::points)
                .toList();
    }
}
