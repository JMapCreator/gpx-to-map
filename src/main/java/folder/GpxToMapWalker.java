package folder;

import files.ExtractedGpxResult;
import files.FileRunner;
import files.GpxFileRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
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

    public static final Logger LOGGER = LoggerFactory.getLogger(GpxToMapWalker.class);

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
        if (shouldUpdateParentFolder) {
            updateDirWithSubDirsData(dir);
        }
        if (postVisitRunner != null) {
            LOGGER.info("Using post runner to update files...");
            postVisitRunner.run(dir, outPutPath, gpxResultMap);
        }
        return super.postVisitDirectory(dir, exc);
    }

    private void updateDirWithSubDirsData(Path dir) throws IOException {
        try (Stream<Path> subFolders = Files.list(dir)) {
            List<File> matchingSubFolders = subFolders
                    .filter(sf -> gpxResultMap.containsKey(sf.toString()))
                    .map(p -> p.resolve(gpxResultMap.get(p.toString()).gpxName()).toFile())
                    .toList();
            if (!matchingSubFolders.isEmpty()){
                gpxFileRunner.run(matchingSubFolders, dir).ifPresent(gpxResult -> gpxResultMap.put(dir.toString(), gpxResult));
            }
        }
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
