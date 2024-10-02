package map;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class MapImage {
    static void writeMapImageToFile(File gpxFile, Path outputFolder, BufferedImage mImage) throws IOException {
        File file;
        String fileName = String.format("%s.png", gpxFile.getName().split("\\.")[0]);
        if (outputFolder != null) {
            file = outputFolder.resolve(fileName).toFile();
        } else {
            file = new File(fileName);
        }
        ImageIO.write(mImage, "PNG", file);
    }
}
