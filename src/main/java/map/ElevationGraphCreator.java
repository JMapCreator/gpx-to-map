package map;

import io.jenetics.jpx.Length;
import io.jenetics.jpx.WayPoint;
import map.gpx.GpxStyler;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Composing and drawing an elevation graph from GPX elevation data
 */
public class ElevationGraphCreator {
    public static final Logger LOGGER = LoggerFactory.getLogger(ElevationGraphCreator.class);

    /**
     * Compose an {@link XYChart} from GPX input data. This chart can be configured using the provided {@link GpxStyler}.
     *
     * @param wayPoints   waypoints parsed from the GPX file
     * @param chartWidth  width of the elevation graph
     * @param chartHeight height of the elevation graph
     * @param styler      {@link GpxStyler} used to configure the graph
     * @return a {@link BufferedImage} containing the graph drawing
     */
    public static BufferedImage createElevationGraph(List<WayPoint> wayPoints, int chartWidth, int chartHeight, GpxStyler styler) {
        LOGGER.info("Drawing elevation graph...");
        BufferedImage mImage = new BufferedImage(chartWidth, chartHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = mImage.createGraphics();
        graphics.setColor(styler.backgroundColor());
        graphics.fillRect(0, 0, chartWidth, chartHeight);
        XYChart xyChart = ElevationGraphCreator.getElevationGraph(wayPoints, styler);
        xyChart.paint(graphics, chartWidth, chartHeight);
        LOGGER.info("Drawing of elevation graph finished");
        return mImage;
    }

    private static XYChart getElevationGraph(List<WayPoint> wayPoints, GpxStyler styler) {
        double[] indices = getIndices(wayPoints);
        double[] alt = getElevationPoints(wayPoints);
        XYChart chart = new XYChartBuilder().build();
        XYSeries altitudeSeries = chart.addSeries("Altitude", indices, alt);
        altitudeSeries.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Area);
        altitudeSeries.setFillColor(styler.graphFillColor());
        altitudeSeries.setSmooth(true);
        altitudeSeries.setMarker(SeriesMarkers.NONE);
        altitudeSeries.setLineColor(styler.graphLineColor());
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setAxisTitlesVisible(false);
        chart.getStyler().setChartTitleVisible(false);
        chart.getStyler().setXAxisTicksVisible(false);
        chart.getStyler().setPlotMargin(styler.graphPlotMargin());
        chart.getStyler().setPlotContentSize(1D);
        chart.getStyler().setChartPadding(styler.graphChartPadding());
        chart.getStyler().setChartBackgroundColor(new Color(0, 0, 0, 0));
        chart.getStyler().setPlotBackgroundColor(new Color(0, 0, 0, 0));
        chart.getStyler().setPlotBorderVisible(false);
        return chart;

    }

    /**
     * Gets all elevation points. If a point has negative elevation, it will be set to 0, thus avoiding ugly representation on the graph
     *
     * @param wayPoints input waypoints from the GPX file
     * @return an array of elevation points (in meters)
     */
    private static double[] getElevationPoints(List<WayPoint> wayPoints) {
        return wayPoints.stream().mapToDouble(wp -> {
            Double v = wp.getElevation()
                    .map(Length::doubleValue)
                    .orElse(0D);
            if (v < 0) {
                return 0;
            }
            return v;
        }).toArray();
    }

    private static double[] getIndices(List<WayPoint> wayPoints) {
        return IntStream.range(0, wayPoints.size())
                .mapToDouble(i -> i)
                .toArray();
    }
}
