package controller;

import model.Genre;
import java.math.BigInteger;
import java.util.Scanner;

public class InputHelper {
    private final Scanner scan;

    public InputHelper() {
        this.scan = new Scanner(System.in);
    }

    public String readLine() {
        return scan.nextLine().trim();
    }

    public String prompt(String label) {
        System.out.print(label + ": ");
        return readLine();
    }

    public int promptInt(String label) {
        while (true) {
            System.out.print(label + ": ");
            String in = readLine();
            try {
                return Integer.parseInt(in);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    public BigInteger promptDuration() {
        while (true) {
            System.out.print("Duration (seconds): ");
            String in = readLine();
            try {
                long sec = Long.parseLong(in);
                return BigInteger.valueOf(sec);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    public Genre promptGenre() {
        System.out.println("Available Genres: ");
        for (Genre g : Genre.values()) System.out.print(g + " ");
        System.out.println();
        System.out.print("Genre: ");
        String in = readLine().toUpperCase();
        try {
            return Genre.valueOf(in);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}