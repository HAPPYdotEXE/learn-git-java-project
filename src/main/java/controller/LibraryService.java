package controller;

import data.DataManager;
import model.AudioCollection;
import model.Content;
import java.util.*;
import java.util.stream.Stream;

public class LibraryService {
    private final DataManager dataManager;
    private Set<Content> catalog;

    public LibraryService() {
        this.dataManager = new DataManager();
        this.catalog = new LinkedHashSet<>();
    }

    public void load() {
        this.catalog = dataManager.load();
    }

    public void save() {
        dataManager.save(catalog);
    }

    public Set<Content> getCatalog() {
        return catalog;
    }

    public Stream<Content> streamAll() {
        return catalog.stream()
                .flatMap(content -> {
                    List<Content> expanded = new ArrayList<>();
                    expanded.add(content);

                    if (content instanceof AudioCollection pl) {
                        expanded.addAll(pl.getItems());
                    }
                    return expanded.stream();
                })
                .distinct();
    }
}