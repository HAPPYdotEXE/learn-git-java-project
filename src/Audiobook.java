public class Audiobook extends Content{

    private String narrator;
    private String publisher;

    public Audiobook(){}

    public Audiobook(String title, String author, int publicationYear, Genre genre,
                     long durationSeconds, String narrator, String publisher){
        super(title, author, publicationYear, genre, durationSeconds);
        setNarrator(narrator);
        setPublisher(publisher);
    }

    public String getNarrator() {
        return narrator;
    }
    public String getPublisher(){
        return publisher;
    }
    public void setNarrator(String narrator){
        if (narrator == null) {
            throw new IllegalArgumentException("Narrator must not be null");
        }
        narrator = narrator.trim();
        if (narrator.isBlank()) {
            throw new IllegalArgumentException("Narrator must not be blank");
        }
        if (narrator.length() >= 150) {
            throw new IllegalArgumentException("Narrator length must be less than or equal to 150");
        }
        this.narrator = narrator;
    }
    public void setPublisher(String publisher){
        if (publisher == null) {
            throw new IllegalArgumentException("Publisher must not be null");
        }
        publisher = publisher.trim();
        if (publisher.isBlank()) {
            throw new IllegalArgumentException("Publisher must not be blank");
        }
        if (publisher.length() >= 150) {
            throw new IllegalArgumentException("Publisher length must be less than or equal to 150");
        }
        this.publisher = publisher;
    }

    @Override
    public void displayInfo(){
        System.out.println("--- Audiobook: " + getTitle() + " ---");
        System.out.println("Original author: " + getAuthor());
        System.out.println("Narrator: " + narrator);
        System.out.println("Publisher: " + publisher);
        System.out.println("Genre: " + getGenre());
        System.out.println("Year: " + getPublicationYear()); // publicationYear conflict with Song year restriction
        System.out.println("Duration: " + formatDuration());
    }

    @Override
    public String toString() {
        return String.format("[Audiobook] Title: %s, Original Author: %s, Narrator: %s, Duration: %s",
                getTitle(), getAuthor(), narrator, formatDuration());
    }
}
