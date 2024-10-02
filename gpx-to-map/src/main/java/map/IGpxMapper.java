package map;

import files.ExtractedGpxResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public interface IGpxMapper {
    ExtractedGpxResult map(File gpxFile) throws IOException;

    ExtractedGpxResult map(File gpxFile, Path outputPath) throws IOException;
}
