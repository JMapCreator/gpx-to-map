package files;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class MarkdownRunnerTest {

        @TempDir
        File tempDir;

        @Test
        void it_should_update_markdown_file_if_gpx_present() throws IOException {
                // Given a folder where there is a gpx and a markdown file
                Path testFile = Files.createFile(tempDir.toPath().resolve("test.md"));
                Map<String, ExtractedGpxResult> gpxResultMap = new HashMap<>();
                String gpxName = "testGpxName.gpx";
                String testDuration = "testDuration";
                float speed = 5f;
                int elevation = 10;
                int distance = 10;
                gpxResultMap.put(tempDir.getPath(),
                                new ExtractedGpxResult(gpxName, testDuration, distance, elevation, speed));

                // When the MarkdownRunner runs through this folder
                MarkdownRunner markdownRunner = new MarkdownRunner();
                markdownRunner.run(tempDir.toPath(), null, gpxResultMap);

                String expectedHeader = """
                                +++
                                speed = "5.0"
                                gps = "testGpxName.png"
                                distance = "10"
                                elevation = "10"
                                duration = "testDuration"
                                +++
                                """;
                // Then it should update the markdown file with the gpx infos
                assertThat(testFile.toFile()).hasContent(expectedHeader);
        }

        @Test
        void it_should_override_markdown_file_if_gpx_present() throws IOException {
                // Given a folder where there is a gpx and a not empty markdown file
                Path testFile = Files.createFile(tempDir.toPath().resolve("test.md"));
                Files.writeString(testFile,
                                """
                                +++
                                title = "my nice adventure !"
                                draft = true
                                gps = toto.png
                                +++

                                # Title
                                Papa tango, this is a test !
                                """,
                                StandardCharsets.UTF_8);
                Map<String, ExtractedGpxResult> gpxResultMap = new HashMap<>();
                String gpxName = "testGpxName.gpx";
                String testDuration = "testDuration";
                float speed = 5f;
                int elevation = 10;
                int distance = 10;
                gpxResultMap.put(tempDir.getPath(),
                                new ExtractedGpxResult(gpxName, testDuration, distance, elevation, speed));

                // When the MarkdownRunner runs through this folder
                MarkdownRunner markdownRunner = new MarkdownRunner();
                markdownRunner.run(tempDir.toPath(), null, gpxResultMap);

                String expectedContent = """
                                +++
                                speed = "5.0"
                                title = ""my nice adventure !""
                                gps = "testGpxName.png"
                                draft = "true"
                                distance = "10"
                                elevation = "10"
                                duration = "testDuration"
                                +++

                                # Title
                                Papa tango, this is a test !
                                """;

                // Then it should update the markdown file with the gpx infos
                assertThat(testFile.toFile()).hasContent(expectedContent);
        }

        @Test
        void it_should_delete_previous_image_if_present_in_header() throws IOException {
                // Given a folder where there is a gpx and a markdown file
                Path testFile = Files.createFile(tempDir.toPath().resolve("test.md"));
                Files.writeString(testFile,
                                """
                                +++
                                title = "my nice adventure !"
                                draft = true
                                gps = oldImage.png
                                +++

                                # Title
                                Papa tango, this is a test !
                                """,
                                StandardCharsets.UTF_8);
                Path testImage = Files.createFile(tempDir.toPath().resolve("oldImage.png"));
                Map<String, ExtractedGpxResult> gpxResultMap = new HashMap<>();
                String gpxName = "testGpxName.gpx";
                String testDuration = "testDuration";
                float speed = 5f;
                int elevation = 10;
                int distance = 10;
                gpxResultMap.put(tempDir.getPath(),
                                new ExtractedGpxResult(gpxName, testDuration, distance, elevation, speed));

                // When the MarkdownRunner runs through this folder
                MarkdownRunner markdownRunner = new MarkdownRunner();
                markdownRunner.run(tempDir.toPath(), null, gpxResultMap);

                String expectedHeader = """
                                +++
                                speed = "5.0"
                                title = ""my nice adventure !""
                                gps = "testGpxName.png"
                                draft = "true"
                                distance = "10"
                                elevation = "10"
                                duration = "testDuration"
                                +++

                                # Title
                                Papa tango, this is a test !
                                """;

                // Then it should update the markdown file with the gpx infos AND delete the old
                // png
                assertThat(testFile.toFile()).hasContent(expectedHeader);
                assertThat(testImage.toFile()).doesNotExist();
        }

}