package map.filewriter;

import map.gpx.GpxStyler;
import map.gpx.GraphToMapPosition;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public abstract class AbstractWriter implements FileWriter {

    @Override
    public void writeMapImageToFile(File gpxFile, Path outputFolder, GpxStyler styler, BufferedImage map, BufferedImage elevationGraph) throws IOException {
        if (styler.separateFiles()) {
            writeImage(gpxFile, outputFolder, "map", map);
            writeImage(gpxFile, outputFolder, "elevation", elevationGraph);
        } else {
            BufferedImage resultingImage = combineGraphAndMap(styler, map, elevationGraph);
            writeImage(gpxFile, outputFolder, null, resultingImage);
        }
    }

    @Override
    public void writeMapImageToFile(File gpxFile, Path outputFolder, GpxStyler styler, BufferedImage map) throws IOException {
        writeImage(gpxFile, outputFolder, null, map);
    }

    protected abstract void writeImage(File gpxFile, Path outputFolder, String suffix, BufferedImage map) throws IOException;

    protected BufferedImage combineGraphAndMap(GpxStyler styler, BufferedImage map, BufferedImage elevationGraph) {
        BufferedImage combinedImages = new BufferedImage(map.getWidth(),
                map.getHeight() + elevationGraph.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics graphics = combinedImages.getGraphics();
        if (GraphToMapPosition.BOTTOM == styler.graphPosition()) {
            graphics.drawImage(map, 0, 0, null);
            graphics.drawImage(elevationGraph, 0, map.getHeight(), null);
        } else {
            graphics.drawImage(map, 0, elevationGraph.getHeight(), null);
            graphics.drawImage(elevationGraph, 0, 0, null);
        }
        graphics.dispose();
        return combinedImages;
    }
}
