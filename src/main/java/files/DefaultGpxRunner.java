package files;

import map.IGpxMapper;

public class DefaultGpxRunner extends AbstractGpxRunner<IGpxMapper> {
    public DefaultGpxRunner(IGpxMapper gpxMapper) {
        super(gpxMapper);
    }
}
