package folder;

import files.DefaultGpxRunner;
import files.ExtractedGpxResult;
import files.FileRunner;
import files.GpxFileRunner;
import map.DefaultGpxMapper;
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

    public GpxToMapWalker(Path outPutPath, U gpxFileRunner, V postVisitRunner) {
        this.outPutPath = outPutPath;
        this.gpxFileRunner = gpxFileRunner;
        this.postVisitRunner = postVisitRunner;
    }

    @Override
    public FileVisitResult visitFile(Path path, BasicFileAttributes attrs) {
        File file = path.toFile();
        LOGGER.info("Visiting file {}...", file.getName());
        gpxFileRunner.run(file, outPutPath)
                .ifPresent(r -> gpxResultMap.put(path.getParent().toString(), r));
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

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return super.visitFileFailed(file, exc);
    }

    public static GpxToMapWalker<DefaultGpxRunner, FileRunner> getDefaultGpxPathWalker() {
        return new GpxToMapWalker<>(null, new DefaultGpxRunner(new DefaultGpxMapper.builder().build()), null);
    }

    public Stream<ExtractedGpxResult> getExtractedResults() {
        return this.gpxResultMap.values().stream();
    }
}
