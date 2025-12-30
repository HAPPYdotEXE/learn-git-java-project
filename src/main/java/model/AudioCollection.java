package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class AudioCollection extends Content {

    @JsonProperty("items")
    protected List<Content> items;

    public AudioCollection() {
        this.items = new ArrayList<>();
    }

    public AudioCollection(String title, String author, int publicationYear, Genre genre) {
        super(title, author, publicationYear, genre, BigInteger.valueOf(0));
        this.items = new ArrayList<>();
    }

    public List<Content> getItems() {
        return items;
    }

    @JsonProperty("items")
    public void setItems(List<Content> content) {
        if(content != null){
            this.items.clear();
            this.items.addAll(content);
        }
    }

    @JsonIgnore
    public BigInteger getDurationSeconds() {
        return items.stream()
                .map(Content::getDurationSeconds)
                .reduce(BigInteger.ZERO, BigInteger::add);
    }

    @JsonIgnore
    public String getFormatDuration() {
        BigInteger hours = getDurationSeconds().divide(BigInteger.valueOf(3600));
        BigInteger remainingSeconds = getDurationSeconds().remainder(BigInteger.valueOf(3600));
        BigInteger minutes = remainingSeconds.divide(BigInteger.valueOf(60));
        BigInteger seconds = remainingSeconds.remainder(BigInteger.valueOf(60));

        if (hours.compareTo(BigInteger.ZERO) > 0) {
            return String.format("%dh %dm %02ds", hours, minutes, seconds);
        } else {
            return String.format("%dm %02ds", minutes, seconds);
        }
    }

    public void sortByTitle() {
        items.sort(Content.BY_TITLE);
    }

    public void sortByAuthor() {
        items.sort(Content.BY_AUTHOR);
    }

    public void sortByYear() {
        items.sort(Content.BY_YEAR);
    }

    public void sortDefault() {
        Collections.sort(items);
    }
}
