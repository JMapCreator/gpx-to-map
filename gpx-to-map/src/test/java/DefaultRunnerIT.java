import files.DefaultGpxRunner;
import files.ExtractedGpxResult;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class DefaultRunnerIT {

    @TempDir
    Path tempDir;

    @Test
    @DisplayName("Given a gpx, a map should be created and the infos about the gpx be returned")
    public void it_should_generate_map_from_gpx() {
        // Given a correct gpx file
        Path resourceDirectory = Paths.get("src","test","resources");

        // When the default runner uses this file
        DefaultGpxRunner defaultGpxRunner = new DefaultGpxRunner();
        List<ExtractedGpxResult> results = defaultGpxRunner.run(resourceDirectory, tempDir);

        // Then a map should be generated and the infos should be returned
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(results).hasSize(1);
        softly.assertAll();
    }
}
