import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

public class Playlist extends AudioCollection{

    public Playlist(){}

    public Playlist(String title) {
        super(title, "User created", LocalDate.now().getYear(), Genre.MIXED);
    }

    @JsonProperty("items")
    public void setItems(List<Content> content) {
        if(content != null){
            this.items.clear();
            this.items.addAll(content);
        }
    }

    public void addContent(Content content) {
        if (content instanceof Album) {
            throw new IllegalArgumentException("Error: cannot add albums to playlists");
        }
        if (items.contains(content)){
            throw new IllegalArgumentException("Error:" + content.getClass().getSimpleName() + " (" + content.getTitle() +") is already in the playlist");
        }
        items.add(content);
    }
    public void removeContent(Content content) {
        if (items.remove(content)){
            System.out.println(String.format("Removed: %s \nFrom: %s", content.toString(), this.getTitle()));
        }
    }

    @Override
    public void displayInfo(){
        System.out.println("\n--- Playlist: " + getTitle() + " ---");
        System.out.println("Elements: " + items.size());
        System.out.println("Duration: " + getFormatDuration());

        if (items.isEmpty()){
            System.out.println("The playlist is empty!");
            return;
        }

        System.out.println("--- Content ---");
        int index = 1;
        for(Content c : items){
            System.out.printf("%d. %s\n", index++, c.toString());
        }
    }

    @Override
    public String toString() {
        return String.format("[Playlist] Title: %s, Items: %d, Duration: %s",
                getTitle(), items.size(), getFormatDuration());
    }
}
