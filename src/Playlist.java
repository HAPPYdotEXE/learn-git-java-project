import java.util.*;

public class Playlist {

    private String title;
    private List<Content> contents;

    public Playlist(String title) {
        this.title = title;
        this.contents = new ArrayList<>();
        // hashset vs arrayList
    }


    public boolean addContent(Content content) {
        if (content instanceof Album) {
            System.out.println("Error: Cannot add albums to playlist!");
            return false;
        }
        if (!contents.add(content)){
            System.out.println("Error:" + content.getClass().getSimpleName() + " (" + content.getTitle() +") is already in the playlist");
            return false;
        }
        return true;
    }

    public boolean removeContent(Content content){
        return contents.remove(content);
    }

    public long getTotalDurationSeconds(){
        return contents.stream()
                .mapToLong(s-> s.getDurationSeconds())
                .sum();
    }

    public String getFormatDuration(){
        long durationSeconds = getTotalDurationSeconds();
        long minutes = durationSeconds / 60;
        long seconds = durationSeconds % 60;
        return String.format("%d min. %02d sec.", minutes, seconds);
    }

    public String getTitle(){
        return title;
    }
    public List<Content> getContents(){
        return contents;
    }

    public void displayPlaylistInfo(){
        System.out.println("\n--- Playlist: " + title + " ---");
        System.out.println("Elements: " + contents.size());
        System.out.println("Duration: " + getFormatDuration());

        if (contents.isEmpty()){
            System.out.println("The playlist is empty!");
            return;
        }

        System.out.println("\n--- Content ---");
        int index = 1;
        for(Content c : contents){
            System.out.printf("%d. %s\n", index++, c.toString());
        }
    }

    public void sortByTitle(){
        contents.sort(Comparator.comparing(s-> s.getTitle()));
//        contents.sort(Content.BY_TITLE);
        System.out.println("Playlist \"" + title + "\" is sorted by title.");
    }

    public void sortByAuthor(){
        contents.sort(Comparator.comparing(s-> s.getAuthor()));
//        contents.sort(Content.BY_AUTHOR);
        System.out.println("Playlist \"" + title + "\" is sorted by author.");

    }

    public void sortByPublicationYear(){
        contents.sort(Comparator.comparing(s-> s.getPublicationYear()));
//        contents.sort(Content.BY_YEAR);
        System.out.println("Playlist \"" + title + "\" is sorted by publication year.");
    }

    public void sortDefault(){
        Collections.sort(contents);
    }





}
