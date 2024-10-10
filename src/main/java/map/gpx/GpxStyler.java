package map.gpx;

import java.awt.*;

public record GpxStyler(Color backgroundColor,
                        TextToMapPosition textPosition,
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

    public static GpxStyler getDefaultStyler() {
        return new GpxStyler.builder().build();
    }

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
