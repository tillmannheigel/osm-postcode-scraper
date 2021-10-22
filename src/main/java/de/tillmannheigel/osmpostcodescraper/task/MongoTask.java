package de.tillmannheigel.osmpostcodescraper.task;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.EntityType;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MongoTask implements Sink {

    Set<EntityContainer> nodes = new HashSet<>();
    Set<EntityContainer> ways = new HashSet<>();
    Set<EntityContainer> relations = new HashSet<>();

    @Override
    public void process(EntityContainer entityContainer) {
        if (entityContainer.getEntity().getType().equals(EntityType.Relation)) {
            if (relations.size() < 5)  {
                log.warn("Add Relation: {}", entityContainer.getEntity());
            }
            relations.add(entityContainer);
        }
        if (entityContainer.getEntity().getType().equals(EntityType.Node)) {
            if (nodes.size() < 5)  {
                log.warn("Add Node: {}", entityContainer.getEntity());
            }
            nodes.add(entityContainer);
        }
        if (entityContainer.getEntity().getType().equals(EntityType.Way)) {
            if (ways.size() < 5)  {
                log.warn("Add Way: {}", entityContainer.getEntity());
            }
            ways.add(entityContainer);
        }
    }

    @Override
    public void initialize(Map<String, Object> metaData) {
        log.warn("{}", metaData);
    }

    @Override
    public void complete() {
        log.warn("complete");
        log.warn("Relations: {}", relations.size());
        log.warn("Ways: {}", ways.size());
        log.warn("Nodes: {}", nodes.size());
    }

    @Override
    public void close() {
        log.warn("close");
    }
}
