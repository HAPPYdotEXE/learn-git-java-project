package model;

import java.time.LocalDate;

public class Playlist extends AudioCollection{

    public Playlist(){}

    public Playlist(String title) {
        super(title, "User created", LocalDate.now().getYear(), Genre.MIXED);
    }

    public void addContent(Content content) {
        if (content instanceof AudioCollection) {
            throw new IllegalArgumentException("Cannot add audio collections to playlists");
        }
        if (items.contains(content)){
            throw new IllegalArgumentException(content.getClass().getSimpleName() + " (" + content.getTitle() +") is already in the playlist");
        }
        items.add(content);
    }
    public void removeContent(Content content) {
        items.remove(content);
    }

    @Override
    public String toString() {
        return String.format("[Playlist] Title: %s, Items: %d, Duration: %s",
                getTitle(), items.size(), getFormatDuration());
    }
}
