package files;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

public interface GpxFileRunner {
    Optional<ExtractedGpxResult> run(File file, Path outputFolder);
}
