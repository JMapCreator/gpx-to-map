package map;

import files.ExtractedGpxResult;
import io.jenetics.jpx.Track;
import io.jenetics.jpx.WayPoint;
import map.gpx.GpxMetadataExtractor;
import map.gpx.GpxParser;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

class GpxMetadataExtractorTest {

    @TempDir
    File tempDir;

    @Test
    void it_should_correctly_extract_metadata() throws IOException {
        // Given a valid GPX file
        Path resourceDirectory = Paths.get("src", "test", "resources");
        String gpxName = "test.gpx";

        // When the file gets parsed
        List<Track> tracks = GpxParser.getTracks(resourceDirectory.resolve(gpxName).toFile());
        List<WayPoint> wayPoints = GpxParser.getWayPoints(tracks);
        ExtractedGpxResult extracted = GpxMetadataExtractor.extract(gpxName, tracks, wayPoints);

        // Then the extracted metadata should be correct
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(extracted.distance()).isEqualTo(13173);
        softly.assertThat(extracted.elevation()).isEqualTo(1644);
        softly.assertThat(extracted.duration()).isEqualTo("5:11:27");
        softly.assertThat(extracted.speed()).isEqualTo(2.5377429f);
        softly.assertAll();
    }

    @Test
    void it_should_correctly_extract_multiple_metadata() throws IOException {
        // Given multiple valid GPX files
        Path resourceDirectory = Paths.get("src", "test", "resources");
        String gpxName = "test.gpx";
        String gpxName2 = "test2.gpx";

        // When the file gets parsed
        File concatenated = GpxParser.concatGpxFiles(Stream.of(resourceDirectory.resolve(gpxName).toFile(),
                resourceDirectory.resolve(gpxName2).toFile())
        );
        List<Track> tracks = GpxParser.getTracks(concatenated);
        List<WayPoint> wayPoints = GpxParser.getWayPoints(tracks);
        ExtractedGpxResult extracted = GpxMetadataExtractor.extract(gpxName, tracks, wayPoints);

        // Then the extracted metadata should be correct
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(extracted.distance()).isEqualTo(31624);
        softly.assertThat(extracted.elevation()).isEqualTo(3324);
        softly.assertThat(extracted.duration()).isEqualTo("13:58:00");
        softly.assertThat(extracted.speed()).isEqualTo(2.2642484f);
        softly.assertAll();
    }

}