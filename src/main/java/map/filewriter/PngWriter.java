package map.filewriter;

import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Implementation of a {@link FileWriter} for writing PNG images
 */
public class PngWriter extends AbstractWriter {
    public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(PngWriter.class);

    protected void writeImage(File gpxFile, Path outputFolder, String suffix, BufferedImage image) throws IOException {
        File file;
        String gpxName = gpxFile.getName().split("\\.")[0];
        String fileName = String.format("%s%s.png", gpxName, suffix == null ? "" : "-" + suffix);
        if (outputFolder != null) {
            file = outputFolder.resolve(fileName).toFile();
        } else {
            file = new File(gpxFile.getParent(), fileName);
        }
        LOGGER.info("Writing finished image to : {}", file.getAbsolutePath());
        ImageIO.write(image, "PNG", file);
    }
}
