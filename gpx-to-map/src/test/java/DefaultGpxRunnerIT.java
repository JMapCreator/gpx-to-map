import files.DefaultGpxRunner;
import files.ExtractedGpxResult;
import map.DefaultGpxMapper;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class DefaultGpxRunnerIT {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("Given a gpx, a map should be created and the infos about the gpx be returned")
    public void it_should_generate_map_from_gpx() {
        // Given a correct gpx file
        Path resourceDirectory = Paths.get("src","test","resources");

        // When the default runner uses this file
        DefaultGpxRunner defaultGpxRunner = new DefaultGpxRunner(new DefaultGpxMapper());
        Optional<ExtractedGpxResult> result = defaultGpxRunner.run(resourceDirectory.resolve("test.gpx").toFile(), tempDir);

        // Then a map should be generated and the infos should be returned
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(result).isPresent();
        softly.assertAll();
    }
}
