package view;

import model.*;

public class ConsoleView {

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
            System.out.println("--- core.Content: " + content.getTitle() + " ---");
            System.out.println(content.toString());
        }
    }

    private void displaySong(Song song) {
        System.out.println("--- core.Song: " + song.getTitle() + " ---");
        System.out.println("Author: " + song.getAuthor());
        System.out.println("core.Genre: " + song.getGenre());
        System.out.println("Year: " + song.getPublicationYear());
        System.out.println("Duration: " + song.formatDuration());
    }

    private void displayPodcast(Podcast podcast) {
        System.out.println("--- core.Podcast Episode: " + podcast.getTitle() + " ---");
        System.out.println("Series: " + podcast.getSeriesName() + "(Ep. " + podcast.getEpisodeNumber() + ")");
        System.out.println("Host: " + podcast.getAuthor());
        System.out.println("core.Genre: " + podcast.getGenre());
        System.out.println("Year: " + podcast.getPublicationYear());
        System.out.println("Duration: " + podcast.formatDuration());
    }

    private void displayAudiobook(Audiobook audiobook) {
        System.out.println("--- core.Audiobook: " + audiobook.getTitle() + " ---");
        System.out.println("Original author: " + audiobook.getAuthor());
        System.out.println("Narrator: " + audiobook.getNarrator());
        System.out.println("Initial publication year: " + audiobook.getInitialPublicationYear());
        System.out.println("Publisher: " + audiobook.getPublisher());
        System.out.println("core.Genre: " + audiobook.getGenre());
        System.out.println("Year: " + audiobook.getPublicationYear());
        System.out.println("Duration: " + audiobook.formatDuration());
    }

    private void displayAlbum(Album album) {
        System.out.println("\n*** core.Album: " + album.getTitle() + " ***");
        System.out.println("Arist: " + album.getAuthor());
        System.out.println("Year: " + album.getPublicationYear());
        System.out.println("core.Genre: " + album.getGenre());
        System.out.println("Entire duration: " + album.getFormatDuration());
        System.out.println("Number of songs: " + album.getItems().size());

        System.out.println("--- List of songs ---");
        int index = 1;
        for (Content c : album.getItems()) {
            System.out.printf("%d. %s - (%s)\n", index++, c.getTitle(), c.formatDuration());
        }
    }

    private void displayPlaylist(Playlist playlist) {
        System.out.println("\n--- core.Playlist: " + playlist.getTitle() + " ---");
        System.out.println("Elements: " + playlist.getItems().size());
        System.out.println("Duration: " + playlist.getFormatDuration());

        if (playlist.getItems().isEmpty()){
            System.out.println("The playlist is empty!");
            return;
        }

        System.out.println("--- core.Content ---");
        int index = 1;
        for(Content c : playlist.getItems()){
            System.out.printf("%d. %s\n", index++, c.toString());
        }
    }
}

