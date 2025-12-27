package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import data.DataManager;
import model.*;
import view.ConsoleView;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConsoleController {
    private final DataManager dataManager;
    private List<Content> catalog;
    private List<Content> allContent;
    private final Scanner scanner;
    private final ObjectMapper mapper;
    private final ConsoleView view;

    public ConsoleController() {
        this.dataManager = new DataManager();
        this.catalog = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.mapper = new ObjectMapper();
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.view = new ConsoleView();
    }

    public void start() {
        System.out.println("Loading application..");
        System.out.println("Type 'help' for a list of commands.");

        catalog = dataManager.load();
        refreshCache();
        allContent = catalog.stream()
                .flatMap(c -> {
                    if (c instanceof AudioCollection) {
                        // Return a stream containing the Collection object ITSELF + its elements as a standalone objects
                        return Stream.concat(
                                Stream.of(c),
                                ((AudioCollection) c).getItems().stream()
                        );
                    } else { return Stream.of(c);  }})
                .distinct()
                .collect(Collectors.toList());
        // collect(Collectors.toList()) is used to return mutable list / .toList() for immutable


        System.out.println("Loaded " + catalog.size() + " items from storage.");

        boolean running = true;
        while (running) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            String[] parts = input.split("\\s+", 2); // (\s+) - regex that allows many whitespaces between the two parts
            String command = parts[0].toLowerCase();
            String args = parts.length > 1 ? parts[1] : "";

            try {
                switch (command) {
                    case "add" -> handleAdd(args);
                    case "delete", "remove" -> handleDelete(args);
                    case "search", "find" -> handleSearch(args);
                    case "playlist" -> handlePlaylist(args);
                    case "show", "info" -> handleShow(args);
                    case "list" -> handleList();
                    case "filter" -> handleFilter(args);
                    case "sort" -> handleSort(args);
                    case "save" -> dataManager.save(catalog);
                    case "load" -> {
                        catalog = dataManager.load();
                        System.out.println("Reloaded catalog: " + catalog.size() + " items.");
                    }
                    case "help" -> printHelp();
                    case "exit", "quit" -> running = false;
                    default -> System.out.println("Unknown command. Type 'help' for available commands.");
                }
            } catch (Exception e) {
                System.out.println("Error executing command: " + e.getMessage());
            }
        }
        System.out.println("Shutting down...");
    }

    private void printHelp() {
        System.out.println("""
                Available Commands:
                  add <type>                  - Add a new item (song, podcast, audiobook, playlist)
                  delete <title>              - Delete an item by title
                  search <query>              - Search by title, author, or genre
                  playlist create <title>     - Create a new empty playlist
                  playlist add <pl> <item>    - Add an item to a playlist
                  playlist remove <pl> <item> - Remove an item from a playlist
                  playlist sort <pl> <mode>   - Sort playlist (title, author, year)
                  show <title>                - Show details of an item
                  list                        - List all items
                  filter <type> <value>       - Filter by genre, author, year (e.g., 'filter genre ROCK')
                  sort [title|author|year]    - Sort the catalog temporarily
                  save                        - Save catalog to file
                  load                        - Reload catalog from file
                  exit                        - Exit the application
                """);
    }

    private void addSong() {
        Song song = new Song();
        System.out.println("--- Add core.Song ---");
        song.setTitle(prompt("Title"));
        song.setAuthor(prompt("Artist"));
        song.setPublicationYear(promptInt("Year"));
        song.setGenre(promptGenre());
        song.setDurationMilliseconds(promptDuration());
        song.setAlbumTitle(prompt("core.Album title"));
        catalog.add(song);
        System.out.println("core.Song added successfully.");
    }

    private void addPodcast() {
        Podcast podcast = new Podcast();
        System.out.println("--- Add core.Podcast ---");
        podcast.setTitle(prompt("Episode Title"));
        podcast.setAuthor(prompt("Host"));
        podcast.setPublicationYear(promptInt("Year"));
        podcast.setGenre(promptGenre());
        podcast.setDurationMilliseconds(promptDuration());
        podcast.setSeriesName(prompt("Series Name"));
        podcast.setEpisodeNumber(promptInt("Episode Number"));
        catalog.add(podcast);
        System.out.println("core.Podcast added successfully.");
    }

    private void addAudiobook() {
        Audiobook ab = new Audiobook();
        System.out.println("--- Add core.Audiobook ---");
        ab.setTitle(prompt("Title"));
        ab.setAuthor(prompt("Original Author"));
        ab.setPublicationYear(promptInt("Year"));
        ab.setGenre(promptGenre());
        ab.setDurationMilliseconds(promptDuration());
        ab.setNarrator(prompt("Narrator"));
        ab.setPublisher(prompt("Publisher"));
        ab.setInitialPublicationYear(promptInt("Initial Publication Year"));
        catalog.add(ab);
        System.out.println("core.Audiobook added successfully.");
    }

    private void handleAdd(String type) {
        if (type.isEmpty()) {
            System.out.println("Usage: add <type> (song, podcast, audiobook)");
            return;
        }

        try {
            switch (type.toLowerCase()) {
                case "song" -> addSong();
                case "podcast" -> addPodcast();
                case "audiobook" -> addAudiobook();
                case "playlist" -> {
                    System.out.print("Enter core.Playlist Title: ");
                    String title = scanner.nextLine();
                    catalog.add(new Playlist(title));
                    System.out.println("core.Playlist created.");
                }
                default -> System.out.println("Unknown type: " + type);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Creation failed: " + e.getMessage());
        }
        refreshCache();
    }

    private void handleDelete(String title) {
        if (title.isEmpty()) {
            System.out.println("Usage: delete <title>");
            return;
        }
        Content toRemove = resolveContent(title);
        if (toRemove != null) {
            catalog.remove(toRemove);
            System.out.println("Item '" + title + "' removed.");
        } else {
            System.out.println("Item not found.");
        }
        refreshCache();
    }

    private void handleSearch(String query) {
        if (query.isEmpty()) {
            System.out.println("Usage: search <query>");
            return;
        }
        String q = query.toLowerCase();
        List<Content> results = allContent.stream()
                .filter(c -> c.getTitle().toLowerCase().contains(q) ||
                        c.getAuthor().toLowerCase().contains(q))
                .toList();

        printList(results);
    }

    private void handlePlaylist(String args) {
        String[] parts = args.split("\\s+", 2);
        if (parts.length < 1) {
            System.out.println("Usage: playlist <create|add|remove|sort> ...");
            return;
        }
        String action = parts[0].toLowerCase();
        String params = parts.length > 1 ? parts[1] : "";

        try {
            switch (action) {
                case "create" -> {
                    if (params.isEmpty()) params = prompt("Enter core.Playlist Title");
                    catalog.add(new Playlist(params));
                    System.out.println("core.Playlist '" + params + "' created.");
                }
                case "add", "remove" -> {
                    Playlist pl = findPlaylist(prompt("core.Playlist Name"));
                    Content c = resolveContent(prompt("core.Content Title"));

                    if (pl == null) {
                        System.out.println("core.Playlist not found.");
                        return;
                    }
                    if (c == null) {
                        System.out.println("core.Content not found.");
                        return;
                    }

                    if (action.equals("add")) {
                        pl.addContent(c);
                        System.out.println("Added.");
                    } else {
                        pl.removeContent(c);
                        System.out.println("Removed.");
                    }
                }
                case "sort" -> {
                    String plSortName = prompt("core.Playlist Name");
                    Playlist plSort = findPlaylist(plSortName);
                    if (plSort == null) {
                        System.out.println("core.Playlist not found.");
                        return;
                    }
                    String mode = prompt("Sort by (title/author/year/default)");
                    switch (mode.toLowerCase()) {
                        case "title" -> plSort.sortByTitle();
                        case "author" -> plSort.sortByAuthor();
                        case "year" -> plSort.sortByYear();
                        default -> plSort.sortDefault();
                    }
                }
                default -> System.out.println("Unknown playlist command.");
            }
        } catch (Exception e) {
            System.out.println("Error in playlist operation: " + e.getMessage());
        }
    }

    private Playlist findPlaylist(String name) {
        return (Playlist) catalog.stream()
                .filter(c -> c instanceof Playlist && c.getTitle().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    private void handleShow(String title) {
        Content c = resolveContent(title);
        if (c != null) {
            view.displayInfo(c);
        } else {
            System.out.println("Item not found.");
        }
    }

    private void handleList() {
        if (catalog.isEmpty()) {
            System.out.println("Catalog is empty.");
        } else {
            printList(catalog);
        }
    }

    private void handleFilter(String args) {
        String[] parts = args.split("\\s+", 2);
        if (parts.length < 2) {
            System.out.println("Usage: filter <type|genre|author|year> <value>");
            return;
        }
        String type = parts[0].toLowerCase();
        String value = parts[1];

        List<Content> filtered = new ArrayList<>();
        switch (type) {
            case "type", "category" -> {
                filtered = allContent.stream()
                        .filter(c -> c.getClass().getSimpleName().equalsIgnoreCase(value))
                        .toList();
            }
            case "genre" -> {
                try {
                    String genreStr = value.toUpperCase().replace(" ", "_");
                    Genre g = Genre.valueOf(genreStr);
                    filtered = allContent.stream().filter(c -> c.getGenre() == g).toList();
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid genre. Available: ");
                    for (Genre g : Genre.values()) System.out.print(g + " ");
                    System.out.println();
                    return;
                }
            }
            case "author", "artist" -> filtered = allContent.stream()
                    .filter(c -> c.getAuthor().toLowerCase().contains(value.toLowerCase()))
                    .toList();
            case "year" -> {
                try {
                    int y = Integer.parseInt(value);
                    filtered = allContent.stream().filter(c -> c.getPublicationYear() == y).toList();
                } catch (NumberFormatException e) {
                    System.out.println("Invalid year.");
                    return;
                }
            }
            default -> {
                System.out.println("Unknown filter type.");
                return;
            }
        }
        printList(filtered);
    }

    private void handleSort(String args) {
        if (args.isEmpty()) {
            System.out.println("Usage: sort <type|title|author|year>");
            return;
        }

        switch (args.toLowerCase()) {
            case "type" -> catalog.sort(Content.BY_TYPE);
            case "title" -> catalog.sort(Content.BY_TITLE);
            case "author", "artist" -> catalog.sort(Content.BY_AUTHOR);
            case "year" -> catalog.sort(Content.BY_YEAR);
            default -> {
                System.out.println("Unknown sort key.");
                return;
            }
        }
        System.out.println("Catalog sorted by " + args);
        printList(catalog);
    }

    private Content resolveContent(String title) {
        List<Content> matches = allContent.stream()
                .filter(c -> c.getTitle().equalsIgnoreCase(title))
                .toList();

        if (matches.isEmpty()) {
            return null;
        }

        if (matches.size() == 1) {
            return matches.get(0);
        }

        System.out.println("Found " + matches.size() + " items with the title '" + title + "'. Please choose one:");

        for (int i = 0; i < matches.size(); i++) {
            Content c = matches.get(i);
            System.out.printf(" [%d] %s\n", i + 1, c.toString());
        }

        while (true) {
            System.out.print("Select item number (1-" + matches.size() + "): ");
            try {
                String input = scanner.nextLine().trim();
                int choice = Integer.parseInt(input);

                if (choice >= 1 && choice <= matches.size()) {
                    return matches.get(choice - 1); // Convert 1-based index to 0-based
                }
            } catch (NumberFormatException ignored) {
                continue;
            }
            System.out.println("Invalid selection. Please try again.");
        }
    }

    private void printList(List<Content> list) {
        System.out.println("--- Results (" + list.size() + ") ---");
        for (Content c : list) {
            System.out.println(c.toString());
        }
    }

    private String prompt(String label) {
        System.out.print(label + ": ");
        return scanner.nextLine().trim();
    }

    private int promptInt(String label) {
        while (true) {
            System.out.print(label + ": ");
            String in = scanner.nextLine().trim();
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
        while (true) {
            System.out.print("core.Genre: ");
            String in = scanner.nextLine().trim().toUpperCase();
            try {
                return Genre.valueOf(in);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid genre. Try again.");
            }
        }
    }

    private BigInteger promptDuration() {
        while (true) {
            System.out.print("Duration (seconds): ");
            String in = scanner.nextLine().trim();
            try {
                long sec = Long.parseLong(in);
                return BigInteger.valueOf(sec).multiply(BigInteger.valueOf(1000));
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    private void refreshCache() {
        allContent = catalog.stream()
                .flatMap(c -> {
                    if (c instanceof AudioCollection) {
                        return Stream.concat(Stream.of(c), ((AudioCollection) c).getItems().stream());
                    }
                    return Stream.of(c);
                })
                .distinct()
                .toList();
    }
}