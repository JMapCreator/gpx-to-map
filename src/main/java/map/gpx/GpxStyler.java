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
 * @param graphFillColor        color of the graph area
 * @param graphLineColor        color of the graph line
 */
public record GpxStyler(Color backgroundColor,
                        TextToMapPosition graphPosition,
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
                        Color graphFillColor,
                        Color graphLineColor) {

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
        Color backgroundColor = Color.WHITE;
        TextToMapPosition textPosition = TextToMapPosition.BOTTOM;
        int strokeWidth = 3;
        Color strokeColor = LIGHT_BLUE;
        Color outlineColor = Color.BLACK;
        int centerOffsetX = 0;
        int centerOffsetY = 0;
        int paddingX = 0;
        int paddingY = 0;
        TileProvider tileProvider = TileProvider.ARCGIS_ONLINE;
        boolean showStartingPoints = false;
        boolean displayElevationGraph = true;
        Color graphFillColor = new Color(134, 171, 210, 125);
        Color graphLineColor = Color.BLACK;

        public GpxStyler build() {
            return new GpxStyler(backgroundColor, textPosition, strokeWidth, strokeColor, outlineColor, centerOffsetX, centerOffsetY,
                    paddingX, paddingY, tileProvider, showStartingPoints, displayElevationGraph, graphFillColor, graphLineColor);
        }

        public builder setBackgroundColor(Color backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public builder setTextPosition(TextToMapPosition textPosition) {
            this.textPosition = textPosition;
            return this;
        }

        public builder setStrokeWidth(int strokeWidth) {
            this.strokeWidth = strokeWidth;
            return this;
        }

        public builder setStrokeColor(Color strokeColor) {
            this.strokeColor = strokeColor;
            return this;
        }

        public builder setOutlineColor(Color outlineColor) {
            this.outlineColor = outlineColor;
            return this;
        }

        public builder setCenterOffsetX(int centerOffsetX) {
            this.centerOffsetX = centerOffsetX;
            return this;
        }

        public builder setCenterOffsetY(int centerOffsetY) {
            this.centerOffsetY = centerOffsetY;
            return this;
        }

        public builder setPaddingX(int paddingX) {
            this.paddingX = paddingX;
            return this;
        }

        public builder setPaddingY(int paddingY) {
            this.paddingY = paddingY;
            return this;
        }

        public builder setTileProvider(TileProvider tileProvider) {
            this.tileProvider = tileProvider;
            return this;
        }

        public builder setShowStartingPoints(boolean showStartingPoints) {
            this.showStartingPoints = showStartingPoints;
            return this;
        }

        public builder setDisplayElevationGraph(boolean displayElevationGraph) {
            this.displayElevationGraph = displayElevationGraph;
            return this;
        }

        public builder setGraphFillColor(Color graphFillColor) {
            this.graphFillColor = graphFillColor;
            return this;
        }

        public builder setGraphLineColor(Color graphLineColor) {
            this.graphLineColor = graphLineColor;
            return this;
        }
    }
}
