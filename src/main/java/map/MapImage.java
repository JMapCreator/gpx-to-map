package map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class MapImage {
    public static final Logger LOGGER = LogManager.getLogger();

    static void writeMapImageToFile(File gpxFile, Path outputFolder, BufferedImage mImage) throws IOException {
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
