package files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class MarkdownRunner implements FileRunner {
    public static final Logger LOGGER = LoggerFactory.getLogger(MarkdownRunner.class);

    public static final String MD_EXTENSION = ".md";
    public static final String TOML_PARAM_FORMAT = "%s = \"%s\"";
    public static final String TOML_HEADER_LINE = "+++";
    public static final String TOML_PARAM_DELIMITER = "=";
    public static final Pattern TRAILING_QUOTES = Pattern.compile("^\"|\"$");
    public static final String POINT = "\\.";
    public static final String PNG_EXTENSION = ".png";
    public static final String GPS_KEY = "gps";
    public static final String DISTANCE_KEY = "distance";
    public static final String DURATION_KEY = "duration";
    public static final String SPEED_KEY = "speed";
    public static final String ELEVATION_KEY = "elevation";

    @Override
    public void run(Path dir, Path outputFolder, Map<String, ExtractedGpxResult> gpxResultSet) {
        try (Stream<Path> files = Files.list(dir)) {
            files
                    .filter(file -> file.getFileName().toString().endsWith(MD_EXTENSION))
                    .forEach(file -> updateMarkdownFile(gpxResultSet, file, dir));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void updateMarkdownFile(Map<String, ExtractedGpxResult> gpxResultSet, Path file, Path dir) {
        gpxResultSet.entrySet().stream()
                .filter(entry -> entry.getKey().equals(file.getParent().toString()))
                .map(Map.Entry::getValue)
                .findFirst()
                .ifPresent(result -> {
                    List<String> lines = updateFile(file.toFile(), result, dir);
                    try {
                        Files.write(file,
                                lines,
                                StandardOpenOption.WRITE);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private static List<String> updateFile(File file, ExtractedGpxResult result, Path dir) {
        LOGGER.info("Updating markdown file {}", dir.resolve(file.getName()));
        Map<String, String> headerParams = new HashMap<>();
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            AtomicBoolean isHeader = new AtomicBoolean(false);
            br.lines().forEach(line -> {
                if (line.equals(TOML_HEADER_LINE)) {
                    isHeader.set(!isHeader.get());
                } else {
                    processLine(line, isHeader, headerParams, lines);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        deleteOldGpxImageIfPresent(headerParams, result, dir);
        updateHeaderParams(result, headerParams);
        setNewHeader(lines, headerParams);
        return lines;
    }

    private static void deleteOldGpxImageIfPresent(Map<String, String> headerParams, ExtractedGpxResult gpxResult, Path dir) {
        if (headerParams.containsKey(GPS_KEY) && !getFileNameWithoutExt(headerParams.get(GPS_KEY)).equals(getFileNameWithoutExt(gpxResult.gpxName()))) {
            LOGGER.info("Different previous GPS image reference found in header : {} (actual {})", getFileNameWithoutExt(headerParams.get(GPS_KEY)),
                    getFileNameWithoutExt(gpxResult.gpxName()));
            File oldImage = dir.resolve(headerParams.get(GPS_KEY)).toFile();
            if (oldImage.exists()) {
                deleteOldImage(oldImage);
            } else {
                LOGGER.info("Previous image cannot be found in directory : {}, skipping", dir);
            }
        }
    }

    private static String getFileNameWithoutExt(String fileName) {
        return fileName.split("\\.")[0];
    }

    private static void deleteOldImage(File oldImage) {
        LOGGER.info("Deleting previous image : {}", oldImage.getPath());
        if (oldImage.delete()) {
            LOGGER.info("Previous image deleted");
        } else {
            LOGGER.info("Couldnt delete previous image");
        }
    }

    private static void setNewHeader(List<String> lines, Map<String, String> headerParams) {
        lines.addFirst(TOML_HEADER_LINE);
        headerParams.forEach((key, value) -> lines.addFirst(String.format(TOML_PARAM_FORMAT, key, value)));
        lines.addFirst(TOML_HEADER_LINE);
    }

    private static void updateHeaderParams(ExtractedGpxResult result, Map<String, String> headerParams) {
        headerParams.put(GPS_KEY, result.gpxName().split(POINT)[0] + PNG_EXTENSION);
        headerParams.put(DISTANCE_KEY, String.valueOf(result.distance()));
        headerParams.put(DURATION_KEY, result.duration());
        headerParams.put(SPEED_KEY, String.valueOf(result.speed()));
        headerParams.put(ELEVATION_KEY, String.valueOf(result.elevation()));
        LOGGER.info("Markdown file updated with new header : {}", headerParams);
    }

    private static void processLine(String line, AtomicBoolean isHeader, Map<String, String> headerParams, List<String> lines) {
        if (isHeader.get()) {
            processHeaderLine(line, headerParams);
        } else {
            lines.add(line);
        }
    }

    private static void processHeaderLine(String line, Map<String, String> headerParams) {
        String[] param = line.split(TOML_PARAM_DELIMITER);
        String sanitizedValue = TRAILING_QUOTES.matcher(param[1].trim()).replaceAll("");
        headerParams.put(param[0].trim(), sanitizedValue);
    }
}
