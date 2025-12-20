import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Playlist extends AudioCollection{


    public Playlist(String title) {
        super(title, "User created", 2025, "Mixed");
    }


    public boolean addContent(Content content) {
        if (content instanceof Album) {
            System.out.println("Error: Cannot add albums to playlist!");
            return false;
        }
        if (items.contains(content)){
            System.out.println("Error:" + content.getClass().getSimpleName() + " (" + content.getTitle() +") is already in the playlist");
            return false;
        }
        return items.add(content);
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

        System.out.println("\n--- Content ---");
        int index = 1;
        for(Content c : items){
            System.out.printf("%d. %s\n", index++, c.toString());
        }
    }

    @JsonProperty("items")
    public void setItems(List<Song> content) {
        if(content != null){
            this.items.clear();
            this.items.addAll(content);
        }
    }
}
