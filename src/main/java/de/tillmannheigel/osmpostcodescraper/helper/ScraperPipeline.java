package de.tillmannheigel.osmpostcodescraper.helper;

import java.util.List;

import org.openstreetmap.osmosis.core.OsmosisRuntimeException;
import org.openstreetmap.osmosis.core.pipeline.common.PipeTasks;
import org.openstreetmap.osmosis.core.pipeline.common.Pipeline;
import org.openstreetmap.osmosis.core.pipeline.common.TaskManager;

public class ScraperPipeline extends Pipeline {

    public List<TaskManager> taskManagers;

    public ScraperPipeline() {
        super(null);
    }

    public void connectTasks() {
        PipeTasks pipeTasks;

        // Create a container to map between the pipe name and the task that has
        // last written to it.
        pipeTasks = new PipeTasks();

        // Request each node to perform connection, each node will update the
        // pipe tasks as it provides and consumes pipes.
        for (TaskManager taskManager : taskManagers) {
            taskManager.connect(pipeTasks);
        }

        // Validate that no pipes are left without sinks.
        if (pipeTasks.size() > 0) {
            StringBuilder namedPipes;

            // Build a list of pipes to include in the error.
            namedPipes = new StringBuilder();
            for (String pipeName : pipeTasks.getPipeNames()) {
                if (namedPipes.length() > 0) {
                    namedPipes.append(", ");
                }
                namedPipes.append(pipeName);
            }

            throw new OsmosisRuntimeException(
                    "The following named pipes (" + namedPipes + ") and "
                            + pipeTasks.defaultTaskSize()
                            + " default pipes have not been terminated with appropriate output sinks."
            );
        }
    }

    @Override
    public void execute() {
        for (TaskManager taskManager : taskManagers) {
            taskManager.execute();
        }
    }

}
