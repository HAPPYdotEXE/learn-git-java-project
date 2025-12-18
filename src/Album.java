import java.util.LinkedHashSet;
import java.util.Set;

public class Album extends Content{


    private Set<Song> songs;

    public Album(String title, String artist, int publicationYear, String genre){
        super(title, artist, publicationYear, genre, 0);
            this.songs = new LinkedHashSet<>();
    }

    public boolean addSong(Song song){
        if(!song.getAuthor().equals(this.getAuthor())){
            System.out.println("Error: Song (" + song.getTitle() +") does not match album artist");
            return false;
        }
        else if (song.getPublicationYear() != this.getPublicationYear()){
            System.out.println("Error: Song (" + song.getTitle() +") does not match album release year");
            return false;
        }
        if (songs.add(song)){
            song.setAlbumTitle(this.getTitle());
            return true;
        } else {
            System.out.println("Error: Song (" + song.getTitle() +") is already in the album");
            return false;
        }
    }

    public boolean removeSong(Song song){
        return songs.remove(song);
    }
    public Set<Song> getSongs(){
        return songs;
    }

    @Override
    public long getDurationSeconds(){
//        long duration = 0;
//        for(Song c : songs) { duration += c.getDurationSeconds(); }
//        return duration;

        return songs.stream()
                .mapToLong(s-> s.getDurationSeconds())
                .sum();
    }

    @Override
    public void displayInfo(){
        System.out.println("\n*** Album: " + getTitle() + " ***");
        System.out.println("Arist: " + getAuthor());
        System.out.println("Year: " + getPublicationYear());
        System.out.println("Genre: " + getGenre());
        System.out.println("Entire duration: " + formatDuration());
        System.out.println("Number of songs: " + songs.size());

        System.out.println("--- List of songs ---");
        int index = 1;
        for(Song s : songs) {
            System.out.printf("%d. %s - (%s)\n", index++, s.getTitle(), s.formatDuration());
        }
    }

}



