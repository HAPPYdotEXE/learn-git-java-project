package controller;

import model.Content;
import model.Playlist;
import view.ConsoleView;
import java.util.Set;

public class PlaylistController extends BaseController {

    public PlaylistController(LibraryService context, InputHelper input, ConsoleView view) {
        super(context, input, view);
    }

    public void handlePlaylist(String action) {
        action = action.trim().toLowerCase();
        if (action.isEmpty() || !Set.of("create", "add", "remove", "sort").contains(action)) {
            System.out.println("Usage: playlist <create|add|remove|sort>");
            return;
        }

        try {
            if (action.equals("create")) {
                String name = input.prompt("Enter Playlist Title");
                System.out.println(library.getCatalog().add(new Playlist(name)) ? "Created." : "Error: Duplicate name.");
                return;
            }

            Playlist pl = findPlaylist(input.prompt("Playlist Name"));
            if (pl == null) {
                System.out.println("Playlist not found.");
                return;
            }
            switch (action) {
                case "add", "remove" -> {
                    Content c = resolveContent(input.prompt("Content Title"));
                    if (c == null) return;
                    if (action.equals("add")) pl.addContent(c);
                    else pl.removeContent(c);
                    System.out.println("Success.");
                }
                case "sort" -> {
                    String criteria = input.prompt("Sort by (title|author|year|default)").toLowerCase();
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

    private Playlist findPlaylist(String name) {
        return (Playlist) library.getCatalog().stream()
                .filter(c -> c instanceof Playlist && c.getTitle().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}