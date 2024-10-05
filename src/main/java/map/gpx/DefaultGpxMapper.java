package map.gpx;

import files.ExtractedGpxResult;
import io.jenetics.jpx.Track;
import io.jenetics.jpx.WayPoint;
import map.ElevationGraphCreator;
import map.MapImage;
import map.StaticMapCreator;
import org.knowm.xchart.XYChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class DefaultGpxMapper implements IGpxMapper {
    public static final Logger LOGGER = LoggerFactory.getLogger(DefaultGpxMapper.class);

    private final int width;
    private final int height;
    private final int chartHeight;

    private DefaultGpxMapper(int width, int height, int chartHeight) {
        this.width = width;
        this.height = height;
        this.chartHeight = chartHeight;
    }

    public static class builder {
        int width = 1000;
        int height = 1400;
        int chartHeight = 150;

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
            return new DefaultGpxMapper(this.width, this.height, this.chartHeight);
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
        BufferedImage mImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = mImage.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
        StaticMapCreator.drawMap(wayPoints, graphics, width, height);
        drawElevationGraph(wayPoints, graphics);
        MapImage.writeMapImageToFile(gpxFile, outputFolder, mImage);
        return GpxMetadataExtractor.extract(gpxFile.getName(), tracks, wayPoints);
    }

    private void drawElevationGraph(List<WayPoint> wayPoints, Graphics2D graphics) {
        LOGGER.info("Drawing elevation graph...");
        XYChart xyChart = ElevationGraphCreator.getElevationGraph(wayPoints);
        AffineTransform startFromBottom = AffineTransform.getTranslateInstance(0, height - chartHeight);
        graphics.setTransform(startFromBottom);
        xyChart.paint(graphics, width, chartHeight);
        LOGGER.info("Drawing elevation graph finished");
    }

}
