package com.github.progdojo.adventure.app;

import com.github.progdojo.adventure.TextAdventure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) throws IOException {
        TextAdventure textAdventure = new TextAdventure();
        System.out.println("WELCOME TO: HOW TO CREATE (TEXT) ADVENTURES GAMES");
        System.out.println(textAdventure.getDescription().stream().collect(Collectors.joining("\n")));
//        System.out.println("Choose a room you like to see: 1..%d".formatted(textAdventure.getNumberOfRooms()));
        boolean exitGame = false;
        do {

            System.out.println("WHAT NOW?");

            // Enter data using BufferReader
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            // Reading data using readLine
            String input = reader.readLine();
            String[] parser = textAdventure.parser(input);
            System.out.println("Verb: %s  Noun: %s".formatted(parser[0], parser[1]));
            if (parser[0].equalsIgnoreCase("exi") || parser[0].equalsIgnoreCase("qui")) {
                exitGame = true;
            }
            String text = textAdventure.handle(parser);
            if (text != null && text != "") {
                System.out.println(text);
            }
            else {
                System.out.println(textAdventure.getDescription().stream().collect(Collectors.joining("\n")));
            }
        }
        while (! exitGame);

        System.out.println("Thanks for playing 'HOW TO CREATE (TEXT) ADVENTURES GAMES'");
        System.out.println("Looking forward to see you soon again!");
    }
}
