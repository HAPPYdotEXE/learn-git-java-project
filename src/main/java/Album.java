import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class Album extends AudioCollection {

    Album(){};

    @JsonProperty("items")
    public void setItems(List<Song> songs) {
        if (songs != null) {
            this.items.clear();
            this.items.addAll(songs);
            for (Song s : songs) {
                s.setAlbumTitle(this.getTitle());
            }
        }
    }


    @Override
    public boolean addContent(Content content){
        System.out.println("Operation Denied: Cannot add songs to an official Album after it's been issued.");
        return false;
    }

    @Override
    public void removeContent(Content content){
        System.out.println("Cannot remove songs from an official Album.");
    }


    @Override
    public void displayInfo(){
        System.out.println("\n*** Album: " + getTitle() + " ***");
        System.out.println("Arist: " + getAuthor());
        System.out.println("Year: " + getPublicationYear());
        System.out.println("Genre: " + getGenre());
        System.out.println("Entire duration: " + getFormatDuration());
        System.out.println("Number of songs: " + items.size());

        System.out.println("--- List of songs ---");
        int index = 1;
        for(Content c : items) {
            System.out.printf("%d. %s - (%s)\n", index++, c.getTitle(), c.formatDuration());
        }
    }

}