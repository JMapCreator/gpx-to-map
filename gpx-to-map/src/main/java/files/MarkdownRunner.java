package files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class MarkdownRunner implements FileRunner {

    public static final String MD_EXTENSION = ".md";
    public static final String TOML_PARAM_FORMAT = "%s = %s";
    public static final String TOML_HEADER_LINE = "+++";
    public static final String TOML_PARAM_DELIMITER = "=";

    @Override
    public void run(Path dir, Path outputFolder, Map<String, ExtractedGpxResult> gpxResultSet) {
        File[] files = dir.toFile().listFiles();
        if (files != null) {
            Arrays.stream(files)
                    .forEach(file -> {
                        if (file.getName().endsWith(MD_EXTENSION)) {
                            gpxResultSet.entrySet().stream()
                                    .filter(entry -> entry.getKey().equals(file.toPath().getParent().toString()))
                                    .map(Map.Entry::getValue)
                                    .findFirst()
                                    .ifPresent(result -> {
                                        List<String> lines = updateFile(file, result);
                                        try {
                                            Files.write(file.toPath(),
                                                    lines,
                                                    StandardOpenOption.WRITE);
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    });
                        }
                    });
        }
    }

    private static List<String> updateFile(File file, ExtractedGpxResult result) {
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
        updateHeaderParams(result, headerParams);
        setNewHeader(lines, headerParams);
        return lines;
    }

    private static void setNewHeader(List<String> lines, Map<String, String> headerParams) {
        lines.addFirst(TOML_HEADER_LINE);
        headerParams.forEach((key, value) -> lines.addFirst(String.format(TOML_PARAM_FORMAT, key, value)));
        lines.addFirst(TOML_HEADER_LINE);
    }

    private static void updateHeaderParams(ExtractedGpxResult result, Map<String, String> headerParams) {
        headerParams.put("gps", result.gpxName());
        headerParams.put("distance", String.valueOf(result.distance()));
        headerParams.put("duration", result.duration());
        headerParams.put("speed", String.valueOf(result.speed()));
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
        headerParams.put(param[0].trim(), param[1].trim());
    }
}
