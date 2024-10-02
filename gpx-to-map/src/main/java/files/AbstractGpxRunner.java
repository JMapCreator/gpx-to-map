package files;

import map.GpxMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public abstract class AbstractGpxRunner implements GpxFileRunner {

    public static final String GPX_EXTENSION = ".gpx";

    @Override
    public List<ExtractedGpxResult> run(Path currentFolder, Path outputFolder) {
        if (currentFolder.toFile().exists()) {
            return Stream.of(currentFolder.toFile().listFiles())
                    .filter(file -> file.getName().endsWith(GPX_EXTENSION))
                    .map(f -> convertToMap(f, outputFolder))
                    .toList();
        }
        return Collections.emptyList();
    }

    public ExtractedGpxResult convertToMap(File gpxFile, Path outputFolder) {
        try {
            return GpxMapper.map(gpxFile, outputFolder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
