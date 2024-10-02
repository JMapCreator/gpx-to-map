package files;

import java.nio.file.Path;
import java.util.Map;

public interface FileRunner {

    void run(Path file, Path outputFolder, Map<String, ExtractedGpxResult> gpxResultSet);
}
