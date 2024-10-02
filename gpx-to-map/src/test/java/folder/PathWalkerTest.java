package folder;

import files.DefaultGpxRunner;
import files.ExtractedGpxResult;
import files.FileRunner;
import map.DefaultGpxMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PathWalkerTest {

    @TempDir
    Path emptyFolder;

    @Test
    void it_should_return_no_result_for_empty_folder() throws IOException {
        // Given the default gpx path walker
        PathWalker<DefaultGpxRunner, FileRunner> defaultGpxPathWalker = getTestPathWalker();

        // If the folder is empty
        Files.walkFileTree(emptyFolder, defaultGpxPathWalker);

        // No GPX is extracted
        assertThat(defaultGpxPathWalker.getExtractedResults()).isEmpty();
    }

    @Test
    void it_should_parse_gpx_if_found() throws IOException {
        // Given a folder that contains a gpx file
        Path resourceDirectory = Paths.get("src", "test", "resources");
        PathWalker<DefaultGpxRunner, FileRunner> defaultGpxPathWalker = getTestPathWalker();

        // When the walker goes through it
        Files.walkFileTree(resourceDirectory, defaultGpxPathWalker);

        // Then the GPX file should be parsed
        assertThat(defaultGpxPathWalker.getExtractedResults()).hasSize(1);
    }

    private PathWalker<DefaultGpxRunner, FileRunner> getTestPathWalker() throws IOException {
        DefaultGpxMapper defaultGpxMapper = Mockito.mock(DefaultGpxMapper.class);
        when(defaultGpxMapper.map(any(), any())).thenReturn(new ExtractedGpxResult("", "", 1, 1, 1f));
        return new PathWalker<>(null, new DefaultGpxRunner(defaultGpxMapper), null);
    }
}