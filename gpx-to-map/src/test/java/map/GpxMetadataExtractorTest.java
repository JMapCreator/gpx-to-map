package map;

import files.ExtractedGpxResult;
import io.jenetics.jpx.WayPoint;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

class GpxMetadataExtractorTest {

    @Test
    void it_should_correctly_extract_metadata() throws IOException {
        Path resourceDirectory = Paths.get("src", "test", "resources");
        List<WayPoint> wayPoints = GpxParser.getWayPoints(resourceDirectory.resolve("test.gpx").toFile());
        ExtractedGpxResult extracted = GpxMetadataExtractor.extract(wayPoints);
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(extracted.distance()).isEqualTo(13173);
        softly.assertThat(extracted.elevation()).isEqualTo(1572);
        softly.assertThat(extracted.duration()).isEqualTo("5:11:27");
        softly.assertThat(extracted.vitesse()).isEqualTo(2.5377429f);
        softly.assertAll();
    }

}