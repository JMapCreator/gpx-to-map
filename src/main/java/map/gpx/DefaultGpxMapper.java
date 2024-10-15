package map.gpx;

import files.ExtractedGpxResult;
import io.jenetics.jpx.Track;
import io.jenetics.jpx.WayPoint;
import map.ElevationGraphCreator;
import map.StaticMapCreator;
import map.filewriter.FileWriter;
import map.filewriter.PngWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * This class can be extended to implement your own GpxMapper. However, it can be used <i>as is</i>,
 * to provide default functionalities.
 * <p>
 * The {@link builder} should be used to create and configure an instance.
 */
public class DefaultGpxMapper implements IGpxMapper {
    public static final Logger LOGGER = LoggerFactory.getLogger(DefaultGpxMapper.class);

    private final int width;
    private final int height;
    private final int chartHeight;
    private final GpxStyler styler;
    private final FileWriter fileWriter;

    private DefaultGpxMapper(int width, int height, int chartHeight, GpxStyler styler, FileWriter fileWriter) {
        this.width = width;
        this.height = height;
        this.chartHeight = chartHeight;
        this.styler = styler;
        this.fileWriter = fileWriter;
    }

    public ExtractedGpxResult map(File gpxFile) throws IOException {
        return map(gpxFile, null);
    }

    public ExtractedGpxResult map(File gpxFile, Path outputFolder) throws IOException {
        LOGGER.info("Parsing GPX file {}...", gpxFile.getName());
        List<Track> tracks = GpxParser.getTracks(gpxFile);
        List<WayPoint> wayPoints = GpxParser.getWayPoints(tracks);
        LOGGER.info("Parsed {} waypoints", wayPoints.size());

        BufferedImage map = StaticMapCreator.createMap(wayPoints, width, height, styler);
        if (styler.displayElevationGraph()) {
            BufferedImage elevationGraph = ElevationGraphCreator.createElevationGraph(wayPoints, width, chartHeight, styler);
            this.fileWriter.writeMapImageToFile(gpxFile, outputFolder, styler, map, elevationGraph);
        } else {
            this.fileWriter.writeMapImageToFile(gpxFile, outputFolder, styler, map);
        }
        return GpxMetadataExtractor.extract(gpxFile.getName(), tracks, wayPoints);
    }

    /**
     * Builder for the {@link DefaultGpxMapper}
     */
    public static class builder {
        /**
         * Default map width. The elevation graph will always use the exact same width
         */
        private int width = 1000;
        /**
         * Default map height
         */
        private int height = 1400;
        /**
         * Default chart height. Resulting image height will be {@link #height} + chartHeight
         */
        private int chartHeight = 150;
        /**
         * The {@link GpxStyler} to use. If none provided, the {@link GpxStyler#getDefaultStyler()} method will be used to create a default one
         */
        private GpxStyler gpxStyler;

        /**
         * The {@link FileWriter} to use. Will specify the output format. Defaults to PNG images
         */
        private FileWriter fileWriter = new PngWriter();

        public builder withWidth(int width) {
            this.width = width;
            return this;
        }

        public builder withHeight(int height) {
            this.height = height;
            return this;
        }

        public builder withChartHeight(int chartHeight) {
            this.chartHeight = chartHeight;
            return this;
        }

        public builder withGpxStyler(GpxStyler gpxStyler) {
            this.gpxStyler = gpxStyler;
            return this;
        }

        public builder setFileWriter(FileWriter fileWriter) {
            this.fileWriter = fileWriter;
            return this;
        }

        public DefaultGpxMapper build() {
            if (this.gpxStyler == null) {
                this.gpxStyler = GpxStyler.getDefaultStyler();
            }
            return new DefaultGpxMapper(this.width, this.height, this.chartHeight, this.gpxStyler, this.fileWriter);
        }
    }
}
