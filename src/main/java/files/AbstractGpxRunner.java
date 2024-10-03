package files;

import map.IGpxMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public abstract class AbstractGpxRunner<U extends IGpxMapper> implements GpxFileRunner {

    public static final String GPX_EXTENSION = ".gpx";
    public final U gpxMapper;

    protected AbstractGpxRunner(U gpxMapper) {
        this.gpxMapper = gpxMapper;
    }

    @Override
    public Optional<ExtractedGpxResult> run(File file, Path outputFolder) {
        if (file.getName().endsWith(GPX_EXTENSION)) {
            return Optional.of(convertToMap(file, outputFolder));
        }
        return Optional.empty();
    }

    public ExtractedGpxResult convertToMap(File gpxFile, Path outputFolder) {
        try {
            return gpxMapper.map(gpxFile, outputFolder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
