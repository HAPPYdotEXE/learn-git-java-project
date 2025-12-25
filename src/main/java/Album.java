import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Album extends AudioCollection {

    Album(){};

    @JsonProperty("items")
    public void setItems(List<Content> contents) {
        List<Song> songs = contents.stream()
                .filter(c -> c instanceof Song)
                .map(c -> (Song) c)
                .toList();

        if (songs != null) {
            this.items.clear();
            this.items.addAll(songs);
            for (Song s : songs) {
                s.setAlbumTitle(this.getTitle());
            }
        }
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