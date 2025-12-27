package model;

import java.math.BigInteger;

public class Audiobook extends Content{

    private String narrator;
    private String publisher;
    private int initialPublicationYear;

    public Audiobook(){}

    public Audiobook(String title, String author, int publicationYear, Genre genre,
                     BigInteger durationSeconds, String narrator, String publisher, int initialPublicationYear){
        super(title, author, publicationYear, genre, durationSeconds);
        setNarrator(narrator);
        setPublisher(publisher);
        setInitialPublicationYear(initialPublicationYear);
    }

    public String getNarrator() {
        return narrator;
    }
    public String getPublisher(){
        return publisher;
    }
    public int getInitialPublicationYear() {
        return initialPublicationYear;
    }
    public void setNarrator(String narrator){
        this.narrator = validateString(narrator, "Narrator");
    }
    public void setPublisher(String publisher){
        this.publisher = validateString(publisher, "Publisher");
    }
    public void setInitialPublicationYear(int initialPublicationYear){
        if (initialPublicationYear > Content.MAX_PUBLICATION_YEAR){
            throw new IllegalArgumentException("Invalid publication year");
        }
        this.initialPublicationYear = initialPublicationYear;
    }

    @Override
    public String toString() {
        return String.format("[core.Audiobook] Title: %s, Original Author: %s, Narrator: %s, Duration: %s",
                getTitle(), getAuthor(), narrator, formatDuration());
    }
}
