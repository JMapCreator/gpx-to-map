package map;

import io.jenetics.jpx.Length;
import io.jenetics.jpx.WayPoint;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;

import java.awt.*;
import java.util.List;
import java.util.stream.IntStream;

public class ElevationGraphCreator {
    static XYChart getElevationGraph(List<WayPoint> wayPoints) {
        double[] indices = IntStream.range(0, wayPoints.size())
                .mapToDouble(i -> i)
                .toArray();
        double[] alt = wayPoints.stream().mapToDouble(wp -> {
            Double v = wp.getElevation()
                    .map(Length::doubleValue)
                    .orElse(0D);
            if (v < 0) {
                return 0;
            }
            return v;
        }).toArray();
        XYChart chart = new XYChartBuilder().height(500).width(800).build();
        XYSeries altitudeSeries = chart.addSeries("Altitude", indices, alt);
        altitudeSeries.setXYSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Area);
        altitudeSeries.setFillColor(new Color(134, 171, 210, 125));
        altitudeSeries.setSmooth(true);
        altitudeSeries.setMarker(SeriesMarkers.NONE);
        altitudeSeries.setLineColor(Color.BLACK);
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setAxisTitlesVisible(false);
        chart.getStyler().setChartTitleVisible(false);
        chart.getStyler().setXAxisTicksVisible(false);
        chart.getStyler().setPlotMargin(0);
        chart.getStyler().setPlotContentSize(1D);
        chart.getStyler().setChartPadding(0);
        chart.getStyler().setChartBackgroundColor(new Color(0, 0, 0, 0));
        chart.getStyler().setPlotBackgroundColor(new Color(0, 0, 0, 0));
        chart.getStyler().setPlotBorderVisible(false);
        return chart;

    }
}
