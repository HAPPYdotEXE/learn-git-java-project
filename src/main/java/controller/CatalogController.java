package controller;

import model.*;
import view.ConsoleView;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CatalogController extends BaseController {

    public CatalogController(LibraryService library, InputHelper input, ConsoleView view) {
        super(library, input, view);
    }

    public void addContent(String type) {
        if (type.isEmpty()) {
            System.out.println("Usage: add <type> (song, podcast, audiobook)");
            return;
        }
        try {
            switch (type.toLowerCase()) {
                case "song" -> addSong();
                case "podcast" -> addPodcast();
                case "audiobook" -> addAudiobook();
                default -> System.out.println("Unknown type: " + type);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Creation failed: " + e.getMessage());
        }
    }

    // Original add methods using 'input' controller instead of local method calls
    private void addSong() {
        Song song = new Song();
        System.out.println("--- Add Song ---");
        song.setTitle(input.prompt("Title"));
        song.setAuthor(input.prompt("Artist"));
        song.setPublicationYear(input.promptInt("Year"));
        song.setGenre(input.promptGenre());
        song.setDurationSeconds(input.promptDuration());
        song.setAlbumTitle(input.prompt("Album title"));
        library.getCatalog().add(song);
        System.out.println("Song added successfully.");
    }

    private void addPodcast() {
        Podcast podcast = new Podcast();
        System.out.println("--- Add Podcast ---");
        podcast.setTitle(input.prompt("Episode Title"));
        podcast.setAuthor(input.prompt("Host"));
        podcast.setPublicationYear(input.promptInt("Year"));
        podcast.setGenre(input.promptGenre());
        podcast.setDurationSeconds(input.promptDuration());
        podcast.setSeriesName(input.prompt("Series Name"));
        podcast.setEpisodeNumber(input.promptInt("Episode Number"));
        library.getCatalog().add(podcast);
        System.out.println("Podcast added successfully.");
    }

    private void addAudiobook() {
        Audiobook ab = new Audiobook();
        System.out.println("--- Add Audiobook ---");
        ab.setTitle(input.prompt("Title"));
        ab.setAuthor(input.prompt("Original Author"));
        ab.setPublicationYear(input.promptInt("Year"));
        ab.setGenre(input.promptGenre());
        ab.setDurationSeconds(input.promptDuration());
        ab.setNarrator(input.prompt("Narrator"));
        ab.setPublisher(input.prompt("Publisher"));
        ab.setInitialPublicationYear(input.promptInt("Initial Publication Year"));
        library.getCatalog().add(ab);
        System.out.println("Audiobook added successfully.");
    }

    public void deleteContent(String title) {
        if (title.isEmpty()) { System.out.println("Usage: delete <title>"); return; }

        Content toRemove = resolveContent(title);
        if (toRemove == null) return;

        Album inAlbum = null;
        boolean removed = library.getCatalog().remove(toRemove);
        for (Content c : library.getCatalog()) {
            if (c instanceof Playlist pl && pl.getItems().remove(toRemove)) {
                removed = true;
            }
            if (c instanceof Album alb && alb.getItems().contains(toRemove)){
                inAlbum = alb;
            }
        }

        if (removed) System.out.println("Removed '" + toRemove.getTitle() + "' from library/playlists.");

        if (inAlbum != null) {
            String msg = removed ? "Note: Item remains in an official Album - " + inAlbum.getTitle()+ " (immutable)."
                    : "Denied: '" + toRemove.getTitle() + "' only exists in an immutable Album - " + inAlbum.getTitle();
            System.out.println(msg);
        }
    }

    public void searchContent(String query) {
        if (query.isEmpty()) {
            System.out.println("Usage: search <query>");
            return;
        }
        List<Content> results = library.streamAll()
                .filter(c -> c.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                        c.getAuthor().toLowerCase().contains(query.toLowerCase()))
                .toList();

        printList(results);
    }

    public void showContentDetails(String title) {
        Content c = resolveContent(title);
        if (c != null) {
            view.displayInfo(c);
        }
    }

    // using predicate in order to make the method more concise and shrink it down
    public void filterContent(String args) {
        String[] parts = args.split("\\s+", 2);
        if (parts.length < 2) {
            System.out.println("Usage: filter <type|genre|author|year> <value>");
            return;
        }
        String type = parts[0].toLowerCase();
        String value = parts[1];

        Predicate<Content> criteria;
        try {
            criteria = switch (type) {
                case "type", "category" -> c -> c.getClass().getSimpleName().equalsIgnoreCase(value);
                case "author", "artist" -> c -> c.getAuthor().toLowerCase().contains(value.toLowerCase());
                case "year" -> {
                    int year = Integer.parseInt(value); // parsing into a variable so the catch block would detect invalid input
                    yield c -> c.getPublicationYear() == year;
                }
                case "genre" -> {
                    Genre genre = Genre.valueOf(value.toUpperCase());
                    yield c -> c.getGenre() == genre;
                }
                default -> null;
            };

            if (criteria == null) {
                System.out.println("Unknown filter type.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid year. Please enter a number.");
            return;
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid genre. Available: \n" + java.util.Arrays.toString(Genre.values()));
            return;
        }

        printList(library.streamAll().filter(criteria).toList());
    }

    public void sortCatalog(String args) {
        if (args.isEmpty()) {
            System.out.println("Usage: sort <type|title|author|year>");
            return;
        }
        List<Content> sortedList = new ArrayList<>(library.streamAll().toList());
        switch (args.toLowerCase()) {
            case "type" -> sortedList.sort(Content.BY_TYPE);
            case "title" -> sortedList.sort(Content.BY_TITLE);
            case "author", "artist" -> sortedList.sort(Content.BY_AUTHOR);
            case "year" -> sortedList.sort(Content.BY_YEAR);
            default -> {
                System.out.println("Unknown sort key.");
                return;
            }
        }
        System.out.println("Catalog sorted by " + args);
        printList(sortedList);
    }

    public void printCatalog() {
        printList(library.getCatalog());
    }
}