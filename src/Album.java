public class Album extends AudioCollection {



    public Album(String title, String artist, int publicationYear, String genre){
        super(title, artist, publicationYear, genre);
    }

    public boolean addContent(Content content){

        if(!(content instanceof Song)){
            System.out.println("Error: only songs can be added to albums.");
            return false;
        }
        Song song = (Song) content;

        if(!song.getAuthor().equals(this.getAuthor())){
            System.out.println("Error: Song (" + song.getTitle() +") does not match album artist");
            return false;
        }
        else if (song.getPublicationYear() != this.getPublicationYear()){
            System.out.println("Error: Song (" + song.getTitle() +") does not match album release year");
            return false;
        }
        if (items.contains(song)){

            System.out.println("Error: Song (" + song.getTitle() +") is already in the album");
            return false;
        } else {
            song.setAlbumTitle(this.getTitle());
            return items.add(song);
        }
    }




    @Override
    public void displayInfo(){
        System.out.println("\n*** Album: " + getTitle() + " ***");
        System.out.println("Arist: " + getAuthor());
        System.out.println("Year: " + getPublicationYear());
        System.out.println("Genre: " + getGenre());
        System.out.println("Entire duration: " + getFormatDuration());
        System.out.println("Number of songs: " + items.size());

        System.out.println("--- List of songs ---");
        int index = 1;
        for(Content c : items) {
            System.out.printf("%d. %s - (%s)\n", index++, c.getTitle(), c.formatDuration());
        }
    }

}