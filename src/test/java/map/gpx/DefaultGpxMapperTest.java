package map.gpx;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class DefaultGpxMapperTest {

    public static final int SIZE = 500;
    public static final Path resourceDirectory = Paths.get("src", "test", "resources");
    public static final int CHART_HEIGHT = 100;

    @TempDir
    public Path tempDir;

    @Test
    void it_should_create_map_to_correct_dimensions() throws IOException {
        // Given a GPX mapper
        DefaultGpxMapper defaultGpxMapper = new DefaultGpxMapper.builder()
                .withWidth(SIZE)
                .withHeight(SIZE)
                .withChartHeight(CHART_HEIGHT)
                .build();
        File gpxFile = resourceDirectory.resolve("test.gpx").toFile();

        // When the map is created
        defaultGpxMapper.map(gpxFile, tempDir);
        BufferedImage resultingImage = ImageIO.read(tempDir.resolve("test.png").toFile());

        // Its dimensions should be correct
        assertThat(resultingImage.getWidth()).isEqualTo(SIZE);
        assertThat(resultingImage.getHeight()).isEqualTo(SIZE + CHART_HEIGHT);
    }

}