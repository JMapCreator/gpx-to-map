package map.gpx;

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
