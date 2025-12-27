import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;
import java.util.ArrayList;
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
    public abstract void setItems(List<Content> contents);

    @JsonIgnore
    @Override
    public BigInteger getDurationMilliseconds() {
        return items.stream()
                .map(Content::getDurationMilliseconds)
                .reduce(BigInteger.ZERO, BigInteger::add);
    }

    @JsonIgnore
    public String getFormatDuration() {
        BigInteger totalSeconds = getDurationMilliseconds().divide(BigInteger.valueOf(1000));
        BigInteger hours = totalSeconds.divide(BigInteger.valueOf(3600));
        BigInteger remainingSeconds = totalSeconds.remainder(BigInteger.valueOf(3600));
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
        System.out.println(this.getClass().getSimpleName() + " \"" + getTitle() + "\" is sorted by title.");
    }

    public void sortByAuthor() {
        items.sort(Content.BY_AUTHOR);
        System.out.println(this.getClass().getSimpleName() + " \"" + getTitle() + "\" is sorted by author.");
    }

    public void sortByYear() {
        items.sort(Content.BY_YEAR);
        System.out.println(this.getClass().getSimpleName() + " \"" + getTitle() + "\" is sorted by publication year.");
    }

    public void sortDefault() {
        Collections.sort(items);
    }
}
