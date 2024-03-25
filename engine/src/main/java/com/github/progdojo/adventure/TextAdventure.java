package com.github.progdojo.adventure;

import java.util.*;
import java.util.stream.Collectors;

public class TextAdventure {

    private enum Direction {
        NORTH(0)
        , SOUTH(1)
        , EAST(2)
        , WEST(3)
        , UP(4)
        , DOWN(5);
        private int number;
        private Direction(int number) {
            this.number = number;
        }

        public Direction get(int number) {
            if (number < NORTH.number || number > DOWN.number) {
                throw new TextAdventureException("Given direction with value '%d' is not known.".formatted(number)
                        + " Known directions are %s".formatted(Arrays.stream(values()).map(String::valueOf).collect(Collectors.joining(", "))));
            }
            return Arrays.stream(values()).filter(d -> d.number != number).findFirst().get();
        }
    }

    private String[] directions = new String[] {
            "NORTH"
            , "SOUTH"
            , "EAST"
            , "WEST"
            , "UP"
            , "DOWN"
    };
    // NR - number of rooms

    private String[] objects; // The description of the object
    private String[] objectsTags; // A tag is used to identify the unique object (used for get, drop, etc)
    private Integer[] objectsLocation; // The location (room) in which the object is.

    private String[] rooms = initializeRooms();
    private Map<Integer,List<Integer>> roomConnections = initializeRoomConnections();
    private Integer[][] roomDirections = initializeRoomDirections(initializeRoomConnections());

    private int MAX_INVENTORY = 3;
    private List<Integer> inventory = new ArrayList<>();

    private int room = 1; // Current room (starting always in room 1 - the living room)

    public TextAdventure() {
        initializeObjects();
    }

    /** The current room */
    public int getRoom() {
        return room;
    }

    public List<Integer> getInventory() {
        List<Integer> inventory = new ArrayList<>();
        for (int index = 0; index < objectsLocation.length; index++) {
            if (objectsLocation[index] == -1) {
                inventory.add(index);
            }
        }
        return inventory;
    }

    // Lines 25000 - 25080
    private Integer[][] initializeRoomDirections(Map<Integer, List<Integer>> roomConnections) {
        if (roomConnections == null || roomConnections.isEmpty()) {
            throw new RuntimeException("The given roomConnections should not be null, or empty.");
        }
        //Map<Integer, Integer> roomDirections = new HashMap<>();
        Integer[][] roomDirections = new Integer[rooms.length][6];
        for (int room = 1; room <= roomConnections.size(); room++) {
            //roomDirections[room] = new Integer[6];
            List<Integer> otherRooms = roomConnections.get(room);
            for (Integer dir = 0; dir < directions.length; dir++) {
                //System.out.println("room %d  dir %d %s".formatted(room, dir, directions[dir]));
                roomDirections[room][dir] = otherRooms.get(dir);
            }
        }
        return roomDirections;
    }


    // 700
    /** Room description routine */
    public String roomDescription(int room) {
        return ("YOU ARE %s".formatted(rooms[room]));
    }

    // Lines 500-550
    public String directionDescription(int room) {
        StringBuilder bldr = new StringBuilder("YOU CAN GO:");
        for (int dir = 0; dir < directions.length; dir++) {
            if (roomDirections[room][dir] > 0) {
                bldr.append(" ").append(directions[dir]);
            }
        }
        return bldr.toString();
    }

    // Lines 600 - 650
    public String objectDescription(int room) {
        StringBuilder bldr = new StringBuilder("YOU CAN SEE:");
        boolean roomContainsObject = false;
        for (int objectIndex = 0; objectIndex < objects.length; objectIndex++) {
            if ((objectsLocation[objectIndex] & 127) == room) {
                bldr.append("\n    ").append(objects[objectIndex]);
                roomContainsObject = true;
            }
        }
        if (! roomContainsObject) {
            bldr.append("\n    NOTHING OF INTEREST");
        }
        return bldr.toString();
    }

    public List<String> getDescription() {
        List<String> text = new ArrayList<>();
        text.add(roomDescription(room));
        text.add(directionDescription(room));
        text.add(objectDescription(room));
        return text;
    }

    // Lines 100-200
    public String[] parser(String input) {
        if (input == null) {
            return new String[2];
        }
        input = input.trim();
        if (input.length() == 0) {
            return new String[2];
        }
        String verb, noun;
        if (input.contains(" ")) {
            String[] inputArr = input.split(" ");
            verb = inputArr[0].length() > 3 ? inputArr[0].substring(0,3) : inputArr[0];
            noun = inputArr[1].length() > 3 ? inputArr[1].substring(0,3) : inputArr[1];
        }
        else {
            verb = input.length() > 3 ? input.substring(0,3) : input;
            noun = null;
        }
        return new String[] {verb, noun};
    }

