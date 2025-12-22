public class Song extends Content{

    private String albumTitle;

    public Song(){}

    public Song(String title, String author, int publicationYear, Genre genre, long durationSeconds, String albumTitle){
        super(title, author, publicationYear, genre, durationSeconds);
        setAlbumTitle(albumTitle);
    }

    public String getAlbumTitle(){
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle){
        if (albumTitle == null) {
            throw new IllegalArgumentException("Album title must not be null");
        }
        albumTitle = albumTitle.trim();
        if (albumTitle.isBlank()) {
            throw new IllegalArgumentException("Album title must not be blank");
        }
        if (albumTitle.length() >= 150) {
            throw new IllegalArgumentException("Album title length must be less than or equal to 150");
        }
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
