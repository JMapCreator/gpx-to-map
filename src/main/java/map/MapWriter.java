package map;

import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Utility class for writing the resulting map image on disk
 */
public class MapWriter {
    public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MapWriter.class);

    /**
     * Writes the resulting image from the mapping of the GPX file
     *
     * @param gpxFile      the input GPX file
     * @param outputFolder the folder where to write the image
     * @param mImage       the image created from the GPX file
     * @throws IOException if there is any issue while writing the {@link BufferedImage} on disk
     */
    public static void writeMapImageToFile(File gpxFile, Path outputFolder, BufferedImage mImage) throws IOException {
        File file;
        String fileName = String.format("%s.png", gpxFile.getName().split("\\.")[0]);
        if (outputFolder != null) {
            file = outputFolder.resolve(fileName).toFile();
        } else {
            file = new File(gpxFile.getParent(), fileName);
        }
        LOGGER.info("Writing finished image to : {}", file.getAbsolutePath());
        ImageIO.write(mImage, "PNG", file);
    }
}
