package com.github.progdojo.adventure;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TextAdventureScenarioTest {
    @Test
    public void completeScenario() {
        TextAdventure textAdventure = new TextAdventure();

        assertEquals("YOU ARE IN YOUR LIVING ROOM.\n" +
                "YOU CAN GO: NORTH SOUTH EAST\n" +
                "YOU CAN SEE:\n" +
                "    AN OLD DIARY\n" +
                "    A SMALL BOX", textAdventure.handle("look"));
        assertEquals("IT SAYS: 'ADD SODIUM CHLORIDE PLUS THE\n" +
                "FORMULA TO RAINWATER, TO REACH THE\n" +
                "OTHER WORLD.'", textAdventure.handle("read diary"));
        assertEquals("SOMETHING FELL OUT!", textAdventure.handle("open box"));
        assertEquals("YOU ARE IN YOUR LIVING ROOM.\n" +
                "YOU CAN GO: NORTH SOUTH EAST\n" +
                "YOU CAN SEE:\n" +
                "    AN OLD DIARY\n" +
                "    A SMALL BOX\n" +
                "    A SMALL BOTTLE", textAdventure.handle("l"));
        assertEquals("TAKEN", textAdventure.handle("get bottle"));
        assertEquals("YOU ARE IN THE KITCHEN.\n" +
                "YOU CAN GO: WEST\n" +
                "YOU CAN SEE:\n" +
                "    CABINET", textAdventure.handle("go east"));
        assertEquals("THERE'S SOMETHING INSIDE!", textAdventure.handle("open cabinet"));
        assertEquals("TAKEN", textAdventure.handle("get salt"));
        assertEquals("YOU ARE IN YOUR LIVING ROOM.\n" +
                "YOU CAN GO: NORTH SOUTH EAST\n" +
                "YOU CAN SEE:\n" +
                "    AN OLD DIARY\n" +
                "    A SMALL BOX", textAdventure.handle("go west"));
        assertEquals("YOU ARE IN THE LIBRARY.\n" +
                "YOU CAN GO: NORTH\n" +
                "YOU CAN SEE:\n" +
                "    A DICTIONARY", textAdventure.handle("go south"));
        assertEquals("IT SAYS: SODIUM CHLORIDE IS\n" +
                "COMMON TABLE SALT.", textAdventure.handle("read dictionary"));
        assertEquals("YOU ARE IN YOUR LIVING ROOM.\n" +
                "YOU CAN GO: NORTH SOUTH EAST\n" +
                "YOU CAN SEE:\n" +
                "    AN OLD DIARY\n" +
                "    A SMALL BOX", textAdventure.handle("go north"));
        assertEquals("YOU ARE IN THE FRONT YARD.\n" +
                "YOU CAN GO: SOUTH WEST\n" +
                "YOU CAN SEE:\n" +
                "    A LADDER", textAdventure.handle("go north"));
        assertEquals("WHATEVER FOR?", textAdventure.handle("climb ladder"));
        assertEquals("TAKEN", textAdventure.handle("get ladder"));
        assertEquals("YOU ARE CARRYING:\n" +
                "    A SALT SHAKER\n" +
                "    A SMALL BOTTLE\n" +
                "    A LADDER", textAdventure.handle("inventory"));
        assertEquals("YOU ARE IN THE GARAGE.\n" +
                "YOU CAN GO: EAST\n" +
                "YOU CAN SEE:\n" +
                "    WOODEN BARREL\n" +
                "    A SHOVEL", textAdventure.handle("go west"));
        assertEquals("YOU CAN'T DIG THAT!", textAdventure.handle("dig"));
        assertEquals("YOU CAN'T CARRY ANY MORE.", textAdventure.handle("take shovel"));
        assertEquals("DROPPED A SMALL BOTTLE.", textAdventure.handle("drop bottle"));
        assertEquals("TAKEN", textAdventure.handle("take shovel"));
        assertEquals("YOU ARE IN THE FRONT YARD.\n" +
                "YOU CAN GO: SOUTH WEST\n" +
                "YOU CAN SEE:\n" +
                "    NOTHING OF INTEREST", textAdventure.handle("go east"));
        assertEquals("YOU DON'T FIND ANYTHING.", textAdventure.handle("dig ground"));
        assertEquals("YOU ARE IN THE GARAGE.\n" +
                "YOU CAN GO: EAST\n" +
                "YOU CAN SEE:\n" +
                "    WOODEN BARREL\n" +
                "    A SMALL BOTTLE", textAdventure.handle("go west"));
        assertEquals("IT'S FILLED WITH RAIN WATER!", textAdventure.handle("look barrel"));
        assertEquals("POURED!", textAdventure.handle("pour salt"));
        assertEquals("DROPPED A SALT SHAKER.", textAdventure.handle("drop salt"));
        assertEquals("TAKEN", textAdventure.handle("take bottle"));
        assertEquals("THERE'S SOMETHING WRITTEN ON IT!", textAdventure.handle("look bottle"));
        assertEquals("THERE IS AN EXPLOSION!\n" +
                "EVERYTHING GOES BLACK!\n" +
                "SUDDENLY YOU ARE ...\n" +
                "... SOMEWHERE ELSE!", textAdventure.handle("pour bottle"));
        assertEquals("YOU ARE IN AN OPEN FIELD.\n" +
                "YOU CAN GO: NORTH SOUTH\n" +
                "YOU CAN SEE:\n" +
                "    NOTHING OF INTEREST", textAdventure.handle("l"));
        assertEquals("THERE'S SOMETHING THERE!", textAdventure.handle("dig hole"));
        assertEquals("YOU ARE IN AN OPEN FIELD.\n" +
                "YOU CAN GO: NORTH SOUTH\n" +
                "YOU CAN SEE:\n" +
                "    A GOLDEN SWORD", textAdventure.handle("l"));
        assertEquals("DROPPED A SMALL BOTTLE.", textAdventure.handle("drop bottle"));
        assertEquals("TAKEN", textAdventure.handle("take sword"));

        assertEquals("YOU ARE AT THE EDGE OF A FOREST.\n" +
                "YOU CAN GO: NORTH\n" +
                "YOU CAN SEE:\n" +
                "    A TREE", textAdventure.handle("go south"));
        assertEquals("THE LADDER SINKS UNDER YOUR WEIGHT!\n" +
                "IT DISAPPEARS INTO THE GROUND!", textAdventure.handle("climb ladder"));
        assertEquals("YOU GRAB THE LOWEST BRANCH OF THE\n" +
                "TREE AND PULL YOURSELF UP ....\n" +
                "YOU ARE ON A BRANCH OF A TREE.\n" +
                "YOU CAN GO: DOWN\n" +
                "YOU CAN SEE:\n" +
                "    A MAGIC FAN", textAdventure.handle("jump tree"));
        assertEquals("YOU ARE CARRYING:\n" +
                "    A SHOVEL\n" +
                "    A GOLDEN SWORD", textAdventure.handle("inventory"));
        assertEquals("TAKEN", textAdventure.handle("get fan"));
        assertEquals("DROPPED A SHOVEL.", textAdventure.handle("drop shovel"));
        assertEquals("YOU GRAB A HIGHER BRANCH ON THE\n" +
                "TREE AND PULL YOURSELF UP ....\n" +
                "YOU ARE ON THE TOP OF A TREE.\n" +
                "YOU CAN GO: DOWN\n" +
                "YOU CAN SEE:\n" +
                "    A PAIR OF RUBBER GLOVES", textAdventure.handle("jump tree"));
        assertEquals("TAKEN", textAdventure.handle("get gloves"));
        assertEquals("YOU ARE ON A BRANCH OF A TREE.\n" +
                "YOU CAN GO: DOWN\n" +
                "YOU CAN SEE:\n" +
                "    A SHOVEL", textAdventure.handle("go down"));
        assertEquals("YOU ARE AT THE EDGE OF A FOREST.\n" +
                "YOU CAN GO: NORTH\n" +
                "YOU CAN SEE:\n" +
                "    A TREE", textAdventure.handle("go down"));
        assertEquals("YOU ARE IN AN OPEN FIELD.\n" +
                "YOU CAN GO: NORTH SOUTH\n" +
                "YOU CAN SEE:\n" +
                "    A SMALL BOTTLE", textAdventure.handle("go north"));

        assertEquals("YOU ARE ON A LONG, WINDING ROAD.\n" +
                "YOU CAN GO: SOUTH EAST\n" +
                "YOU CAN SEE:\n" +
                "    NOTHING OF INTEREST", textAdventure.handle("go north"));
        assertEquals("YOU ARE ON A LONG, WINDING ROAD.\n" +
                "YOU CAN GO: NORTH WEST\n" +
                "YOU CAN SEE:\n" +
                "    NOTHING OF INTEREST", textAdventure.handle("go east"));
        assertEquals("YOU ARE ON A LONG, WINDING ROAD.\n" +
                "YOU CAN GO: SOUTH WEST\n" +
                "YOU CAN SEE:\n" +
                "    NOTHING OF INTEREST", textAdventure.handle("go north"));
        assertEquals("YOU ARE ON THE SOUTH BANK OF A RIVER.\n" +
                "YOU CAN GO: EAST\n" +
                "YOU CAN SEE:\n" +
                "    A WOODEN BOAT", textAdventure.handle("go west"));
        assertEquals("YOU SEE NOTHING UNUSUAL.", textAdventure.handle("examine boat"));
        assertEquals("YOU ARE INSIDE THE WOODEN BOAT.\n" +
                "YOU CAN GO:\n" +
                "YOU CAN SEE:\n" +
                "    NOTHING OF INTEREST", textAdventure.handle("go boat"));
        assertEquals("HOW CAN YOU ROW THAT?", textAdventure.handle("row"));
        assertEquals("YOU DON'T HAVE AN OAR!", textAdventure.handle("row boat"));
        assertEquals("HOW CAN YOU ROW THAT?", textAdventure.handle("row sword"));
        assertEquals("A POWERFUL BREEZE PROPELS THE BOAT\n" +
                "TO THE OPPOSITE SHORE!", textAdventure.handle("wave fan"));
        assertEquals("YOU ARE ON THE NORTH BANK OF A RIVER.\n" +
                "YOU CAN GO: NORTH\n" +
                "YOU CAN SEE:\n" +
                "    A WOODEN BOAT", textAdventure.handle("leaf boat"));
        assertEquals("YOU ARE ON A WELL-TRAVELED ROAD.\n" +
                "YOU CAN GO: NORTH SOUTH\n" +
                "YOU CAN SEE:\n" +
                "    NOTHING OF INTEREST", textAdventure.handle("go north"));

        assertEquals("YOU ARE IN FRONT OF A LARGE CASTLE.\n" +
                "YOU CAN GO: NORTH SOUTH\n" +
                "YOU CAN SEE:\n" +
                "    A NASTY - LOOKING GUARD", textAdventure.handle("go north"));
        assertEquals("THE GUARD WON'T LET YOU!", textAdventure.handle("go north"));
        assertEquals("THE GUARD, NOTICING YOUR SWORD,\n" +
                "WISELY RETREATS INTO THE CASTLE.", textAdventure.handle("fight guard"));
        assertEquals("YOU ARE IN A NARROW HALL.\n" +
                "YOU CAN GO: UP\n" +
                "YOU CAN SEE:\n" +
                "    NOTHING OF INTEREST", textAdventure.handle("go north"));

        assertEquals("YOU ARE IN A LARGE HALL.\n" +
                "YOU CAN GO: DOWN\n" +
                "YOU CAN SEE:\n" +
                "    A GLASS CASE", textAdventure.handle("go up"));
        assertEquals("THE CASE IS ELECTRIFIED!", textAdventure.handle("open case"));
        assertEquals("YOU ARE NOW WEARING THE GLOVES.", textAdventure.handle("wear gloves"));
        assertEquals("THE GLOVES INSULATE AGAINST THE ELECTRICITY! THE CASE OPENS!", textAdventure.handle("open case"));
        assertEquals("YOU ARE IN A LARGE HALL.\n" +
                "YOU CAN GO: DOWN\n" +
                "YOU CAN SEE:\n" +
                "    A GLASS CASE\n" +
                "    A GLOWING RUBY", textAdventure.handle("look"));
        assertEquals("DROPPED A MAGIC FAN.", textAdventure.handle("drop fan"));
        assertEquals("CONGRATULATIONS! YOU'VE WON!", textAdventure.handle("get ruby"));
    }

    @Test
    public void scenarioFirstFormulaThenShaker() {
        TextAdventure textAdventure = new TextAdventure();

        assertEquals("SOMETHING FELL OUT!", textAdventure.handle("open box"));
        assertEquals("TAKEN", textAdventure.handle("get bottle"));
        assertEquals("YOU ARE IN THE KITCHEN.\n" +
                "YOU CAN GO: WEST\n" +
                "YOU CAN SEE:\n" +
                "    CABINET", textAdventure.handle("go east"));
        assertEquals("THERE'S SOMETHING INSIDE!", textAdventure.handle("open cabinet"));
        assertEquals("TAKEN", textAdventure.handle("get shaker"));
        textAdventure.handle("go west");
        textAdventure.handle("go north");
        textAdventure.handle("go west");
        assertEquals("POURED!", textAdventure.handle("pour formula"));
        assertEquals("THERE IS AN EXPLOSION!\n" +
                "EVERYTHING GOES BLACK!\n" +
                "SUDDENLY YOU ARE ...\n" +
                "... SOMEWHERE ELSE!", textAdventure.handle("pour shaker"));
    }
}
