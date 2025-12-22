import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AudioCollection extends Content {

    @JsonProperty("items")
    protected List<Content> items;

    public AudioCollection() {
    }

    public AudioCollection(String title, String author, int publicationYear, Genre genre) {
        super(title, author, publicationYear, genre, BigInteger.valueOf(0));
        this.items = new ArrayList<>();
    }


    public abstract boolean addContent(Content content);

    public void removeContent(Content content) {
        if (items.remove(content)){
            System.out.println(String.format("Removed: %s \nFrom: %s", content.toString(), this.getTitle()));
        }
    }

    @Override
    public BigInteger getDurationMilliseconds() {
        return items.stream()
                .map(Content::getDurationMilliseconds)
                .reduce(BigInteger.ZERO, BigInteger::add);
    }

    public String getFormatDuration() {
        BigInteger durationSeconds = getDurationMilliseconds();
        BigInteger minutes = durationSeconds.divide(BigInteger.valueOf(60));
        BigInteger seconds = durationSeconds.remainder(BigInteger.valueOf(60));
        return String.format("%d min. %02d sec.", minutes, seconds);
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

    public List<Content> getItems() {
        return items;
    }


}
