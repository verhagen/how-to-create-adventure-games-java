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
        System.out.println(textAdventure.handleLook(null));
        boolean exitGame = false;
        do {

            System.out.println("WHAT NOW?");

            // Enter data using BufferReader
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            // Reading data using readLine
            String input = reader.readLine();
            String[] parser = textAdventure.parser(input);
            String text;
            if (parser[0].equalsIgnoreCase("exi") || parser[0].equalsIgnoreCase("qui")) {
                System.out.println("ARE YOU SURE YOU WANT TO QUIT (Y/N)");
                String inputYesNo = reader.readLine();
                if (inputYesNo.equalsIgnoreCase("yes") || inputYesNo.equalsIgnoreCase("y")) {
                    exitGame = true;
                    text = "Thanks for playing 'HOW TO CREATE (TEXT) ADVENTURES GAMES'" +
                            "\nLooking forward to see you soon again!";
                }
                else {
                    text = "WONDERFUL! LET US CONTINUE OUR JOURNEY";
                }
            }
            else {
                text = textAdventure.handle(parser);
            }
            if (text != null && text != "") {
                System.out.println(text);
            } else {
                System.err.println("Expecting always some text...");
                System.out.println(textAdventure.getDescription().stream().collect(Collectors.joining("\n")));
            }
        }
        while (! exitGame);
    }
}
