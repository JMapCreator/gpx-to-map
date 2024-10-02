package files;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Map;
import java.util.StringJoiner;

public class MarkdownRunner implements FileRunner {

    public static final String MD_EXTENSION = ".md";
    public static final String TOML_PARAM_FORMAT = "%s = %s";

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
                                    .findFirst().ifPresent(result -> {
                                        StringJoiner gpxInfos = getHeader(result);
                                        try {
                                            Files.writeString(file.toPath(),
                                                    gpxInfos.toString(),
                                                    StandardOpenOption.APPEND);
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    });
                        }
                    });
        }
    }

    private static StringJoiner getHeader(ExtractedGpxResult result) {
        StringJoiner gpxInfos = new StringJoiner("\n");
        gpxInfos.add(String.format(TOML_PARAM_FORMAT, "gpx", result.gpxName()));
        gpxInfos.add(String.format(TOML_PARAM_FORMAT, "distance", result.distance()));
        gpxInfos.add(String.format(TOML_PARAM_FORMAT, "duration", result.duration()));
        gpxInfos.add(String.format(TOML_PARAM_FORMAT, "speed", result.speed()));
        return gpxInfos;
    }
}
