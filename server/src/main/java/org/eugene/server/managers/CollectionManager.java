package org.eugene.server.managers;

import org.eugene.common.exceptions.ValidationException;
import org.eugene.common.modelCSV.SpaceMarine;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;


public class CollectionManager {
    private final LocalDateTime initDate = LocalDateTime.now();
    private LinkedHashSet<SpaceMarine> collection;

    public CollectionManager() throws ValidationException, IOException {
        this.collection = new LinkedHashSet<>();
    }

    public LinkedHashSet<SpaceMarine> getCollection() {
        return this.collection;
    }

    public void setCollection(LinkedHashSet<SpaceMarine> collection) {
        this.collection = collection;
    }

    public void clearCollection() {
        collection.clear();
    }

    public void sortCollection() {

    }

    public LocalDateTime getInitDate() {
        return initDate;
    }

    public void addSpaceMarine(SpaceMarine spaceMarine) {

    }
}
