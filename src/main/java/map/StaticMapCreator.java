package map;

import com.hotcoffee.staticmap.CenterOffset;
import com.hotcoffee.staticmap.StaticMap;
import com.hotcoffee.staticmap.geo.Location;
import com.hotcoffee.staticmap.geo.LocationBounds;
import com.hotcoffee.staticmap.geo.LocationPath;
import com.hotcoffee.staticmap.layers.Padding;
import com.hotcoffee.staticmap.layers.TMSLayer;
import com.hotcoffee.staticmap.layers.components.LineString;
import io.jenetics.jpx.WayPoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.util.List;

public class StaticMapCreator {
    public static final Logger LOGGER = LogManager.getLogger();

    public static void drawMap(List<WayPoint> wayPoints, Graphics2D graphics2D, int width, int height) {
        LOGGER.info("Starting to ping tile server...");
        List<Location> locationList = wayPoints.stream()
                .map(wp -> new Location(wp.getLatitude().doubleValue(), wp.getLongitude().doubleValue()))
                .toList();

        LocationPath locationPath = new LocationPath();
        locationList.forEach(locationPath::addLocation);
        LineString lineString = new LineString(locationPath);
        lineString.strokeWidth(3);
        lineString.outlineColor(Color.BLACK);
        lineString.strokeColor(new Color(134, 171, 210));
        LocationBounds locationBounds = new LocationBounds(locationList);
        StaticMap mp = new StaticMap(width, height);
        TMSLayer baseMap = new TMSLayer("https://services.arcgisonline.com/arcgis/rest/services/World_Topo_Map/MapServer/tile/{z}/{y}/{x}.png");
        mp.fitBounds(locationBounds, new Padding(0, 200, 0, 0));
        mp.addLayer(baseMap);
        mp.addLayer(lineString);
        mp.drawInto(graphics2D, new CenterOffset(0, 100));
        LOGGER.info("Map infos acquired");
    }
}
