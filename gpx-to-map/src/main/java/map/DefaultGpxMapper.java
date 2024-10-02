package map;

import files.ExtractedGpxResult;
import io.jenetics.jpx.WayPoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.knowm.xchart.XYChart;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class DefaultGpxMapper implements IGpxMapper{
    public static final Logger LOGGER = LogManager.getLogger();

    public ExtractedGpxResult map(File gpxFile) throws IOException {
        return map(gpxFile, null);
    }

    public ExtractedGpxResult map(File gpxFile, Path outputFolder) throws IOException {
        LOGGER.info("Parsing GPX file {}...", gpxFile.getName());
        List<WayPoint> wayPoints = GpxParser.getWayPoints(gpxFile);
        LOGGER.info("Parsed {} waypoints", wayPoints.size());
        int width = 1000, height = 1400;
        BufferedImage mImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = mImage.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
        StaticMapCreator.drawMap(wayPoints, graphics, width, height);
        drawElevationGraph(wayPoints, height, graphics, width);
        MapImage.writeMapImageToFile(gpxFile, outputFolder, mImage);
        return GpxMetadataExtractor.extract(gpxFile.getName(), wayPoints);
    }

    private static void drawElevationGraph(List<WayPoint> wayPoints, int height, Graphics2D graphics, int width) {
        LOGGER.info("Drawing elevation graph...");
        XYChart xyChart = ElevationGraphCreator.getElevationGraph(wayPoints);
        int chartHeight = 150;
        AffineTransform startFromBottom = AffineTransform.getTranslateInstance(0, height - chartHeight);
        graphics.setTransform(startFromBottom);
        xyChart.paint(graphics, width, chartHeight);
        LOGGER.info("Drawing elevation graph finished");
    }

}
