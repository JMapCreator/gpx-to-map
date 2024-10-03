package map;

import files.ExtractedGpxResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public interface IGpxMapper {
    /**
     * This method transform a GPX file into an image map.
     *
     * @param gpxFile the GPX file
     * @return the GPF file metadata
     * @throws IOException if the GPX file cannot be read or the image cannot be writen to disk
     */
    ExtractedGpxResult map(File gpxFile) throws IOException;

    /**
     * Same as {@link map} but adds the possibility to specify an output folder.
     *
     * @param gpxFile    the GPX file
     * @param outputPath an optional output path
     * @return the GPF file metadata
     * @throws IOException if the GPX file cannot be read or the image cannot be writen to disk
     */
    ExtractedGpxResult map(File gpxFile, Path outputPath) throws IOException;
}
