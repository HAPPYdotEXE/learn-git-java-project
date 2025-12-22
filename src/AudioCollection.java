import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AudioCollection extends Content {

    @JsonProperty("items")
    protected List<Content> items;

    public AudioCollection(){}

    public AudioCollection(String title, String author, int publicationYear, Genre genre) {
        super(title, author, publicationYear, genre, 0);
        this.items = new ArrayList<>();
    }


    public abstract boolean addContent(Content content);

    public boolean removeContent(Content content){
        if(items.remove(content)){
            System.out.println(String.format("Removed: %s \nFrom: %s", content.toString(), this.getTitle()));
            return true;
        } else {
            System.out.println(String.format("Failed to remove: %s \nReason: Item not found in %s", content.getTitle(), this.getTitle()));
            return false;
        }
    }

    @Override
    public long getDurationSeconds(){

        return items.stream()
                .mapToLong(s-> s.getDurationSeconds())
                .sum();
    }

    public String getFormatDuration(){
        long durationSeconds = getDurationSeconds();
        long minutes = durationSeconds / 60;
        long seconds = durationSeconds % 60;
        return String.format("%d min. %02d sec.", minutes, seconds);
    }

    public void sortByTitle(){
        items.sort(Content.BY_TITLE);
        System.out.println(this.getClass().getSimpleName() + " \"" + getTitle() + "\" is sorted by title.");    }

    public void sortByAuthor(){
        items.sort(Content.BY_AUTHOR);
        System.out.println(this.getClass().getSimpleName() + " \"" + getTitle() + "\" is sorted by author.");    }

    public void sortByYear(){
        items.sort(Content.BY_YEAR);
        System.out.println(this.getClass().getSimpleName() + " \"" + getTitle() + "\" is sorted by publication year.");    }

    public void sortDefault(){
        Collections.sort(items);
    }

    public List<Content> getItems(){
        return items;
    }


}
