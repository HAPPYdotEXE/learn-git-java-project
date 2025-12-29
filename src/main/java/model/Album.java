package model;

import java.util.List;

public class Album extends AudioCollection {

    Album() {
    }

    @Override
    public void setItems(List<Content> content) {
        List<Song> songs = content.stream()
                .filter(c -> c instanceof Song)
                .map(c -> (Song) c)
                .toList();
        for(Song s : songs){
            s.setAlbumTitle(this.getTitle());
        }
        super.setItems(content);
    }

    @Override
    public String toString() {
        return String.format("[Album] Title: %s, Artist: %s, Year: %d, Duration: %s, Songs: %d",
                getTitle(), getAuthor(), getPublicationYear(), getFormatDuration(), items.size());
    }

}