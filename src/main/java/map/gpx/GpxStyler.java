package map.gpx;

import java.awt.*;

/**
 * This class represents the available configuration when building a {@link IGpxMapper}.
 *
 * @param backgroundColor       global background color of the image
 * @param graphPosition         whether the graph should be located on top or bottom of the image
 * @param strokeWidth           width of the stroke of the GPX path on the map
 * @param strokeColor           color of the stroke of the GPX path on the map
 * @param outlineColor          outline color of the stroke of the GPX path on the map
 * @param centerOffsetX         offset the center point of the GPX path on the map on the X axis
 * @param centerOffsetY         offset the center point of the GPX path on the map on the Y axis
 * @param paddingX              adds padding on top and bottom of the GPX path on the map
 * @param paddingY              adds padding on the left and right of the GPX path on the map
 * @param tileProvider          the provider of the tiles (will change the map appearance)
 * @param showStartingPoints    whether the starting point of each track on the GPX file should be shown on the map
 * @param displayElevationGraph whether the elevation should be displayed or not
 * @param separateFiles         whether the elevation graph and map should be written in separate files
 * @param graphFillColor        color of the graph area
 * @param graphLineColor        color of the graph line
 * @param graphChartPadding     padding around the graph chart
 * @param graphPlotMargin       margin around the graph plot
 */
public record GpxStyler(Color backgroundColor,
                        GraphToMapPosition graphPosition,
                        int strokeWidth,
                        Color strokeColor,
                        Color outlineColor,
                        int centerOffsetX,
                        int centerOffsetY,
                        int paddingX,
                        int paddingY,
                        TileProvider tileProvider,
                        boolean showStartingPoints,
                        boolean displayElevationGraph,
                        boolean separateFiles,
                        Color graphFillColor,
                        Color graphLineColor,
                        int graphPlotMargin,
                        int graphChartPadding) {

    /**
     * Provide a default {@link GpxStyler} with standard values.
     *
     * @return default {@link GpxStyler}
     */
    public static GpxStyler getDefaultStyler() {
        return new GpxStyler.builder().build();
    }

    /**
     * Use this builder to easily configure a {@link GpxStyler}.
     * All fields have default values and thus are optional.
     */
    public static class builder {
        private static final Color LIGHT_BLUE = new Color(134, 171, 210);
        private Color backgroundColor = Color.WHITE;
        private GraphToMapPosition graphPosition = GraphToMapPosition.BOTTOM;
        private int strokeWidth = 3;
        private Color strokeColor = LIGHT_BLUE;
        private Color outlineColor = Color.BLACK;
        private int centerOffsetX = 0;
        private int centerOffsetY = 0;
        private int paddingX = 0;
        private int paddingY = 0;
        private TileProvider tileProvider = TileProvider.ARCGIS_ONLINE;
        private boolean showStartingPoints = false;
        private boolean displayElevationGraph = true;
        private boolean separateFiles = false;
        private Color graphFillColor = new Color(134, 171, 210, 125);
        private Color graphLineColor = Color.BLACK;
        private int graphPlotMargin = 0;
        private int graphChartPadding = 0;

        public GpxStyler build() {
            return new GpxStyler(backgroundColor, graphPosition, strokeWidth, strokeColor, outlineColor, centerOffsetX, centerOffsetY,
                    paddingX, paddingY, tileProvider, showStartingPoints, displayElevationGraph, separateFiles, graphFillColor, graphLineColor,
                    graphPlotMargin, graphChartPadding);
        }

        public builder withBackgroundColor(Color backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public builder withGraphPosition(GraphToMapPosition graphPosition) {
            this.graphPosition = graphPosition;
            return this;
        }

        public builder withStrokeWidth(int strokeWidth) {
            this.strokeWidth = strokeWidth;
            return this;
        }

        public builder withStrokeColor(Color strokeColor) {
            this.strokeColor = strokeColor;
            return this;
        }

        public builder withOutlineColor(Color outlineColor) {
            this.outlineColor = outlineColor;
            return this;
        }

        public builder withCenterOffsetX(int centerOffsetX) {
            this.centerOffsetX = centerOffsetX;
            return this;
        }

        public builder withCenterOffsetY(int centerOffsetY) {
            this.centerOffsetY = centerOffsetY;
            return this;
        }

        public builder withPaddingX(int paddingX) {
            this.paddingX = paddingX;
            return this;
        }

        public builder withPaddingY(int paddingY) {
            this.paddingY = paddingY;
            return this;
        }

        public builder withTileProvider(TileProvider tileProvider) {
            this.tileProvider = tileProvider;
            return this;
        }

        public builder withShowStartingPoints(boolean showStartingPoints) {
            this.showStartingPoints = showStartingPoints;
            return this;
        }

        public builder withDisplayElevationGraph(boolean displayElevationGraph) {
            this.displayElevationGraph = displayElevationGraph;
            return this;
        }

        public builder withGraphFillColor(Color graphFillColor) {
            this.graphFillColor = graphFillColor;
            return this;
        }

        public builder withGraphLineColor(Color graphLineColor) {
            this.graphLineColor = graphLineColor;
            return this;
        }

        public builder withGraphPlotMargin(int graphPlotMargin) {
            this.graphPlotMargin = graphPlotMargin;
            return this;
        }

        public builder withGraphChartPadding(int graphChartPadding) {
            this.graphChartPadding = graphChartPadding;
            return this;
        }

        public builder withSeparateFiles(boolean separateFiles) {
            this.separateFiles = separateFiles;
            return this;
        }
    }
}
