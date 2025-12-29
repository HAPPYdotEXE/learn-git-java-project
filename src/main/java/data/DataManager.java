package data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import model.Content;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class DataManager {

    private final ObjectMapper mapper;
    private final String FILE_NAME = "src/main/java/data/test_data.json";

    public DataManager() {
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void save(Set<Content> contentList) {
        try {
            mapper.writerFor(new TypeReference<Set<Content>>() {})
                    .writeValue(new File(FILE_NAME), contentList);
            System.out.println("Success: Saved " + contentList.size() + " items to " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Error saving file:");
            e.printStackTrace();
        }
    }

    public Set<Content> load() {
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) {
                System.out.println("File not found, returning empty list.");
                return new LinkedHashSet<>();
            }
            return mapper.readValue(file, new TypeReference<Set<Content>>() {});
        } catch (IOException e) {
            System.out.println("Error loading file:");
            e.printStackTrace();
            return new LinkedHashSet<>();
        }
    }
}