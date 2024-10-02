package folder;

import files.GpxFileRunner;

import java.nio.file.Path;

public class PathWalker<T extends GpxFileRunner> {
    private final Path path;

    public PathWalker(Path path) {
        this.path = path;
    }

    public void updateAll() {

    }
}
