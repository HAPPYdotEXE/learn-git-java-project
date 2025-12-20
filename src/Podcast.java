public class Podcast extends Content{

    private String host;
    private String seriesName;
    private int episodeNumber;
    private String topicCategory;

    public Podcast(){
    }

    public Podcast(String title, String host, int publicationYear, String genre, long durationSeconds,
                   String seriesName, int episodeNumber, String topicCategory){
        super(title, host, publicationYear, genre, durationSeconds);

        this.host = host;
        this.seriesName = seriesName;
        this.episodeNumber = episodeNumber;
        this.topicCategory = topicCategory;
    }

    public String getHost() {
        return host;
    }

    public String getSeriesName() {
        return seriesName;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public String getTopicCategory() {
        return topicCategory;
    }

    @Override
    public void displayInfo(){
        System.out.println("--- Podcast Episode: " + getTitle() + " ---");
        System.out.println("Series: " + seriesName + "(Ep. " + episodeNumber + ")");
        System.out.println("Host: " + host);
        System.out.println("Category: " + topicCategory);
        System.out.println("Genre: " + getGenre());
        System.out.println("Year: " + getPublicationYear());
        System.out.println("Duration: " + formatDuration());
    }

    @Override
    public String toString() {
        return String.format("[Podcast] Series: %s, Title: %s (E%d), Host: %s, Duration: %s",
                seriesName, getTitle(), episodeNumber, host, formatDuration());
    }
}
