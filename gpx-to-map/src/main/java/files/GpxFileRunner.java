package files;

import java.nio.file.Path;
import java.util.List;

public interface GpxFileRunner {

    List<ExtractedGpxResult> run(Path currentFolder, Path outputFolder);
}
