package com.github.progdojo.adventure;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TextAdventureTest {

    @Test
    public void directionAsInt() {
        TextAdventure textAdventure = new TextAdventure();
        assertEquals(0, textAdventure.directionAsInt("north"));
        assertEquals(5, textAdventure.directionAsInt("DOWN"));
    }
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

    @Test
    public void objectDescription() {
        TextAdventure textAdventure = new TextAdventure();

        assertEquals("YOU CAN SEE:\n" +
                "    AN OLD DIARY\n" +
                "    A SMALL BOX", textAdventure.objectDescription(1));
        assertEquals("YOU CAN SEE:\n    CABINET", textAdventure.objectDescription(2));
        assertEquals("YOU CAN SEE:\n    A DICTIONARY", textAdventure.objectDescription(3));
        assertEquals("YOU CAN SEE:\n    NOTHING OF INTEREST", textAdventure.objectDescription(13));
    }

    @Test
    public void parser() {
        TextAdventure textAdventure = new TextAdventure();

        assertArrayEquals(new String[] {"dro", "swo"}, textAdventure.parser("drop sword"));
        assertArrayEquals(new String[] {"nor", null}, textAdventure.parser("north"));
        assertArrayEquals(new String[] {"go", "nor"}, textAdventure.parser("go north"));
        assertArrayEquals(new String[] {null, null}, textAdventure.parser(null));
        assertArrayEquals(new String[] {null, null}, textAdventure.parser(""));
    }

    @Test
    public void handleGo() {
        TextAdventure textAdventure = new TextAdventure();

        assertEquals(1, textAdventure.getRoom());
        textAdventure.handle(textAdventure.parser("go north"));
        assertEquals(4, textAdventure.getRoom());
        textAdventure.handle(textAdventure.parser("go south"));
        assertEquals(1, textAdventure.getRoom());

        String text = textAdventure.handle(textAdventure.parser("go up"));
        assertEquals("YOU CAN'T GO THERE!", text);
    }

    @Test
    public void handleGetDropInventory() {
        TextAdventure textAdventure = new TextAdventure();

        assertEquals(0, textAdventure.getInventory().size());
        assertEquals("YOU ARE CARRYING:\n    NOTHING", textAdventure.handle(textAdventure.parser("inventory")));
        assertEquals("TAKEN", textAdventure.handle(textAdventure.parser("get diary")));
        assertEquals(1, textAdventure.getInventory().size());
        assertEquals("TAKEN", textAdventure.handle(textAdventure.parser("get box")));
        assertEquals(2, textAdventure.getInventory().size());
        assertEquals("DROPPED AN OLD DIARY.", textAdventure.handle(textAdventure.parser("drop diary")));
        List<Integer> inventory = textAdventure.getInventory();
        assertEquals(1, inventory.size());
        assertEquals(textAdventure.getObject("box"), inventory.get(0));
    }

    @Test
    public void handleDrop() {

    }
    @Test
    public void handleInventory() {

    }
}
