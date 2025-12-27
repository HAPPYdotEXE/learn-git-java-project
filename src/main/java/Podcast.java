import java.math.BigInteger;

public class Podcast extends Content{

    private String seriesName;
    private int episodeNumber;

    public Podcast(){
    }

    public Podcast(String title, String author, int publicationYear, Genre genre, BigInteger durationSeconds,
                   String seriesName, int episodeNumber){
        super(title, author, publicationYear, genre, durationSeconds);
        setSeriesName(seriesName);
        setEpisodeNumber(episodeNumber);
    }

    public String getSeriesName() {
        return seriesName;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = validateString(seriesName, "SeriesName");
    }

    public void setEpisodeNumber(int episodeNumber) {
        if (episodeNumber <= 0){
            throw new IllegalArgumentException("Invalid episode number (below zero)");
        }
        this.episodeNumber = episodeNumber;
    }

    @Override
    public void displayInfo(){
        System.out.println("--- Podcast Episode: " + getTitle() + " ---");
        System.out.println("Series: " + seriesName + "(Ep. " + episodeNumber + ")");
        System.out.println("Host: " + getAuthor());
        System.out.println("Genre: " + getGenre());
        System.out.println("Year: " + getPublicationYear());
        System.out.println("Duration: " + formatDuration());
    }

    @Override
    public String toString() {
        return String.format("[Podcast] Series: %s, Title: %s (E%d), Host: %s, Duration: %s",
                seriesName, getTitle(), episodeNumber, getGenre(), formatDuration());
    }
}
