package files;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MarkdownRunnerTest {

    @TempDir
    File tempDir;

    @Test
    void it_should_update_markdown_file_if_gox_present() throws IOException {
        // Given a folder where there is a gpx and a markdown file
        Path testFile = Files.createFile(tempDir.toPath().resolve("test.md"));
        Map<String, ExtractedGpxResult> gpxResultMap = new HashMap<>();
        String gpxName = "testGpxName.gpx";
        String testDuration = "testDuration";
        float speed = 5f;
        int elevation = 10;
        int distance = 10;
        gpxResultMap.put(tempDir.getPath(), new ExtractedGpxResult(gpxName, testDuration, distance, elevation, speed));

        // When the MarkdownRunner runs through this folder
        MarkdownRunner markdownRunner = new MarkdownRunner();
        markdownRunner.run(tempDir.toPath(), null, gpxResultMap);

        // Then it should update the markdown file with the gpx infos
        String expectedHeader = String.format("""
                gpx = %s
                distance = %s
                duration = %s
                speed = %s
                """, gpxName, distance, testDuration, speed);
        assertThat(testFile.toFile()).hasContent(expectedHeader);
    }

}