package files;

import map.gpx.IGpxMapper;

public class DefaultGpxRunner extends AbstractGpxRunner<IGpxMapper> {
    public DefaultGpxRunner(IGpxMapper gpxMapper) {
        super(gpxMapper);
    }
}
