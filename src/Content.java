import java.util.Comparator;
import java.util.Objects;

public abstract class Content implements Comparable<Content> {

    private String title;
    private String author;
    private int publicationYear;
    private String genre;
    private long durationSeconds;

    public Content(String title, String author, int publicationYear, String genre, long durationSeconds){

        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.genre = genre;
        this.durationSeconds = durationSeconds;
    }

    public abstract void displayInfo();

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public String getGenre() {
        return genre;
    }

    public long getDurationSeconds() {
        return durationSeconds;
    }

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Content content = (Content) o;
        return Objects.equals(title, content.title) && Objects.equals(author, content.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author);
    }

    public String formatDuration(){
        long minutes = durationSeconds / 60;
        long seconds = durationSeconds % 60;
        return String.format("%d min. %02d sec.", minutes, seconds);
    }

    @Override
    public int compareTo(Content other) {
        return this.title.compareTo(other.title);
    }

    public static Comparator<Content> BY_TITLE = Comparator.comparing(s -> s.title);
    public static Comparator<Content> BY_AUTHOR = Comparator.comparing( s-> s.author);
    public static Comparator<Content> BY_YEAR = Comparator.comparing(s-> s.publicationYear);
}
