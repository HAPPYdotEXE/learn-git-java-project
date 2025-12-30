package controller;

import model.Content;
import view.ConsoleView;
import java.util.Collection;
import java.util.List;

public abstract class BaseController {
    protected final LibraryService library;
    protected final InputHelper input;
    protected final ConsoleView view;

    public BaseController(LibraryService library, InputHelper input, ConsoleView view) {
        this.library = library;
        this.input = input;
        this.view = view;
    }

    //     Flattens the catalog into a single stream containing all objects
    //     and any nested content found within Playlists or Albums.
    //     This allows search, sort, and filter operations to "see" items that
    //     only exist inside a collection without duplicating them in the main set.
    protected Content resolveContent(String title) {
        List<Content> matches = library.streamAll()
                .filter(c -> c.getTitle().equalsIgnoreCase(title))
                .toList();

        if (matches.isEmpty()) {
            System.out.println("Error: content not found.");
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
            int choice = input.promptInt("Select item number (1-" + matches.size() + ")");
            if (choice >= 1 && choice <= matches.size()) {
                return matches.get(choice - 1);
            }
        }
    }

    protected void printList(Collection<Content> list) {
        System.out.println("--- Results (" + list.size() + ") ---");
        for (Content c : list) {
            System.out.println(c.toString());
        }
    }
}