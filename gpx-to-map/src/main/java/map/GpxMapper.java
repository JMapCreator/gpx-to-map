package map;

import files.ExtractedGpxResult;
import io.jenetics.jpx.WayPoint;
import org.knowm.xchart.XYChart;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class GpxMapper {
    public static ExtractedGpxResult map(File gpxFile) throws IOException {
        return map(gpxFile, null);
    }

    public static ExtractedGpxResult map(File gpxFile, Path outputFolder) throws IOException {
        List<WayPoint> wayPoints = GpxParser.getWayPoints(gpxFile);
        int width = 1000, height = 1400;
        BufferedImage mImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = mImage.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
        StaticMapCreator.drawMap(wayPoints, graphics, width, height);
        drawElevationGraph(wayPoints, height, graphics, width);
        MapImage.writeMapImageToFile(gpxFile, outputFolder, mImage);
        return GpxMetadataExtractor.extract(wayPoints);
    }

    private static void drawElevationGraph(List<WayPoint> wayPoints, int height, Graphics2D graphics, int width) {
        XYChart xyChart = ElevationGraphCreator.getElevationGraph(wayPoints);
        int chartHeight = 150;
        AffineTransform startFromBottom = AffineTransform.getTranslateInstance(0, height - chartHeight);
        graphics.setTransform(startFromBottom);
        xyChart.paint(graphics, width, chartHeight);
    }

}