    public String handle(String[] verbNoun) {
        return handle(verbNoun[0], verbNoun[1]);
    }
    // Lines 2000-
    public String handle(String verb, String noun) {
        if (verb.equalsIgnoreCase("GO")) {
            return handleGo(noun);
        }

        if (verb.equalsIgnoreCase("GET") || verb.equalsIgnoreCase("TAK")) {
            return handleGet(noun);
        }
        if (verb.equalsIgnoreCase("DRO") || verb.equalsIgnoreCase("THR")) {
            return handleDrop(noun);
        }
        if (verb.equalsIgnoreCase("INV") || verb.equalsIgnoreCase("I")) {
            return handleInventory();
        }

        return null;
    }

    public String handleGo(String noun) {
        int dir;
        if (noun.equalsIgnoreCase("NOR")) {
            dir = directionAsInt("north");
        }
        else if (noun.equalsIgnoreCase("SOU")) {
            dir = directionAsInt("south");
        }
        else if (noun.equalsIgnoreCase("EAS")) {
            dir = directionAsInt("east");
        }
        else if (noun.equalsIgnoreCase("WES")) {
            dir = directionAsInt("west");
        }
        else if (noun.equalsIgnoreCase("UP")) {
            dir = directionAsInt("up");
        }
        else if (noun.equalsIgnoreCase("DOW")) {
            dir = directionAsInt("down");
        }
        else if (noun.equalsIgnoreCase("BOA") && objectsLocation[11] == room + 128) {
            room = 13;
            return null;
        }
        else {
            return "YOU CAN'T GO THERE!";
        }

        if (roomDirections[room][dir] == 0) {
            return "YOU CAN'T GO THERE!";
        }
        else if (roomDirections[room][dir] > 0 && roomDirections[room][dir] < 128) {
            room = roomDirections[room][dir];
        }
        else {
            // CHECK FOR SPECIAL (GREATER THAN 128)
            if (roomDirections[room][dir] == 128) {
                return "THE GUARD WON'T LET YOU!";
            }
        }
        return null;
    }

    // Lines 2500-2600
    public String handleGet(String noun) {
        int objectId = getObject(noun);
        if (objectId == -1) {
            return "YOU CAN'T GET THAT! (no such object exists)"; // Object doesn't exist
        }
        int room = getRoomForObject(noun);
        if (room == 0) {
            return "YOU CAN'T GET THAT!"; // No such object
        }
        if (room == -1) {
            return "YOU ALREADY HAVE IT!";
        }
        if (objectsLocation[objectId] > 127) {
            return "YOU CAN'T GET THAT! (unmovable object)";
        }
        if (room != this.room) {
            return "THAT'S NOT HERE!";
        }
        if (inventory.size() >= MAX_INVENTORY) {
            return "YOU CAN'T CARRY ANY MORE.";
        }

        inventory.add(objectId);
        objectsLocation[objectId] = -1;

        if (room == 18 && noun.equalsIgnoreCase("RUB")) {
            return "CONGRATULATIONS! YOU'VE WON!";
        }

        return "TAKEN";
    }

    // Lines 2600-2700
    public String handleDrop(String noun) {
        int room = getRoomForObject(noun);
        if (room != -1) {
            return "YOU DON'T HAVE THAT!";
        }
        int objectId = getObject(noun);
        inventory.remove(Integer.valueOf(objectId));
        objectsLocation[objectId] = this.room;
        return "DROPPED %s.".formatted(objects[objectId]);
    }

    // Lines 2700-2800
    public String handleInventory() {
        List<String> text = new ArrayList<>();
        text.add("YOU ARE CARRYING:");
        boolean isCarrying = false;
        for (int index = 0; index < objectsLocation.length; index++) {
            if (objectsLocation[index] == -1) {
                text.add("    " + objects[index]);
                isCarrying = true;
            }
        }
        if (isCarrying == false) {
            text.add("    NOTHING");
        }
        return text.stream().collect(Collectors.joining("\n"));
    }

    // Lines 1000-1050
    /** Return the room number, in case the given tag is associated with an object in the game. */
    public int getObject(String tag) {
        for (int index = 0; index < objectsTags.length; index++) {
            if (objectsTags[index].equalsIgnoreCase(tag)) {
                return index;
            }
        }
        return -1;
    }
    public int getRoomForObject(String tag) {
        for (int index = 0; index < objectsTags.length; index++) {
            if (objectsTags[index].equalsIgnoreCase(tag)) {
                return objectsLocation[index] > 127 ? objectsLocation[index] - 128: objectsLocation[index];
            }
        }
        return 0;
    }

