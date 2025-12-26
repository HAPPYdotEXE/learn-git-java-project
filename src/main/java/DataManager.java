import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private final ObjectMapper mapper;
    private final String FILE_NAME = "test_data.json";

    public DataManager() {
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public void save(List<Content> contentList) {
        try {
            mapper.writerFor(new TypeReference<List<Content>>() {})
                    .writeValue(new File(FILE_NAME), contentList);
            System.out.println("Success: Saved " + contentList.size() + " items to " + FILE_NAME);
        } catch (IOException e) {
            System.out.println("Error saving file:");
            e.printStackTrace();
        }
    }

    public List<Content> load() {
        try {
            File file = new File(FILE_NAME);
            if (!file.exists()) {
                System.out.println("File not found, returning empty list.");
                return new ArrayList<>();
            }
            return mapper.readValue(file, new TypeReference<List<Content>>() {});
        } catch (IOException e) {
            System.out.println("Error loading file:");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}