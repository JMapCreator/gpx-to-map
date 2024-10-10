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
import map.gpx.GpxStyler;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.List;

public class StaticMapCreator {
    public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(StaticMapCreator.class);

    public static void drawMap(List<WayPoint> wayPoints, Graphics2D graphics2D, int width, int height, GpxStyler styler) {
        LOGGER.info("Starting to ping tile server...");
        List<Location> locationList = wayPoints.stream()
                .map(wp -> new Location(wp.getLatitude().doubleValue(), wp.getLongitude().doubleValue()))
                .toList();

        LocationPath locationPath = new LocationPath();
        locationList.forEach(locationPath::addLocation);
        LineString lineString = new LineString(locationPath);
        lineString.strokeWidth(styler.strokeWidth());
        lineString.outlineColor(styler.outlineColor());
        lineString.strokeColor(styler.strokeColor());
        LocationBounds locationBounds = new LocationBounds(locationList);
        StaticMap mp = new StaticMap(width, height);
        TMSLayer baseMap = new TMSLayer(styler.tileProvider().getUrl());
        mp.fitBounds(locationBounds, new Padding(styler.paddingY(),0, styler.paddingX(), 0));
        mp.addLayer(baseMap);
        mp.addLayer(lineString);
        mp.drawInto(graphics2D, new CenterOffset(styler.centerOffsetX(), styler.centerOffsetY()));
        LOGGER.info("Map infos acquired");
    }
}
