package map;

import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class MapImage {
    public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(MapImage.class);

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
