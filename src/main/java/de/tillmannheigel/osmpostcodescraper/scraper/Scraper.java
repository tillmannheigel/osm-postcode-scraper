package de.tillmannheigel.osmpostcodescraper.scraper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.openstreetmap.osmosis.core.pipeline.common.TaskConfiguration;
import org.openstreetmap.osmosis.core.pipeline.common.TaskManager;
import org.openstreetmap.osmosis.pbf2.v0_6.PbfReaderFactory;
import org.openstreetmap.osmosis.tagfilter.v0_6.TagFilterFactory;
import org.openstreetmap.osmosis.tagfilter.v0_6.UsedNodeFilterFactory;
import org.openstreetmap.osmosis.tagfilter.v0_6.UsedWayFilterFactory;
import org.openstreetmap.osmosis.xml.v0_6.XmlWriterFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import de.tillmannheigel.osmpostcodescraper.task.MongoTaskFactory;
import de.tillmannheigel.osmpostcodescraper.helper.ScraperPipeline;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scraper {

    @Value("classpath:input.osm.pbf")
    Resource resourceFile;

    public void scrape() throws IOException {

        // read
        PbfReaderFactory pbfReaderFactory = new PbfReaderFactory();
        TaskConfiguration read = new TaskConfiguration("read", "read-pbf", Collections.emptyMap(), Collections.emptyMap(), resourceFile.getFile().getAbsolutePath());
        TaskManager pbfReaderFactoryTaskManager = pbfReaderFactory.createTaskManager(read);

        // filter
        TagFilterFactory tagFilterFactory = new TagFilterFactory();
        TaskConfiguration tagFilter = new TaskConfiguration("tagFilter", "tag-filter", Collections.emptyMap(), Map.of("type", "boundary"), "accept-relations");
        TaskManager tagFilterFactoryTaskManager = tagFilterFactory.createTaskManager(tagFilter);

        TagFilterFactory tagFilterFactory2 = new TagFilterFactory();
        TaskConfiguration tagFilter2 = new TaskConfiguration("tagFilter", "tag-filter", Collections.emptyMap(), Map.of("postal_code", "*"), "accept-relations");
        TaskManager tagFilterFactoryTaskManager2 = tagFilterFactory2.createTaskManager(tagFilter2);

        UsedNodeFilterFactory usedNodeFilterFactory = new UsedNodeFilterFactory();
        TaskConfiguration usedNodeFilter = new TaskConfiguration("usedNodeFilter", "used-node-filter", Collections.emptyMap(), Collections.emptyMap(), null);
        TaskManager usedNodeFilterFactoryTaskManager = usedNodeFilterFactory.createTaskManager(usedNodeFilter);

        UsedWayFilterFactory usedWayFilterFactory = new UsedWayFilterFactory();
        TaskConfiguration usedWayFilter = new TaskConfiguration("usedWayFilter", "used-way-filter", Collections.emptyMap(), Collections.emptyMap(), null);
        TaskManager usedWayFilterFactoryTaskManager = usedWayFilterFactory.createTaskManager(usedWayFilter);

        // write
        MongoTaskFactory mongoTaskFactory = new MongoTaskFactory();
        TaskManager mongoTaskFactoryTaskManager = mongoTaskFactory.createTaskManagerImpl(null);

        XmlWriterFactory xmlWriterFactory = new XmlWriterFactory();
        TaskConfiguration write = new TaskConfiguration("write", "write-xml", Collections.emptyMap(), Collections.emptyMap(), "output.xml");
        TaskManager xmlWriterFactoryTaskManager = xmlWriterFactory.createTaskManager(write);

        ScraperPipeline pipeline = new ScraperPipeline();
        pipeline.taskManagers = List.of(pbfReaderFactoryTaskManager, tagFilterFactoryTaskManager, tagFilterFactoryTaskManager2, usedNodeFilterFactoryTaskManager, usedWayFilterFactoryTaskManager, mongoTaskFactoryTaskManager);
        pipeline.connectTasks();
        pipeline.execute();
    }
}