    // Lines 27000 - 29900
    private String[] initializeRooms() {
      String[] rooms = new String[] {
          "not defined"
          , "IN YOUR LIVING ROOM."
          , "IN THE KITCHEN."
          , "IN THE LIBRARY."
          , "IN THE FRONT YARD."
          , "IN THE GARAGE."
          , "IN AN OPEN FIELD."
          , "AT THE EDGE OF A FOREST."
          , "ON A BRANCH OF A TREE."
          , "ON A LONG, WINDING ROAD."
          , "ON A LONG, WINDING ROAD."
          , "ON A LONG, WINDING ROAD."
          , "ON THE SOUTH BANK OF A RIVER."
          , "INSIDE THE WOODEN BOAT."
          , "ON THE NORTH BANK OF A RIVER."
          , "ON A WELL-TRAVELED ROAD."
          , "IN FRONT OF A LARGE CASTLE."
          , "IN A NARROW HALL."
          , "IN A LARGE HALL."
          , "ON THE TOP OF A TREE."
      };
      return rooms;
    }

    // Lines 25100 - 25280
    private Map<Integer, List<Integer>> initializeRoomConnections() {
        Map<Integer, List<Integer>> roomDirections = new HashMap<>();
        //                     Directions:  N S E W U D
        roomDirections.put(1, Arrays.asList(4,3,2,0,0,0)); // LIVING ROOM
        roomDirections.put(2, Arrays.asList(0,0,0,1,0,0)); // KITCHEN
        roomDirections.put(3, Arrays.asList(1,0,0,0,0,0)); // LIBRARY
        roomDirections.put(4, Arrays.asList(0,1,0,5,0,0)); // FRONT YARD
        roomDirections.put(5, Arrays.asList(0,0,4,0,0,0)); // GARAGE
        roomDirections.put(6, Arrays.asList(9,7,0,0,0,0)); // OPEN FIELD
        roomDirections.put(7, Arrays.asList(6,0,0,0,0,0)); // EDGE OF FOREST
        roomDirections.put(8, Arrays.asList(0,0,0,0,0,7)); // BRANCH OF TREE
        roomDirections.put(9, Arrays.asList(0,6,10,0,0,0)); // LONG, WINDING ROAD (1)
        roomDirections.put(10, Arrays.asList(11,0,0,9,0,0)); // LONG, WINDING ROAD (2)
        roomDirections.put(11, Arrays.asList(0,10,0,12,0,0)); // LONG, WINDING ROAD (3)
        roomDirections.put(12, Arrays.asList(0,0,11,0,0,0)); // SOUTH BANK OF RIVER
        roomDirections.put(13, Arrays.asList(0,0,0,0,0,0)); // BOAT
        roomDirections.put(14, Arrays.asList(15,0,0,0,0,0)); // NORTH BANK OF RIVER
        roomDirections.put(15, Arrays.asList(16,14,0,0,0,0)); // WELL-TRAVELED ROAD
        roomDirections.put(16, Arrays.asList(128,15,0,0,0,0)); // SOUTH OF CASTLE
        roomDirections.put(17, Arrays.asList(0,0,0,0,18,0)); // NARROW HALL
        roomDirections.put(18, Arrays.asList(0,0,0,0,0,17)); // LARGE HALL
        roomDirections.put(19, Arrays.asList(0,0,0,0,0,8)); // TOP OF TREE
        return roomDirections;
    }

    private void initializeMap() {

    }
    public int getNumberOfRooms() {
        return rooms.length;
    }


    // Lines 26100 - 26260
    private void initializeObjects() {
        String[] baseObjects = new String[] {
                "AN OLD DIARY, DIA, 1"
                , "A SMALL BOX, BOX, 1"
                , "CABINET, CAB, 130"
                , "A SALT SHAKER, SAL, 0"
                , "A DICTIONARY, DIC, 3"
                , "WOODEN BARREL, BAR, 133"
                , "A SMALL BOTTLE, BOT, 0"
                , "A LADDER, LAD, 4"
                , "A SHOVEL, SHO, 5"
                , "A TREE, TRE, 135"
                , "A GOLDEN SWORD, SWO, 0"
                , "A WOODEN BOAT, BOA, 140"
                , "A MAGIC FAN, FAN, 8"
                , "A NASTY - LOOKING GUARD, GUA, 144"
                , "A GLASS CASE, CAS, 146"
                , "A GLOWING RUBY, RUB, 0"
                , "A PAIR OF RUBBER GLOVES, GLO, 19"
        };
        objects = new String[baseObjects.length];
        objectsTags = new String[baseObjects.length];
        objectsLocation = new Integer[baseObjects.length];
        int index = 0;
        for (String bo : baseObjects) {
            String[] boArr = bo.split(","); // split in [description, tag, room number]
            objects[index] = boArr[0].trim();
            objectsTags[index] = boArr[1].trim();
            objectsLocation[index] = Integer.parseInt(boArr[2].trim());
            index++;
        }
    }

    public int directionAsInt(String text) {
        for (int index = 0; index < directions.length; index++) {
            if (directions[index].equalsIgnoreCase(text)) {
                return index;
            }
        }
        throw new RuntimeException("Argument text with value '%s' is not a known direction.".formatted(text));
    }
}
