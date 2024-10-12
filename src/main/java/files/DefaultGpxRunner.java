package files;

import map.gpx.IGpxMapper;

/**
 * The most basic implementation of a GPX runner
 */
public class DefaultGpxRunner extends AbstractGpxRunner<IGpxMapper> {
    public DefaultGpxRunner(IGpxMapper gpxMapper) {
        super(gpxMapper);
    }
}
