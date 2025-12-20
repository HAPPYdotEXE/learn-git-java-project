public class Song extends Content{

    private String albumTitle;

    public Song(){}

    public Song(String title, String author, int publicationYear, String genre, long durationSeconds, String albumTitle){
        super(title, author, publicationYear, genre, durationSeconds);
        this.albumTitle = albumTitle;
    }

    public String getAlbumTitle(){
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle){
        this.albumTitle = albumTitle;
    }

    @Override
    public void displayInfo(){
        System.out.println("--- Song: " + getTitle() + " ---");
        System.out.println("Author: " + getAuthor());
        System.out.println("Genre: " + getGenre());
        System.out.println("Year: " + getPublicationYear());
        System.out.println("Duration: " + formatDuration());
    }

    @Override
    public String toString(){
        return String.format("[Song] Title: %s, Artist: %s, Album: %s, Duration: %s",
                getTitle(), getAuthor(), albumTitle, formatDuration());
    }



}
