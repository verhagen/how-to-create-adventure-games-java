package com.github.progdojo.adventure;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    public void handleLook() {
        String expectedText = "YOU ARE IN YOUR LIVING ROOM.\n" +
                "YOU CAN GO: NORTH SOUTH EAST\n" +
                "YOU CAN SEE:\n" +
                "    AN OLD DIARY\n" +
                "    A SMALL BOX";
        TextAdventure textAdventure = new TextAdventure();
        assertEquals(expectedText, textAdventure.handleLook(null));
        assertEquals(expectedText, textAdventure.handleLook(""));
        assertEquals("IT LOOKS LIKE GROUND.", textAdventure.handleLook("gro"));

        textAdventure.setRoom(6);
        assertEquals("IT LOOKS LIKE SOMETHING'S BURIED HERE.", textAdventure.handleLook("gro"));
        assertEquals("IT'S NOT HERE!", textAdventure.handleLook("bar"));

        textAdventure.setRoom(5);
        assertEquals("IT'S FILLED WITH RAIN WATER!", textAdventure.handleLook("bar"));
    }
    @Test
    public void handleRead() {
        TextAdventure textAdventure = new TextAdventure();

        assertEquals("IT SAYS: 'ADD SODIUM CHLORIDE PLUS THE\n" +
                "FORMULA TO RAINWATER, TO REACH THE\n" +
                "OTHER WORLD.'", textAdventure.handleRead("dia"));

    }
    @Test
    public void handleOpen() {
        TextAdventure textAdventure = new TextAdventure();

        assertEquals("SOMETHING FELL OUT!", textAdventure.handleOpen("box"));
    }

    @Test
    public void handlePour() {
        TextAdventure textAdventure = new TextAdventure();

        textAdventure.setRoom(2); // kitchen
        assertEquals("THERE'S SOMETHING INSIDE!", textAdventure.handleOpen("cab"));
        assertEquals("YOU ARE IN THE KITCHEN.\n" +
                "YOU CAN GO: WEST\n" +
                "YOU CAN SEE:\n" +
                "    CABINET\n" +
                "    A SALT SHAKER", textAdventure.handleLook(null));
        assertEquals("POURED!", textAdventure.handlePour("sal"));
    }
    @Test
    public void handleClimb() {
        TextAdventure textAdventure = new TextAdventure();

        assertEquals("THERE'S NO TREE HERE!", textAdventure.handleClimb("tre"));
        textAdventure.setRoom(textAdventure.getLocationOfObject("tre"));
        assertEquals("YOU CAN'T REACH THE BRANCHES!", textAdventure.handleClimb("tre"));
        textAdventure.setRoom(textAdventure.getLocationOfObject("lad"));
        assertEquals("WHATEVER FOR?", textAdventure.handleClimb("lad"));
        textAdventure.handleGet("lad");
        textAdventure.setRoom(textAdventure.getLocationOfObject("tre"));
        assertEquals("THE LADDER SINKS UNDER YOUR WEIGHT!\n" +
                "IT DISAPPEARS INTO THE GROUND!", textAdventure.handleClimb("lad"));
    }

    @Test
    public void handleJump() {
        TextAdventure textAdventure = new TextAdventure();

        assertEquals("WHEE! THAT WAS FUN!", textAdventure.handleJump(null));
        assertEquals("WHEE! THAT WAS FUN!", textAdventure.handleJump("box"));
        textAdventure.setRoom(textAdventure.getLocationOfObject("tre"));
        assertEquals("YOU GRAB THE LOWEST BRANCH OF THE\n" +
                "TREE AND PULL YOURSELF UP ....\n" +
                "YOU ARE ON A BRANCH OF A TREE.\n" +
                "YOU CAN GO: DOWN\n" +
                "YOU CAN SEE:\n" +
                "    A MAGIC FAN", textAdventure.handleJump("tree"));
    }

    @Test
    public void handleDig() {
        TextAdventure textAdventure = new TextAdventure();

        assertEquals("YOU CAN'T DIG THAT!", textAdventure.handleDig("box"));

        assertEquals("YOU DON'T HAVE A SHOVEL!", textAdventure.handleDig(""));
        assertEquals("YOU DON'T HAVE A SHOVEL!", textAdventure.handleDig("gro"));
        assertEquals("YOU DON'T HAVE A SHOVEL!", textAdventure.handleDig("hol"));
        textAdventure.setRoom(textAdventure.getLocationOfObject("sho"));
        assertEquals("YOU DON'T FIND ANYTHING.", textAdventure.handleDig("hol"));
        textAdventure.handleGet("sho");
        textAdventure.setRoom(6);
        assertEquals("THERE'S SOMETHING THERE!", textAdventure.handleDig("hol"));
    }

    @Test
    public void handleRow() {
        TextAdventure textAdventure = new TextAdventure();

        assertEquals("HOW CAN YOU ROW THAT?", textAdventure.handleRow("box"));
        assertEquals("YOU'RE NOT IN THE BOAT!", textAdventure.handleRow("boa"));
        textAdventure.setRoom(textAdventure.getLocationOfObject("boa"));
        textAdventure.handleGo("boa");
        assertEquals("YOU DON'T HAVE AN OAR!", textAdventure.handleRow("boa"));
    }

    @Test
    public void handleWave() {
        TextAdventure textAdventure = new TextAdventure();

        assertEquals("YOU CAN'T WAVE THAT!", textAdventure.handleWave("box"));
        textAdventure.setRoom(textAdventure.getLocationOfObject("fan"));
        textAdventure.handleGet("fan");
        assertEquals("YOU FEEL A REFRESHING BREEZE!", textAdventure.handleWave("fan"));
        textAdventure.setRoom(textAdventure.getLocationOfObject("boa"));
        textAdventure.handleGo("boa");
        assertEquals("A POWERFUL BREEZE PROPELS THE BOAT\n" +
                "TO THE OPPOSITE SHORE!", textAdventure.handleWave("fan"));
        assertEquals("YOU ARE ON THE NORTH BANK OF A RIVER.\n" +
                "YOU CAN GO: NORTH\n" +
                "YOU CAN SEE:\n" +
                "    A WOODEN BOAT", textAdventure.handleLeaf("boa"));
    }

    @Test
    public void handleLeaf() {
        TextAdventure textAdventure = new TextAdventure();

        // See test case handleWave()
    }

    @Test
    public void handleFight() {
        TextAdventure textAdventure = new TextAdventure();

        assertEquals("WHOM DO YOU WANT TO FIGHT?", textAdventure.handleFight(""));
        assertEquals("THERE'S NO GUARD HERE!", textAdventure.handleFight("gua"));
        textAdventure.setRoom(textAdventure.getLocationOfObject("gua"));
        assertEquals("YOU ARE IN FRONT OF A LARGE CASTLE.\n" +
                "YOU CAN GO: NORTH SOUTH\n" +
                "YOU CAN SEE:\n" +
                "    A NASTY - LOOKING GUARD", textAdventure.handleLook(""));
        assertEquals("YOU DON'T HAVE A WEAPON!", textAdventure.handleFight("gua"));
        textAdventure.setObjectsLocation("swo", -1);
        assertEquals("THE GUARD, NOTICING YOUR SWORD,\n" +
                "WISELY RETREATS INTO THE CASTLE.", textAdventure.handleFight("gua"));
    }

    @Test
    public void handleWear() {
        TextAdventure textAdventure = new TextAdventure();

        assertEquals("YOU CAN'T WEAR THAT!", textAdventure.handleWear("box"));
        assertEquals("YOU DON'T HAVE THE GLOVES.", textAdventure.handleWear("glo"));
        textAdventure.setObjectsLocation("glo", -1);
        assertEquals("YOU ARE NOW WEARING THE GLOVES.", textAdventure.handleWear("glo"));
    }

}
