package folder;

import files.DefaultGpxRunner;
import files.MarkdownRunner;
import map.gpx.DefaultGpxMapper;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class GpxToMapWalkerIT {

    @TempDir
    File tempDir;

    @Test
    void it_should_create_map_and_update_file() throws IOException {
        // Given a folder with a gpx and md file
        String sampleHeader = createSampleFileHeader();
        String sampleContent = createSampleFileContent();
        Files.write(tempDir.toPath().resolve("test.md"), List.of(sampleHeader, sampleContent));
        String gpxName = "test.gpx";
        Path resourceGpx = Paths.get("src", "test", "resources", gpxName);
        Files.copy(resourceGpx, tempDir.toPath().resolve(gpxName));

        // When the markdown walker goes through it
        DefaultGpxMapper defaultGpxMapper = new DefaultGpxMapper.builder().build();
        var gpxToMapWalker = new GpxToMapWalker.builder<>()
                .setGpxFileRunner(new DefaultGpxRunner(defaultGpxMapper))
                .setPostVisitRunner(new MarkdownRunner())
                .build();

        // Then a map should be created and its info should be inserted in the markdown file
        Files.walkFileTree(tempDir.toPath(), gpxToMapWalker);
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(tempDir.toPath().resolve("test.png").toFile()).exists();
        String expectedHeader = getExpectedHeader();
        softAssertions.assertThat(tempDir.toPath().resolve("test.md").toFile()).hasContent(expectedHeader + sampleContent + "\n");
        softAssertions.assertAll();
    }

    @Test
    void it_should_create_parent_map_and_update_files() throws IOException {
        // Given a parent folder, containing a markdown file, with children folders each containing a gpx and md file
        Path firstChildFolder = tempDir.toPath().resolve("first_child_folder");
        Path secondChildFolder = tempDir.toPath().resolve("second_child_folder");
        firstChildFolder.toFile().mkdirs();
        secondChildFolder.toFile().mkdirs();
        String sampleHeader = createSampleFileHeader();
        Files.write(tempDir.toPath().resolve("test.md"), List.of(sampleHeader));
        Files.write(firstChildFolder.resolve("test.md"), List.of(sampleHeader));
        Files.write(secondChildFolder.resolve("test.md"), List.of(sampleHeader));
        String firstGpxName = "test.gpx";
        String secondGpxName = "test2.gpx";
        Path resource = Paths.get("src", "test", "resources");
        Files.copy(resource.resolve(firstGpxName), firstChildFolder.resolve(firstGpxName));
        Files.copy(resource.resolve(secondGpxName), secondChildFolder.resolve(secondGpxName));

        // When the markdown walker goes through it, with "shouldUpdateParentFolders" option turned on
        DefaultGpxMapper defaultGpxMapper = new DefaultGpxMapper.builder().build();
        var gpxToMapWalker = new GpxToMapWalker.builder<>()
                .setGpxFileRunner(new DefaultGpxRunner(defaultGpxMapper))
                .setPostVisitRunner(new MarkdownRunner())
                .setShouldUpdateParentFolder(true)
                .build();

        // Then a map should be created and its info should be inserted in the markdown file, for each GPX file
        // In the parent folder, a map containing the sum of all GPX files should be created and its markdown file update accordingly
        Files.walkFileTree(tempDir.toPath(), gpxToMapWalker);
        SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(tempDir.toPath().resolve("concatenated.png").toFile()).exists();
        softAssertions.assertThat(tempDir.toPath().resolve("concatenated.gpx").toFile()).exists();
        String expectedHeader = getConcatenatedExpectedHeader();
        softAssertions.assertThat(tempDir.toPath().resolve("test.md").toFile()).hasContent(expectedHeader);
        softAssertions.assertAll();
    }

    private String getConcatenatedExpectedHeader() {
        return """
                +++
                speed = 2.2642484
                title = "Calenzana - Ortu"
                gps = concatenated.gpx
                draft = true
                distance = 31624
                duration = 13:58:00
                date = "2024-09-02 14:57:16.816572"
                +++
                
                """;
    }

    private static String getExpectedHeader() {
        return """
                +++
                speed = 2.5377429
                title = "Calenzana - Ortu"
                gps = test.gpx
                draft = true
                distance = 13173
                duration = 5:11:27
                date = "2024-09-02 14:57:16.816572"
                +++
                
                """;
    }

    private String createSampleFileHeader() {
        return """
                +++
                title = "Calenzana - Ortu"
                date = "2024-09-02 14:57:16.816572"
                draft = true
                +++
                """;
    }

    private String createSampleFileContent() {
        return """               
                ### Don't look up
                L'aube venue, nous constatons l'ampleur de la tâche qui nous attend. La montagne est un mur sablonneux et aride que nous
                devons franchir.
                
                {{< gallery class="content-gallery" btn="2">}}
                {{< img src="DMuTvoeM.jpg" >}}
                {{< img src="iF4LS8t7.jpg" >}}
                {{< /gallery >}}
                
                ### Ortu di u Piobbu
                
                Le terrain de bivouac est immense et des dizaines de tentes s'éparpillent entre des murets de pierre.
                Nous nous installons rapidement, avant de prendre une bonne bière fraîche.
                
                {{< gallery class="content-gallery" btn="4">}}
                {{< img src="JdipwZb9.jpg" >}}
                {{< img src="kKZtrXKF.jpg" >}}
                {{< img src="qY5f5ZA9.jpg" >}}
                {{< img src="VzTNLjuP.jpg" >}}
                {{< /gallery >}}
                """;
    }

}
