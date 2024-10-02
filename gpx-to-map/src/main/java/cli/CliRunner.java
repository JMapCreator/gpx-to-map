package cli;

import folder.PathWalker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

import java.nio.file.Path;
import java.util.concurrent.Callable;

public class CliRunner implements Callable<Integer> {

    private static final Logger LOGGER = LogManager.getLogger();

    @CommandLine.Parameters(index = "0", description = "The folder where to look for gpx files.")
    private Path folder;

    @Override
    public Integer call() throws Exception {
        LOGGER.info("Starting gpx map generator on folder {}...", folder);
        //PathWalker<TomlRunner> pathWalker = new PathWalker<>(folder);
        //pathWalker.updateAll();
        return 0;
    }
}
