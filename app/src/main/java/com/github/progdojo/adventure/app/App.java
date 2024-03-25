package com.github.progdojo.adventure.app;

import com.github.progdojo.adventure.TextAdventure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class App {
    public static void main(String[] args) throws IOException {
        TextAdventure textAdventure = new TextAdventure();
        System.out.println("WELCOME TO: HOW TO CREATE (TEXT) ADVENTURES GAMES");
        System.out.println("Choose a room you like to see: 1..%d".formatted(textAdventure.getNumberOfRooms()));

        // Enter data using BufferReader
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        // Reading data using readLine
        String input = reader.readLine();
        while (! input.equalsIgnoreCase("exit") || ! input.equalsIgnoreCase("quit")) {
            if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
                System.out.println("Thanks for playing 'HOW TO CREATE (TEXT) ADVENTURES GAMES'");
                System.out.println("Looking forward to see you soon again!");
                return;
            }
            try {
                int room = Integer.parseInt(input);
                if (room >= 1 && room <= textAdventure.getNumberOfRooms()) {
                    System.out.println(textAdventure.roomDescription(room));
                    System.out.println(textAdventure.directionDescription(room));
                } else {
                    System.out.println("NO ROOM EXISTS WITH NUMBER %d.".formatted(room));
                }
            }
            catch (NumberFormatException nfe) {
                System.out.println("The given input '%s' is not a number.".formatted(input));
                System.out.println("Please give a number, or use EXIT to leave the adventure game.");
            }
            input = reader.readLine();
        }
        System.out.println(textAdventure.roomDescription(1));
    }
}
