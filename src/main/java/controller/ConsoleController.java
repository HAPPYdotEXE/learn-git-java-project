package controller;

import data.DataManager;
import model.*;
import view.ConsoleView;

import java.math.BigInteger;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ConsoleController {

    private final DataManager dataManager;
    private Set<Content> catalog;
    private final Scanner scan;
    private final ConsoleView view;

    public ConsoleController() {
        dataManager = new DataManager();
        catalog = new LinkedHashSet<>();
        scan = new Scanner(System.in);
        view = new ConsoleView();
    }

    public void start() {

        catalog = dataManager.load();
        System.out.println("Loaded " + catalog.size() + " items from storage.");

        while (true) {
            System.out.print("> ");
            String input = scan.nextLine().trim();
            if (input.isEmpty()) continue;

            // regex that allows multiple whitespaces
            String[] parts = input.split("\\s+", 2);
            String command = parts[0].toLowerCase();
            String args = parts.length > 1 ? parts[1] : "";

            try {
                switch (command) {
                    case "add" -> addContent(args);
                    case "delete", "remove" -> deleteContent(args);
                    case "search", "find" -> searchContent(args.toLowerCase());
                    case "playlist" -> handlePlaylist(args);
                    case "show", "info" -> showContentDetails(args);
                    case "list" -> printList(catalog);
                    case "filter" -> filterContent(args);
                    case "sort" -> sortCatalog(args);
                    case "save" -> dataManager.save(catalog);
                    case "load" -> {
                        catalog = dataManager.load();
                        System.out.println("Reloaded catalog: " + catalog.size() + " items.");
                    }
                    case "help" -> view.printHelp();
                    case "exit", "quit" -> {
                        System.out.println("Shutting down...");
                        return;
                    }
                    default -> System.out.println("Unknown command. Type 'help' for available commands.");
                }
            } catch (Exception e) {
                System.out.println("Error executing command: " + e.getMessage());
            }
        }
    }

    private void addContent(String type) {
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

    private void deleteContent(String title) {
        if (title.isEmpty()) { System.out.println("Usage: delete <title>"); return; }

        Content toRemove = resolveContent(title);
        if (toRemove == null) return;

        boolean removed = catalog.remove(toRemove);
        for (Content c : catalog) {
            if (c instanceof Playlist pl && pl.getItems().remove(toRemove)) {
                removed = true;
            }
        }
        Album inAlbum = catalog.stream()
                .filter(Album.class::isInstance)
                .map(Album.class::cast)
                .filter(alb -> alb.getItems().contains(toRemove))
                .findFirst()
                .orElse(null);

        if (removed) System.out.println("Removed '" + toRemove.getTitle() + "' from library/playlists.");

        if (inAlbum != null) {
            String msg = removed ? "Note: Item remains in an official Album - " + inAlbum.getTitle()+ " (immutable)."
                    : "Denied: '" + toRemove.getTitle() + "' only exists in an immutable Album - " + inAlbum.getTitle();
            System.out.println(msg);
        }
    }

    private void searchContent(String query) {
        if (query.isEmpty()) {
            System.out.println("Usage: search <query>");
            return;
        }
        List<Content> results = streamAll()
                .filter(c -> c.getTitle().toLowerCase().contains(query) ||
                        c.getAuthor().toLowerCase().contains(query))
                .toList();

        printList(results);
    }

    private void handlePlaylist(String action) {
        action = action.trim().toLowerCase();
        if (action.isEmpty() || !Set.of("create", "add", "remove", "sort").contains(action)) {
            System.out.println("Usage: playlist <create|add|remove|sort>");
            return;
        }

        try {
            if (action.equals("create")) {
                String name = prompt("Enter Playlist Title");
                System.out.println(catalog.add(new Playlist(name)) ? "Created." : "Error: Duplicate name.");
                return;
            }

            Playlist pl = findPlaylist(prompt("Playlist Name"));
            if (pl == null) {
                System.out.println("Playlist not found.");
                return;
            }
            switch (action) {
                case "add", "remove" -> {
                    Content c = resolveContent(prompt("Content Title"));
                    if (c == null) return;
                    if (action.equals("add")) pl.addContent(c);
                    else pl.removeContent(c);
                    System.out.println("Success.");
                }
                case "sort" -> {
                    String criteria = prompt("Sort by (title|author|year|default)").toLowerCase();
                    switch (criteria) {
                        case "title" -> pl.sortByTitle();
                        case "author" -> pl.sortByAuthor();
                        case "year" -> pl.sortByYear();
                        default -> pl.sortDefault();
                    }
                    view.displayInfo(pl);
                }
                default -> System.out.println("Unknown action: " + action);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void showContentDetails(String title) {
        Content c = resolveContent(title);
        if (c != null) {
            view.displayInfo(c); }
    }

    private void printList(Collection<Content> list) {
        System.out.println("--- Results (" + list.size() + ") ---");
        for (Content c : list) {
            System.out.println(c.toString());
        }
    }

    // using predicate in order to make the method more concise and shrink it down
    private void filterContent(String args) {
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
                case "year" -> c -> c.getPublicationYear() == Integer.parseInt(value);
                case "genre" -> c -> c.getGenre() == Genre.valueOf(value.toUpperCase());
                default -> null;
            };
        } catch (NumberFormatException e) {
            System.out.println("Invalid year. Please enter a number.");
            return;
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid genre. Available: " + java.util.Arrays.toString(Genre.values()));
            return;
        }
        if (criteria == null) {
            System.out.println("Unknown filter type.");
            return;
        }
        printList(streamAll().filter(criteria).toList());
    }

    private void sortCatalog(String args) {
        if (args.isEmpty()) {
            System.out.println("Usage: sort <type|title|author|year>");
            return;
        }
        List<Content> sortedList = new ArrayList<>(streamAll().toList());
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

    //Helper methods
    private void addSong() {
        Song song = new Song();
        System.out.println("--- Add Song ---");
        song.setTitle(prompt("Title"));
        song.setAuthor(prompt("Artist"));
        song.setPublicationYear(promptInt("Year"));
        song.setGenre(promptGenre());
        song.setDurationSeconds(promptDuration());
        song.setAlbumTitle(prompt("Album title"));
        catalog.add(song);
        System.out.println("Song added successfully.");
    }

    private void addPodcast() {
        Podcast podcast = new Podcast();
        System.out.println("--- Add Podcast ---");
        podcast.setTitle(prompt("Episode Title"));
        podcast.setAuthor(prompt("Host"));
        podcast.setPublicationYear(promptInt("Year"));
        podcast.setGenre(promptGenre());
        podcast.setDurationSeconds(promptDuration());
        podcast.setSeriesName(prompt("Series Name"));
        podcast.setEpisodeNumber(promptInt("Episode Number"));
        catalog.add(podcast);
        System.out.println("Podcast added successfully.");
    }

    private void addAudiobook() {
        Audiobook ab = new Audiobook();
        System.out.println("--- Add Audiobook ---");
        ab.setTitle(prompt("Title"));
        ab.setAuthor(prompt("Original Author"));
        ab.setPublicationYear(promptInt("Year"));
        ab.setGenre(promptGenre());
        ab.setDurationSeconds(promptDuration());
        ab.setNarrator(prompt("Narrator"));
        ab.setPublisher(prompt("Publisher"));
        ab.setInitialPublicationYear(promptInt("Initial Publication Year"));
        catalog.add(ab);
        System.out.println("Audiobook added successfully.");
    }

    private Content resolveContent(String title) {
        List<Content> matches = streamAll()
                .filter(c -> c.getTitle().equalsIgnoreCase(title))
                .toList();

        if (matches.isEmpty()) {
            System.out.println("Error: content not found."); // experimental print
            return null;
        } else if (matches.size() == 1) {
            return matches.get(0);
        }

        System.out.println("Multiple items found with that title. Please choose:");
        for (int i = 0; i < matches.size(); i++) {
            Content c = matches.get(i);
            System.out.printf(" [%d] %s\n", i + 1, c.toString());
        }

        while (true) {
            System.out.print("Select item number (1-" + matches.size() + "): ");
            try {
                int choice = Integer.parseInt(scan.nextLine().trim());
                if (choice >= 1 && choice <= matches.size()) {
                    return matches.get(choice - 1); // Convert 1-based index to 0-based
                }
            } catch (NumberFormatException ignored) {
                System.out.println("Invalid selection. Please try again.");
            }
        }
    }

    private String prompt(String label) {
        System.out.print(label + ": ");
        return scan.nextLine().trim();
    }

    private int promptInt(String label) {
        while (true) {
            System.out.print(label + ": ");
            String in = scan.nextLine().trim();
            try {
                return Integer.parseInt(in);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    private Genre promptGenre() {
        System.out.println("Available Genres: ");
        for (Genre g : Genre.values()) System.out.print(g + " ");
        System.out.println();
        System.out.print("Genre: ");
        String in = scan.nextLine().trim().toUpperCase();
        return Genre.valueOf(in);

    }

    private BigInteger promptDuration() {
        while (true) {
            System.out.print("Duration (seconds): ");
            String in = scan.nextLine().trim();
            try {
                long sec = Long.parseLong(in);
                return BigInteger.valueOf(sec).multiply(BigInteger.valueOf(1000));
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    private Playlist findPlaylist(String name) {
        return (Playlist) catalog.stream()
                .filter(c -> c instanceof Playlist && c.getTitle().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    private Stream<Content> streamAll() {
        return catalog.stream()
                .flatMap(content -> {
                    List<Content> expanded = new ArrayList<>();
                    expanded.add(content);

                    if (content instanceof AudioCollection pl) {
                        expanded.addAll(pl.getItems());
                    }
                    return expanded.stream();
                })
                .distinct();
    }

//     Flattens the catalog into a single stream containing all objects
//     and any nested content found within Playlists or Albums.
//     This allows search, sort, and filter operations to "see" items that
//     only exist inside a collection without duplicating them in the main set.
}
