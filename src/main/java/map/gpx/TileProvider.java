package map.gpx;

/**
 * Simple wrapper around the URL of a tile server.
 * The <b>{z}{x}{y}</b> symbols are mandatory as they will be used by the {@link com.hotcoffee.staticmap.StaticMap} library
 * to request each specific tile.
 */
public enum TileProvider {
    ARCGIS_ONLINE(
            "https://services.arcgisonline.com/arcgis/rest/services/World_Topo_Map/MapServer/tile/{z}/{y}/{x}.png");

    private final String url;

    TileProvider(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

}
