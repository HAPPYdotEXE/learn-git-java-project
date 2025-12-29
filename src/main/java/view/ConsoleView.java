package view;

import model.*;

public class ConsoleView {

    public void printHelp() {
        System.out.println("""
                Available Commands:
                  add <type>                  - Add a new item (song, podcast, audiobook)
                  delete <title>              - Delete an item by title
                  search <query>              - Search by title, author, or genre
                  playlist create             - Create a new empty playlist
                  playlist add                - Add an item to a playlist
                  playlist remove             - Remove an item from a playlist
                  playlist sort               - Sort playlist (title, author, year)
                  show <title>                - Show details of an item
                  list                        - List all items
                  filter <type> <value>       - Filter by genre, author, year (e.g., 'filter genre ROCK')
                  sort [title|author|year]    - Print sorted catalog
                  save                        - Save catalog to file
                  load                        - Reload catalog from file
                  exit                        - Exit the application
                """);
    }

    public void displayInfo(Content content) {
        if (content instanceof Song song) {
            displaySong(song);
        } else if (content instanceof Podcast podcast) {
            displayPodcast(podcast);
        } else if (content instanceof Audiobook audiobook) {
            displayAudiobook(audiobook);
        } else if (content instanceof Album album) {
            displayAlbum(album);
        } else if (content instanceof Playlist playlist) {
            displayPlaylist(playlist);
        } else {
            System.out.println("--- Content: " + content.getTitle() + " ---");
            System.out.println(content.toString());
        }
    }

    private void displaySong(Song song) {
        System.out.println("--- Song: " + song.getTitle() + " ---");
        System.out.println("Author: " + song.getAuthor());
        System.out.println("Genre: " + song.getGenre());
        System.out.println("Year: " + song.getPublicationYear());
        System.out.println("Duration: " + song.formatDuration());
    }

    private void displayPodcast(Podcast podcast) {
        System.out.println("--- Podcast Episode: " + podcast.getTitle() + " ---");
        System.out.println("Series: " + podcast.getSeriesName() + "(Ep. " + podcast.getEpisodeNumber() + ")");
        System.out.println("Host: " + podcast.getAuthor());
        System.out.println("Genre: " + podcast.getGenre());
        System.out.println("Year: " + podcast.getPublicationYear());
        System.out.println("Duration: " + podcast.formatDuration());
    }

    private void displayAudiobook(Audiobook audiobook) {
        System.out.println("--- Audiobook: " + audiobook.getTitle() + " ---");
        System.out.println("Original author: " + audiobook.getAuthor());
        System.out.println("Narrator: " + audiobook.getNarrator());
        System.out.println("Initial publication year: " + audiobook.getInitialPublicationYear());
        System.out.println("Publisher: " + audiobook.getPublisher());
        System.out.println("Genre: " + audiobook.getGenre());
        System.out.println("Year: " + audiobook.getPublicationYear());
        System.out.println("Duration: " + audiobook.formatDuration());
    }

    private void displayAlbum(Album album) {
        System.out.println("\n*** Album: " + album.getTitle() + " ***");
        System.out.println("Arist: " + album.getAuthor());
        System.out.println("Year: " + album.getPublicationYear());
        System.out.println("Genre: " + album.getGenre());
        System.out.println("Entire duration: " + album.getFormatDuration());
        System.out.println("Number of songs: " + album.getItems().size());

        System.out.println("--- List of songs ---");
        int index = 1;
        for (Content c : album.getItems()) {
            System.out.printf("%d. %s - (%s)\n", index++, c.getTitle(), c.formatDuration());
        }
    }

    private void displayPlaylist(Playlist playlist) {
        System.out.println("\n--- Playlist: " + playlist.getTitle() + " ---");
        System.out.println("Elements: " + playlist.getItems().size());
        System.out.println("Duration: " + playlist.getFormatDuration());

        if (playlist.getItems().isEmpty()){
            System.out.println("The playlist is empty!");
            return;
        }

        System.out.println("--- Content ---");
        int index = 1;
        for(Content c : playlist.getItems()){
            System.out.printf("%d. %s\n", index++, c.toString());
        }
    }
}

