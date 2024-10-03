package files;

import java.nio.file.Path;
import java.util.Map;

public interface FileRunner {

    /**
     * This method will be called on every file in each folder, **after** the {@link GpxFileRunner} has been called.
     *
     * @param file         the actual folder
     * @param outputFolder a path that can be used as an output folder
     * @param gpxResultSet if gpx file were extracted, a map containing their infos
     */
    void run(Path file, Path outputFolder, Map<String, ExtractedGpxResult> gpxResultSet);
}
