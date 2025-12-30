package controller;

import view.ConsoleView;

public class MainController {

    private final LibraryService library;
    private final InputHelper input;
    private final ConsoleView view;

    private final CatalogController catalogController;
    private final PlaylistController playlistController;

    public MainController() {
        this.library = new LibraryService();
        this.input = new InputHelper();
        this.view = new ConsoleView();

        this.catalogController = new CatalogController(library, input, view);
        this.playlistController = new PlaylistController(library, input, view);
    }

    public void start() {
        library.load();
        System.out.println("Loaded " + library.getCatalog().size() + " items from storage.");

        while (true) {
            System.out.print("> ");
            String raw = input.readLine();
            if (raw.isEmpty()) continue;

            // regex that allows multiple whitespaces
            String[] parts = raw.split("\\s+", 2);
            String command = parts[0].toLowerCase();
            String args = parts.length > 1 ? parts[1] : "";

            try {
                switch (command) {
                    case "add" -> catalogController.addContent(args);
                    case "delete", "remove" -> catalogController.deleteContent(args);
                    case "search", "find" -> catalogController.searchContent(args);
                    case "playlist" -> playlistController.handlePlaylist(args);
                    case "show", "info" -> catalogController.showContentDetails(args);
                    case "list" -> catalogController.printCatalog();
                    case "filter" -> catalogController.filterContent(args);
                    case "sort" -> catalogController.sortCatalog(args);
                    case "save" -> library.save();
                    case "load" -> {
                        library.load();
                        System.out.println("Reloaded catalog: " + library.getCatalog().size() + " items.");
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
}