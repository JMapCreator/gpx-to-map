package map.filewriter;

import map.gpx.GpxStyler;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * Interface representing writer for writing the map/graph on disk in a given format
 */
public interface FileWriter {
    /**
     * Writes the resulting image from the mapping of the GPX file
     *
     * @param gpxFile        the input GPX file
     * @param outputFolder   the folder where to write the image
     * @param map            the map to write
     * @param elevationGraph the elevation graph to write
     * @param styler         the {@link GpxStyler} to use
     * @throws IOException if there is any issue while writing the {@link BufferedImage} on disk
     */
    void writeMapImageToFile(File gpxFile, Path outputFolder, GpxStyler styler, BufferedImage map, BufferedImage elevationGraph) throws IOException;

    void writeMapImageToFile(File gpxFile, Path outputFolder, GpxStyler styler, BufferedImage map) throws IOException;
}