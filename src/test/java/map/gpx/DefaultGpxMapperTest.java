package map.gpx;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
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

        // When the map is created
        File gpxFile = resourceDirectory.resolve("test.gpx").toFile();
        defaultGpxMapper.map(gpxFile, tempDir);
        BufferedImage resultingImage = ImageIO.read(tempDir.resolve("test.png").toFile());

        // Its dimensions should be correct
        assertThat(resultingImage.getWidth()).isEqualTo(SIZE);
        assertThat(resultingImage.getHeight()).isEqualTo(SIZE + CHART_HEIGHT);
    }

    @Test
    void it_should_be_possible_to_configure_map_style() throws IOException {
        // Given a GPX mapper and a styler
        GpxStyler gpxStyler = new GpxStyler.builder()
                .withGraphFillColor(new Color(0, 49, 223))
                .withGraphLineColor(new Color(0, 255, 0))
                .withStrokeColor(new Color(216, 0, 0))
                .withGraphPosition(GraphToMapPosition.TOP)
                .withStrokeWidth(5)
                .build();

        DefaultGpxMapper gpxMapper = new DefaultGpxMapper.builder()
                .withWidth(SIZE)
                .withHeight(SIZE)
                .withChartHeight(CHART_HEIGHT)
                .withGpxStyler(gpxStyler)
                .build();

        // When the map is created
        File gpxFile = resourceDirectory.resolve("test.gpx").toFile();
        gpxMapper.map(gpxFile, tempDir);

        // Then it should be drawn according to the style
        BufferedImage expected = ImageIO.read(resourceDirectory.resolve("ugly_result.png").toFile());
        BufferedImage resultingImage = ImageIO.read(tempDir.resolve("test.png").toFile());
        byte[] expectedData = ((DataBufferByte) expected.getData().getDataBuffer()).getData();
        byte[] actualData = ((DataBufferByte) resultingImage.getData().getDataBuffer()).getData();
        assertThat(actualData).isEqualTo(expectedData);
    }

}