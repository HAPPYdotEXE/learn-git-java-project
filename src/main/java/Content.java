import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.Objects;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type" // This will add type property to the objects
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Song.class, name = "song"),
        @JsonSubTypes.Type(value = Podcast.class, name = "podcast"),
        @JsonSubTypes.Type(value = Audiobook.class, name = "audiobook"),
        @JsonSubTypes.Type(value = Playlist.class, name = "playlist"),
        @JsonSubTypes.Type(value = Album.class, name = "album")
})


public abstract class Content implements Comparable<Content> {

    private String title;
    private String author;
    private int publicationYear;
    private Genre genre;
    private BigInteger durationMilliseconds;

    private final static int MAX_STRING_LENGTH = 100;
    public final static BigInteger MAX_DURATION = BigInteger.valueOf(8_786_692_000L); // longest audiobook recoded - Shree Haricharitramrut Sagar (milliseconds)
    public final static int MIN_PUBLICATION_YEAR = 1857; // first ever recorded audio - 1857
    public final static int  MAX_PUBLICATION_YEAR = java.time.LocalDate.now(java.time.ZoneOffset.ofHours(14)).getYear(); // maximum possible year no matter where the server is deployed


    public Content() {
    } // used by jackson to load objects and use setters for the values

    public Content(String title, String author, int publicationYear, Genre genre, BigInteger durationMilliseconds) {
        setTitle(title);
        setAuthor(author);
        setPublicationYear(publicationYear);
        setGenre(genre);
        setDurationMilliseconds(durationMilliseconds);
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public Genre getGenre() {
        return genre;
    }

    public BigInteger getDurationMilliseconds() {
        return durationMilliseconds;
    }

    public void setTitle(String title) {
        this.title = validateString(title, "Title");
    }

    public void setAuthor(String author) {
        this.title = validateString(title, "Author");
    }

    public void setPublicationYear(int publicationYear) {
        if (publicationYear < MIN_PUBLICATION_YEAR || publicationYear > MAX_PUBLICATION_YEAR){
            throw new IllegalArgumentException("Invalid publication year");
        }
        this.publicationYear = publicationYear;
    }

    public void setGenre(Genre genre) {
        if(genre == null){
            throw new IllegalArgumentException("Cannot have null as genre");
        }
        this.genre = genre;
    }

    public void setDurationMilliseconds(BigInteger durationMilliseconds) {
        if (durationMilliseconds.compareTo(MAX_DURATION) > 0) {
            throw new IllegalArgumentException("Duration exceeded");
        }
        if (durationMilliseconds.compareTo(BigInteger.valueOf(0)) < 0){
            throw new IllegalArgumentException("Duration below zero!");
        }
        this.durationMilliseconds = durationMilliseconds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Content content = (Content) o;
        return Objects.equals(title, content.title) && Objects.equals(author, content.author) && Objects.equals(publicationYear, content.publicationYear);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author);
    }

    @Override
    public int compareTo(Content other) {
        return this.title.compareTo(other.title);
    }

    public String formatDuration() {
        BigInteger totalSeconds = durationMilliseconds.divide(BigInteger.valueOf(1000));

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

    public static Comparator<Content> BY_TITLE = Comparator.comparing(s -> s.title);
    public static Comparator<Content> BY_AUTHOR = Comparator.comparing(s -> s.author);
    public static Comparator<Content> BY_YEAR = Comparator.comparing(s -> s.publicationYear);

    public abstract void displayInfo();

    protected String validateString(String value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " must not be null");
        }

        String trimmedValue = value.trim();

        if (trimmedValue.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }

        if (trimmedValue.length() > MAX_STRING_LENGTH) {
            throw new IllegalArgumentException(fieldName + " length must be less than or equal to " + MAX_STRING_LENGTH);
        }

        return trimmedValue;
    }
}
