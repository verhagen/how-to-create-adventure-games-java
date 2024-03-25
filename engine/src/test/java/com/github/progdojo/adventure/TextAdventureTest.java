package com.github.progdojo.adventure;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TextAdventureTest {

    @Test
    public void roomInfo() {
        TextAdventure textAdventure = new TextAdventure();

        assertEquals("YOU ARE IN YOUR LIVING ROOM.", textAdventure.roomDescription(1));
        assertEquals("YOU ARE IN AN OPEN FIELD.", textAdventure.roomDescription(6));
        assertEquals("YOU ARE ON THE TOP OF A TREE.", textAdventure.roomDescription(19));
    }

    @Test
    public void directionDescription() {
        TextAdventure textAdventure = new TextAdventure();

        assertEquals("YOU CAN GO: NORTH SOUTH EAST", textAdventure.directionDescription(1));
        assertEquals("YOU CAN GO: WEST", textAdventure.directionDescription(2));
        assertEquals("YOU CAN GO: NORTH", textAdventure.directionDescription(3));
    }
}
