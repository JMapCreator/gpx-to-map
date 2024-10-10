package map.gpx;

import java.awt.Color;

public record GpxStyler(int chartHeight,
        Color backgroundColor,
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
        boolean displayElevationGraph) {

    public static class builder {
        private static final Color LIGHT_BLUE = new Color(134, 171, 210);
        int chartHeight = 200;
        Color backgroundColor = Color.WHITE;
        TextToMapPosition textPosition = TextToMapPosition.BOTTOM;
        int strokeWidth = 3;
        Color strokeColor = LIGHT_BLUE;
        Color outline = Color.BLACK;
        int centerOffsetX = 0;
        int centerOffsetY = 0;
        int paddingX = 0;
        int paddingY = 0;
        TileProvider tileProvider = TileProvider.ARCGIS_ONLINE;
        boolean showStartingPoints = false;
        boolean displayElevationGraph = false;

        public GpxStyler build() {
            return new GpxStyler(centerOffsetX, backgroundColor, textPosition, centerOffsetX, backgroundColor,
                    backgroundColor, centerOffsetX, centerOffsetX, centerOffsetX, centerOffsetX, tileProvider,
                    displayElevationGraph, displayElevationGraph);
        }

    }
}
