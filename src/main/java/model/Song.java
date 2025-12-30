package model;

import java.math.BigInteger;

public class Song extends Content{

    private String albumTitle;

    public Song(){}

    public Song(String title, String author, int publicationYear, Genre genre, BigInteger durationSeconds, String albumTitle){
        super(title, author, publicationYear, genre, durationSeconds);
        setAlbumTitle(albumTitle);
    }

    public String getAlbumTitle(){
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle){
        this.albumTitle = validateString(albumTitle, "AlbumTitle");
    }

    @Override
    public String toString(){
        return String.format("[Song] Title: %s, Artist: %s, Album: %s, Duration: %s",
                getTitle(), getAuthor(), albumTitle, formatDuration());
    }



}
