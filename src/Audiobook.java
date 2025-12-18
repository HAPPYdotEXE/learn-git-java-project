public class Audiobook extends Content{

    private String narrator;
    private String originalAuthor;
    private String seriesName;

    public Audiobook(String title, String publisher, int publicationYear, String genre,
                     long durationSeconds, String narrator, String originalAuthor, String seriesName){

        super(title, publisher, publicationYear, genre, durationSeconds);
        this.narrator = narrator;
        this.originalAuthor = originalAuthor;
        this.seriesName = seriesName;
    }

    public String getNarrator() {
        return narrator;
    }

    public String getOriginalAuthor() {
        return originalAuthor;
    }

    public String getSeriesName() {
        return seriesName;
    }

    @Override
    public void displayInfo(){
        System.out.println("--- Audiobook: " + getTitle() + " ---");
        System.out.println("Original author: " + originalAuthor);
        System.out.println("Narrator: " + narrator);
        System.out.println("Publisher: " + getAuthor());

        if (seriesName != null) {
            System.out.println("Series: " + seriesName);
        }
        System.out.println("Genre: " + getGenre());
        System.out.println("Year: " + getPublicationYear());
        System.out.println("Duration: " + formatDuration());
    }

    @Override
    public String toString() {
        return String.format("[Audiobook] Title: %s, Original Author: %s, Narrator: %s, Duration: %s",
                getTitle(), originalAuthor, narrator, formatDuration());
    }
}
