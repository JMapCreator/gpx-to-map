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
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Map image creation from GPX
 */
public class StaticMapCreator {
    public static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(StaticMapCreator.class);

    /**
     * Gets tiles from server, fits the GPX path in the image and draws it
     *
     * @param wayPoints  the list of waypoints resulting of the parsing of the GPX file*
     * @param width      width of the final image
     * @param height     height of the final image
     * @param styler     the style of the static map
     */
    public static BufferedImage createMap(List<WayPoint> wayPoints, int width, int height, GpxStyler styler) {
        BufferedImage mImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = mImage.createGraphics();
        graphics.setColor(styler.backgroundColor());
        graphics.fillRect(0, 0, width, height);
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
        mp.fitBounds(locationBounds, new Padding(styler.paddingY(), 0, styler.paddingX(), 0));
        mp.addLayer(baseMap);
        mp.addLayer(lineString);
        mp.drawInto(graphics, new CenterOffset(styler.centerOffsetX(), styler.centerOffsetY()));
        LOGGER.info("Map infos acquired");
        return mImage;
    }
}
