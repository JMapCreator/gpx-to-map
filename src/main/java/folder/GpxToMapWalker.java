package folder;

import files.ExtractedGpxResult;
import files.FileRunner;
import files.GpxFileRunner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static java.nio.file.FileVisitResult.CONTINUE;

/**
 * This is the main class of the project. It will iterate through the given folder and generate maps from every gpx file it encounters.
 *
 * @param <U> a {@link GpxFileRunner} that will convert GPX to an image
 * @param <V> a {@link FileRunner} that can apply any type of transformation in each visited folder
 */
public class GpxToMapWalker<U extends GpxFileRunner, V extends FileRunner> extends SimpleFileVisitor<Path> {

    public static final Logger LOGGER = LogManager.getLogger();

    private final Map<String, ExtractedGpxResult> gpxResultMap = new HashMap<>();
    private final U gpxFileRunner;
    private final V postVisitRunner;
    private final Path outPutPath;
    private final boolean shouldUpdateParentFolder;

    private GpxToMapWalker(Path outPutPath, U gpxFileRunner, V postVisitRunner, boolean shouldUpdateParentFolder) {
        this.outPutPath = outPutPath;
        this.gpxFileRunner = gpxFileRunner;
        this.postVisitRunner = postVisitRunner;
        this.shouldUpdateParentFolder = shouldUpdateParentFolder;
    }

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
        File file = path.toFile();
        LOGGER.info("Visiting file {}...", file.getName());
        Optional<ExtractedGpxResult> extractedGpxResult = gpxFileRunner.run(file, outPutPath);
        extractedGpxResult.ifPresent(gpxResult -> gpxResultMap.put(file.getParent(), gpxResult));
        return CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        LOGGER.info("Done visiting directory {}", dir.getFileName());
        if (postVisitRunner != null) {
            LOGGER.info("Using post runner to update files...");
            postVisitRunner.run(dir, outPutPath, gpxResultMap);
        }
        return super.postVisitDirectory(dir, exc);
    }

    private boolean isParent(Path dir) {
        return false;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return super.visitFileFailed(file, exc);
    }

    public static class builder<U extends GpxFileRunner, V extends FileRunner> {
        private U gpxFileRunner;
        private V postVisitRunner;
        private Path outPutPath;
        private boolean shouldUpdateParentFolder;

        public builder<U, V> setGpxFileRunner(U gpxFileRunner) {
            this.gpxFileRunner = gpxFileRunner;
            return this;
        }

        public builder<U, V> setPostVisitRunner(V postVisitRunner) {
            this.postVisitRunner = postVisitRunner;
            return this;
        }

        public builder<U, V> setOutPutPath(Path outPutPath) {
            this.outPutPath = outPutPath;
            return this;
        }

        public builder<U, V> setShouldUpdateParentFolder(boolean shouldUpdateParentFolder) {
            this.shouldUpdateParentFolder = shouldUpdateParentFolder;
            return this;
        }

        public GpxToMapWalker<U, V> build() {
            return new GpxToMapWalker<>(this.outPutPath, this.gpxFileRunner, this.postVisitRunner, this.shouldUpdateParentFolder);
        }
    }

    public Stream<ExtractedGpxResult> getExtractedResults() {
        return this.gpxResultMap.values().stream();
    }
}
