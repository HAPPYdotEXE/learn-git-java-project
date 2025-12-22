import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

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
    private String seriesName;
    private int publicationYear;
    private Genre genre;
    private long durationMilliseconds;

    private final static long MAX_DURATION = 8_786_692_000L; // longest audiobook recoded - Shree Haricharitramrut Sagar (milliseconds)
    private final static int MIN_PUBLICATION_YEAR = 1857; // first ever recorded audio - 1857
    private final static int  MAX_PUBLICATION_YEAR = java.time.LocalDate.now(java.time.ZoneOffset.ofHours(14)).getYear(); // maximum possible year no matter where the server is deployed


    public Content() {
    } // used by jackson to load objects and use setters for the values

    public Content(String title, String author, int publicationYear, Genre genre, long durationMilliseconds) {
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
        return seriesName;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public Genre getGenre() {
        return genre;
    }

    public long getDurationSeconds() {
        return durationMilliseconds;
    }

    public void setTitle(String title) {
        if (title == null) {
            throw new IllegalArgumentException("Title must not be null");
        }
        title = title.trim();
        if (title.isBlank()) {
            throw new IllegalArgumentException("Title must not be blank");
        }
        if (title.length() >= 150) {
            throw new IllegalArgumentException("Title length must be less than or equal to 150");
        }
        this.title = title;
    }

    public void setAuthor(String author) {
        if (author == null) {
            throw new IllegalArgumentException("Author must not be null");
        }
        author = author.trim();
        if (author.isBlank()) {
            throw new IllegalArgumentException("Author must not be blank");
        }
        if (author.length() >= 150) {
            throw new IllegalArgumentException("Author length must be less than or equal to 150");
        }
        this.seriesName = author;
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

    public void setDurationMilliseconds(long durationMilliseconds) {
        if (durationMilliseconds > MAX_DURATION) {
            throw new IllegalArgumentException("Duration exceeded");
        }
        if (durationMilliseconds < 0){
            throw new IllegalArgumentException("Duration below zero!");
        }
        this.durationMilliseconds = durationMilliseconds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Content content = (Content) o;
        return Objects.equals(title, content.title) && Objects.equals(seriesName, content.seriesName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, seriesName);
    }

    @Override
    public int compareTo(Content other) {
        return this.title.compareTo(other.title);
    }

    public String formatDuration() {
        long durationSeconds = durationMilliseconds / 1000;
        long minutes = durationSeconds / 60;
        long seconds = durationSeconds % 60;
        return String.format("%d min. %02d sec.", minutes, seconds);
    }

    public static Comparator<Content> BY_TITLE = Comparator.comparing(s -> s.title);
    public static Comparator<Content> BY_AUTHOR = Comparator.comparing(s -> s.seriesName);
    public static Comparator<Content> BY_YEAR = Comparator.comparing(s -> s.publicationYear);

    public abstract void displayInfo();
}
