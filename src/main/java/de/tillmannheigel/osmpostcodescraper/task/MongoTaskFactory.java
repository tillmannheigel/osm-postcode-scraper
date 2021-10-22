package de.tillmannheigel.osmpostcodescraper.task;

import java.util.Collections;

import org.openstreetmap.osmosis.core.pipeline.common.TaskConfiguration;
import org.openstreetmap.osmosis.core.pipeline.common.TaskManager;
import org.openstreetmap.osmosis.core.pipeline.common.TaskManagerFactory;
import org.openstreetmap.osmosis.core.pipeline.v0_6.SinkManager;

public class MongoTaskFactory extends TaskManagerFactory {

    @Override
    public TaskManager createTaskManagerImpl(TaskConfiguration taskConfig) {
        return new SinkManager("taskId", new MongoTask(), Collections.emptyMap());
    }
}
