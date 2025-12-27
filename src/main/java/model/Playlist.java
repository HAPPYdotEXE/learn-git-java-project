package model;

import java.time.LocalDate;

public class Playlist extends AudioCollection{

    public Playlist(){}

    public Playlist(String title) {
        super(title, "User created", LocalDate.now().getYear(), Genre.MIXED);
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
        items.remove(content);
    }

    @Override
    public String toString() {
        return String.format("[core.Playlist] Title: %s, Items: %d, Duration: %s",
                getTitle(), items.size(), getFormatDuration());
    }
}
