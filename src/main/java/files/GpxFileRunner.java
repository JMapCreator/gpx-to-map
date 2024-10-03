package files;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;

public interface GpxFileRunner {
    /**
     * This method will be called on every GPX file encountered. If everything works properly, it will be turned into a image map.
     * Metadata about the GPX file are also extracted and stored in a map.
     *
     * @param file         input GPX file
     * @param outputFolder a path that can be used as an output folder
     * @return a map containing the GPX metadata, an empty {@link Optional} if something went wrong
     */
    Optional<ExtractedGpxResult> run(File file, Path outputFolder);
}
