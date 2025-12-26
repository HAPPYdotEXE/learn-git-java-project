import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- Starting Simple Test ---");

        DataManager dataManager = new DataManager();
        List<Content> masterList = new ArrayList<>();


        System.out.println("------LOADING TO FILE --------");
        masterList = dataManager.load();
        System.out.println("Master Catalog Loaded: " + masterList.size() + " total items.");


        List<Album> allAlbums = masterList.stream()
                        .filter(c -> c instanceof Album)
                                .map(c -> (Album) c)
                                        .toList();

        List<Playlist> allPlaylists = masterList.stream()
                        .filter(c -> c instanceof Playlist)
                                .map(c -> (Playlist) c)
                                        .toList();
        List<Content> allContent = getAllIndividualItems(masterList);

        System.out.println("Separated into: " + allAlbums.size() + " Albums and " + allPlaylists.size() + " Playlists");

        if (allPlaylists.isEmpty()) {
            System.out.println("No playlists found to test.");
        } else {
            for (Playlist pl : allPlaylists) {
                System.out.println("=== Testing Playlist: " + pl.getTitle() + " ===");

                // Print original state (as loaded from JSON)
                System.out.print("--- Original State ---");
                pl.displayInfo();

                // Sort by Title (Alphabetical)
                System.out.println("\n--- Sorting by Title ---");
                pl.sortByTitle();
                pl.displayInfo();

                // Sort by Publication Year
                System.out.println("\n--- Sorting by Publication Year ---");
                pl.sortByYear();
                pl.displayInfo();
            }
        }

        System.out.println("------SAVING TO FILE --------");
        dataManager.save(masterList);




    }

    public static List<Content> getAllIndividualItems(List<Content> masterList) {
        List<Content> flatList = new ArrayList<>();

        for (Content item : masterList) {
            if (item instanceof AudioCollection) {
                // If it's an Album or Playlist, add the items inside it
                flatList.addAll(((AudioCollection) item).getItems());
            } else {
                // If it's a standalone Song, Podcast, or Audiobook, add it directly
                flatList.add(item);
            }
        }
        // Remove duplicates if the same song is in multiple playlists
        return flatList.stream().distinct().collect(Collectors.toList());
    }
}
