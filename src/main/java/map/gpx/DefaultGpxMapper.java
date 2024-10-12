package map.gpx;

import files.ExtractedGpxResult;
import io.jenetics.jpx.Track;
import io.jenetics.jpx.WayPoint;
import map.ElevationGraphCreator;
import map.MapWriter;
import map.StaticMapCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * This class can be extended to implement your own GpxMapper. However, it can be used <i>as is</i>,
 * to provide default functionalities.
 */
public class DefaultGpxMapper implements IGpxMapper {
    public static final Logger LOGGER = LoggerFactory.getLogger(DefaultGpxMapper.class);

    private final int width;
    private final int height;
    private final int chartHeight;
    private final GpxStyler styler;

    public DefaultGpxMapper(int width, int height, int chartHeight, GpxStyler styler) {
        this.width = width;
        this.height = height;
        this.chartHeight = chartHeight;
        this.styler = styler;
    }

    public static class builder {
        int width = 1000;
        int height = 1400;
        int chartHeight = 150;
        GpxStyler gpxStyler;

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

        public DefaultGpxMapper build() {
            if (this.gpxStyler == null) {
                this.gpxStyler = GpxStyler.getDefaultStyler();
            }
            return new DefaultGpxMapper(this.width, this.height, this.chartHeight, this.gpxStyler);
        }
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
            drawMapWithGraph(gpxFile, outputFolder, wayPoints, map);
        } else {
            MapWriter.writeMapImageToFile(gpxFile, outputFolder, map);
        }
        return GpxMetadataExtractor.extract(gpxFile.getName(), tracks, wayPoints);
    }

    private void drawMapWithGraph(File gpxFile, Path outputFolder, List<WayPoint> wayPoints, BufferedImage map) throws IOException {
        BufferedImage elevationGraph = ElevationGraphCreator.createElevationGraph(wayPoints, width, chartHeight, styler);
        BufferedImage combinedImages = new BufferedImage(width, height + chartHeight, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = combinedImages.getGraphics();
        if (GraphToMapPosition.BOTTOM == styler.graphPosition()) {
            graphics.drawImage(map, 0, 0, null);
            graphics.drawImage(elevationGraph, 0, height, null);
        } else {
            graphics.drawImage(map, 0, chartHeight, null);
            graphics.drawImage(elevationGraph, 0, 0, null);
        }
        graphics.dispose();
        MapWriter.writeMapImageToFile(gpxFile, outputFolder, combinedImages);
    }
}
