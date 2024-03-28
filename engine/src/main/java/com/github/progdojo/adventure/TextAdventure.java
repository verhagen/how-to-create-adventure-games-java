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

    // Only for test cases
    public void setRoom(int room) {
        this.room = room;
    }
    // Only for test cases
    public void setObjectsLocation(String tag, int location) {
        this.objectsLocation[getObject(tag)] = location;
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

        if ("sha".equalsIgnoreCase(noun)) {
            noun = "sal";
        }
        if ("for".equalsIgnoreCase(noun)) {
            noun = "bot";
        }
        return new String[] {verb, noun};
    }

    public String handle(String text) {
        return handle(parser(text));
    }
    public String handle(String[] verbNoun) {
        return handle(verbNoun[0], verbNoun[1]);
    }
    // Lines 2000-
    public String handle(String verb, String noun) {
        if (verb.equalsIgnoreCase("GO") || verb.equalsIgnoreCase("WALK")) { // go, walk
            return handleGo(noun);
        }
        if (verb.equalsIgnoreCase("GET") || verb.equalsIgnoreCase("TAK")) { // get, take
            return handleGet(noun);
        }
        if (verb.equalsIgnoreCase("DRO") || verb.equalsIgnoreCase("THR")) { // drop, throw
            return handleDrop(noun);
        }
        if (verb.equalsIgnoreCase("INV") || verb.equalsIgnoreCase("I")) { // inventory
            return handleInventory();
        }
        if (verb.equalsIgnoreCase("LOO") || verb.equalsIgnoreCase("L")) { // look
            return handleLook(noun);
        }
        if (verb.equalsIgnoreCase("EXA")) { // examine
            return handleExamine(noun);
        }
        if (verb.equalsIgnoreCase("REA")) { // read
            return handleRead(noun);
        }
        if (verb.equalsIgnoreCase("OPE")) { // open
            return handleOpen(noun);
        }
        if (verb.equalsIgnoreCase("POU")) { // pour
            return handlePour(noun);
        }

        if (verb.equalsIgnoreCase("CLI")) { // climb
            return handleClimb(noun);
        }
        if (verb.equalsIgnoreCase("JUM")) { // jump
            return handleJump(noun);
        }
        if (verb.equalsIgnoreCase("DIG")) { // dif
            return handleDig(noun);
        }
        if (verb.equalsIgnoreCase("ROW")) { // row
            return handleRow(noun);
        }
        if (verb.equalsIgnoreCase("WAV")) { // wave
            return handleWave(noun);
        }
        if (verb.equalsIgnoreCase("LEA")) { // leaf, exit
            return handleLeaf(noun);
        }
        if (verb.equalsIgnoreCase("FIG")) { // fight
            return handleFight(noun);
        }
        if (verb.equalsIgnoreCase("WEA")) { // wear
            return handleWear(noun);
        }

        return "I DON'T KNOW HOW TO DO THAT";
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
            return handleLook(null);
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
        return handleLook(null);
    }

    // Lines 2500-2600
    public String handleGet(String noun) {
        int objectId = getObject(noun);
        if (objectId == -1) {
            return "YOU CAN'T GET THAT! (no such object exists)"; // Object doesn't exist
        }
        int location = getLocationOfObject(noun);
        if (location == 0) {
            return "YOU CAN'T GET THAT!"; // No such object
        }
        if (location == -1) {
            return "YOU ALREADY HAVE IT!";
        }
        if (! canObjectBePickedUp(objectId)) {
            return "YOU CAN'T GET THAT! (unmovable object)";
        }
        if (location != this.room) {
            return "THAT'S NOT HERE!";
        }
        if (inventory.size() >= MAX_INVENTORY) {
            return "YOU CAN'T CARRY ANY MORE.";
        }

        inventory.add(objectId);
        objectsLocation[objectId] = -1;

        if (location == 18 && noun.equalsIgnoreCase("RUB")) {
            if (isWearingGloves) {
                return "CONGRATULATIONS! YOU'VE WON!";
            }
            return "";
        }

        return "TAKEN";
    }

    private boolean canObjectBePickedUp(int objectId) {
        return objectsLocation[objectId] < 127;
    }

    // Lines 2600-2700
    public String handleDrop(String noun) {
        int locationId = getLocationOfObject(noun);
        if (locationId != -1) {
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

    // Lines 2800-2900
    public String handleLook(String noun) {
        if (noun == null || noun.equals("")) {
            return getDescription().stream().collect(Collectors.joining("\n"));
        }
        return handleExamine(noun);
    }
    // Lines 2800-2900
    public String handleExamine(String noun) {
        if (noun.equalsIgnoreCase("GRO")) { // Ground
            if (room == 6) {
                return "IT LOOKS LIKE SOMETHING'S BURIED HERE.";
            }
            else {
                return "IT LOOKS LIKE GROUND.";
            }
        }

        int room = getLocationOfObject(noun);
        if (room == this.room || room == -1) {
            if ("BOT".equalsIgnoreCase(noun)) {
                return "THERE'S SOMETHING WRITTEN ON IT!";
            }
            if ("CAS".equalsIgnoreCase(noun)) {
                return "THERE'S A JEWEL INSIDE!";
            }
            if ("BAR".equalsIgnoreCase(noun)) {
                return "IT'S FILLED WITH RAIN WATER!";
            }
            return "YOU SEE NOTHING UNUSUAL.";
        }
        return "IT'S NOT HERE!";
    }

    // Lines 3500-3700
    public String handleRead(String noun) {
        if ("dia".equalsIgnoreCase(noun)) {
            int objectId = getObject("dia");
            if (objectsLocation[objectId] == this.room || objectsLocation[objectId] == -1) {
                return "IT SAYS: 'ADD SODIUM CHLORIDE PLUS THE"
                        + "\nFORMULA TO RAINWATER, TO REACH THE"
                        + "\nOTHER WORLD.'";
            }
            else {
                return "THERE'S NO DIARY HERE!";
            }
        }
        else if ("dic".equalsIgnoreCase(noun)) {
            int objectId = getObject("dic");
            if (objectsLocation[objectId] == this.room || objectsLocation[objectId] == -1) {
                return "IT SAYS: SODIUM CHLORIDE IS"
                        + "\nCOMMON TABLE SALT.";
            }
            else {
                return "YOU DON'T SEE A DICTIONARY!";
            }
        }
        else if ("bot".equalsIgnoreCase(noun)) {
            int objectId = getObject("bot");
            if (objectsLocation[objectId] == this.room || objectsLocation[objectId] == -1) {
                return "IT READS: 'SECRET FORMULA'.";
            }
            else {
                return "THERE'S NO BOTTLE HERE!";
            }
        }
        return "YOU CAN'T READ THAT!";
    }

    // Lines 3700-3900
    public String handleOpen(String noun) {
        if ("box".equalsIgnoreCase(noun)) {
            int objectId = getObject("box");
            if (objectsLocation[objectId] == this.room || objectsLocation[objectId] == -1) {
                setObjectsLocation("bot", this.room);
                return "SOMETHING FELL OUT!";
            }
            return "THERE'S NO BOX HERE!";
        }
        else if ("cab".equalsIgnoreCase(noun)) {
            int objectId = getObject("cab");
            int objLocation = getLocationOfObject("cab");
            if (objLocation == this.room) {
                objectsLocation[getObject("sal")] = 2;
                return "THERE'S SOMETHING INSIDE!";
            }
            return "THERE'S NO CABINET HERE!";
        }
        else if ("cas".equalsIgnoreCase(noun)) {
            if (isWearingGloves) {
                objectsLocation[getObject("rub")] = 18;
                return "THE GLOVES INSULATE AGAINST THE ELECTRICITY! THE CASE OPENS!";
            }
            return "THE CASE IS ELECTRIFIED!";
        }
        return "YOU CAN'T OPEN THAT!";
    }

    // Lines 3900-4100
    boolean isSaltPoured = false;
    boolean isBottlePoured = false;
    boolean isWearingGloves = false;
    int mixture = 1; // Magic mixture to reach the magic land, it should contain at least 3 ingredients (water, salt, ..)
    public String handlePour(String noun) {
        if (noun.equalsIgnoreCase("sal")) {
            int objectId = getObject("sal");
            if (objectsLocation[objectId] == this.room || objectsLocation[objectId] == -1) {
                if (isSaltPoured) {
                    return "THE SHAKER IS EMPTY!";
                }
                else {
                    isSaltPoured = true;
                    if (this.room == 5) {
                        mixture++;
                        if (mixture >= 3) {
                            this.room = 6;
                            return "THERE IS AN EXPLOSION!"
                                    + "\nEVERYTHING GOES BLACK!"
                                    + "\nSUDDENLY YOU ARE ..."
                                    + "\n... SOMEWHERE ELSE!";
                        }
                    }
                    return "POURED!";
                }
            }
            return "DON'T HAVE THE SALT!";
        }
        if (noun.equalsIgnoreCase("bot")) {
            int objectId = getObject("bot");
            if (objectsLocation[objectId] == this.room || objectsLocation[objectId] == -1) {
                if (isBottlePoured) {
                    return "THE BOTTLE IS EMPTY!";
                }
                else {
                    isBottlePoured = true;
                    if (this.room == 5) {
                        mixture++;
                        if (mixture >= 3) {
                            this.room = 6;
                            return "THERE IS AN EXPLOSION!"
                                    + "\nEVERYTHING GOES BLACK!"
                                    + "\nSUDDENLY YOU ARE ..."
                                    + "\n... SOMEWHERE ELSE!";
                        }
                    }
                    return "POURED!";
                }
            }
            return "YOU DON'T HAVE THE BOTTLE!";
        }
        if (mixture >= 3) {
            this.room = 6;
            return "THERE IS AN EXPLOSION!"
                    + "\nEVERYTHING GOES BLACK!"
                    + "\nSUDDENLY YOU ARE ..."
                    + "\n... SOMEWHERE ELSE!";
        }
        return "YOU CAN'T POUR THAT!";
    }

    // Lines 4100-4300
    public String handleClimb(String noun) {
        if ("tre".equalsIgnoreCase(noun)) { // tree
            int objLocation = getLocationOfObject("tre");
            if (objLocation == this.room) {
                return "YOU CAN'T REACH THE BRANCHES!";
            }
            return "THERE'S NO TREE HERE!";
        }
        if ("lad".equalsIgnoreCase(noun)) { // ladder
            int objectId = getObject("lad");
            if (objectsLocation[objectId] == this.room || objectsLocation[objectId] == -1) {
                if (this.room == 7) {
                    objectsLocation[objectId] = 0;
                    inventory.remove(Integer.valueOf(getObject("lad")));
                    return "THE LADDER SINKS UNDER YOUR WEIGHT!"
                            + "\nIT DISAPPEARS INTO THE GROUND!";
                }
                else {
                    return "WHATEVER FOR?";
                }
            }
            return "YOU DON'T HAVE THE LADDER!";
        }
        return "IT WON'T DO ANY GOOD.";
    }

    // Lines 4300-4400
    public String handleJump(String noun) {
        if (this.room == 7) {
            this.room = 8;
            return "YOU GRAB THE LOWEST BRANCH OF THE"
                    + "\nTREE AND PULL YOURSELF UP ...."
                    + "\n" + handleLook(null);
        }
        else if (this.room == 8) {
            this.room = 19;
            return "YOU GRAB A HIGHER BRANCH ON THE"
                    + "\nTREE AND PULL YOURSELF UP ...."
                    + "\n" + handleLook(null);
        }
        return "WHEE! THAT WAS FUN!";
    }

    // Lines 4400-4500
    public String handleDig(String noun) {
        if ("gro".equalsIgnoreCase(noun) || "hol".equalsIgnoreCase(noun) || "".equals(noun)) {
            int objectId = getObject("sho");
            if (objectsLocation[objectId] == this.room || objectsLocation[objectId] == -1) {
                if (this.room == 6) {
                    int swordId = getObject("swo");
                    if (objectsLocation[swordId] == 0) {
                        objectsLocation[swordId] = this.room;
                        return "THERE'S SOMETHING THERE!";
                    }
                    return "THERE'S NOTHING ELSE THERE!";
                }
                return "YOU DON'T FIND ANYTHING.";
            }
            return "YOU DON'T HAVE A SHOVEL!";
        }
        return "YOU CAN'T DIG THAT!";
    }

    // Lines 45--4600
    public String handleRow(String noun) {
        if ("boa".equalsIgnoreCase(noun)) {
            int objectId = getObject("boa");
            if (this.room == 13) {
                return "YOU DON'T HAVE AN OAR!";
            }
            return "YOU'RE NOT IN THE BOAT!";
        }
        return "HOW CAN YOU ROW THAT?";
    }

    // Lines 4600-4700
    public String handleWave(String noun) {
        if ("fan".equalsIgnoreCase(noun)) {
            int objectId = getObject("fan");
            if (objectsLocation[objectId] == this.room || objectsLocation[objectId] == -1) {
                if (this.room == 13) {
                    int boatId = getObject("boa");
                    if (objectsLocation[boatId] == 140) {
                        objectsLocation[boatId] = 142;
                    }
                    else {
                        objectsLocation[boatId] = 140;
                    }
                    return "A POWERFUL BREEZE PROPELS THE BOAT"
                            + "\nTO THE OPPOSITE SHORE!";
                }
                return "YOU FEEL A REFRESHING BREEZE!";
            }
            return "YOU DON'T HAVE THE FAN!";
        }
        return "YOU CAN'T WAVE THAT!";
    }

    // Lines 4700-4800
    public String handleLeaf(String noun) {
        if (room == 13) {
            if ("boa".equalsIgnoreCase(noun) || "".equals(noun)) {
                this.room = objectsLocation[getObject("boa")] - 128; // room 12 or 14
                return handleLook(null);
            }
            else {
                return "HUH?";
            }
        }
        return "PLEASE GIVE A DIRECTION!";
    }

    // Lines 4800-4900
    public String handleFight(String noun) {
        if ("".equals(noun)) {
            return "WHOM DO YOU WANT TO FIGHT?";
        }
        if ("gua".equalsIgnoreCase(noun)) {
            if (this.room == getLocationOfObject("gua")) {
                if (objectsLocation[getObject("swo")] == -1) {
                    roomDirections[getLocationOfObject("gua")][0] = 17;
                    objectsLocation[getObject("gua")] = 0;
                    return "THE GUARD, NOTICING YOUR SWORD,"
                            + "\nWISELY RETREATS INTO THE CASTLE.";
                }
                return "YOU DON'T HAVE A WEAPON!";
            }
            return "THERE'S NO GUARD HERE!";
        }
        return "YOU CAN'T FIGHT HIM!";
    }

    // Lines 4900-5000
    public String handleWear(String noun) {
        if ("glo".equalsIgnoreCase(noun)) {
            int objectId = getObject("glo");
            if (objectsLocation[objectId] == this.room || objectsLocation[objectId] == -1) {
                isWearingGloves = true;
                return "YOU ARE NOW WEARING THE GLOVES.";
            }
            return "YOU DON'T HAVE THE GLOVES.";
        }
        return "YOU CAN'T WEAR THAT!";
    }


    // Lines 1000-1050
    /** Return the object array index.
     * The returned value for the given tag is:
     * > 0 : The tag is an object in the game, that is located in one of the rooms
     *   0 : The object is not yet discovered (still hidden) or does not exist at all.
     *  -1 : The object is stored in the inventory of the player.
     */
    public int getObject(String tag) {
        for (int index = 0; index < objectsTags.length; index++) {
            if (objectsTags[index].equalsIgnoreCase(tag)) {
                return index;
            }
        }
        return -2;
    }
    public int getLocationOfObject(String tag) {
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
          , "IN YOUR LIVING ROOM." // 1
          , "IN THE KITCHEN."      // 2
          , "IN THE LIBRARY."      // 3
          , "IN THE FRONT YARD."   // 4
          , "IN THE GARAGE."       // 5
          , "IN AN OPEN FIELD."    // 6
          , "AT THE EDGE OF A FOREST." // 7
          , "ON A BRANCH OF A TREE."   // 8
          , "ON A LONG, WINDING ROAD." // 9
          , "ON A LONG, WINDING ROAD." // 10
          , "ON A LONG, WINDING ROAD." // 11
          , "ON THE SOUTH BANK OF A RIVER." // 12
          , "INSIDE THE WOODEN BOAT."       // 13
          , "ON THE NORTH BANK OF A RIVER." // 14
          , "ON A WELL-TRAVELED ROAD."      // 15
          , "IN FRONT OF A LARGE CASTLE."   // 16
          , "IN A NARROW HALL."     // 17
          , "IN A LARGE HALL."      // 18
          , "ON THE TOP OF A TREE." // 19
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
                "AN OLD DIARY, DIA, 1"    // 0
                , "A SMALL BOX, BOX, 1"   // 1
                , "CABINET, CAB, 130"     // 2
                , "A SALT SHAKER, SAL, 0" // 3
                , "A DICTIONARY, DIC, 3"  // 4
                , "WOODEN BARREL, BAR, 133" // 5
                , "A SMALL BOTTLE, BOT, 0"  // 6
                , "A LADDER, LAD, 4"  // 7
                , "A SHOVEL, SHO, 5"  // 8
                , "A TREE, TRE, 135"  // 9
                , "A GOLDEN SWORD, SWO, 0"  // 10
                , "A WOODEN BOAT, BOA, 140" // 11
                , "A MAGIC FAN, FAN, 8"     // 12
                , "A NASTY - LOOKING GUARD, GUA, 144" // 13
                , "A GLASS CASE, CAS, 146"  // 14
                , "A GLOWING RUBY, RUB, 0"  // 15
                , "A PAIR OF RUBBER GLOVES, GLO, 19"  // 16
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

    public String introduction() {
        return Arrays.stream(new String[] {
                "ALL YOUR LIFE YOU HAD HEARD THE STORIES"
                , "ABOUT YOUR CRAZY UNCLE SIMON. HE WAS AN"
                , "INVENTOR, WHO KEPT DISAPPEARING FOR"
                , "LONG PERIODS OF TIME, NEVER TELLING"
                , "ANYONE WHERE HE HAD BEEN."
                , ""
                , "YOU NEVER BELIEVED THE STORIES, BUT"
                , "WHEN YOUR UNCLE DIED AND LEFT YOU HIS"
                , "DIARY, YOU LEARNED THAT THEY WERE TRUE."
                , "YOUR UNCLE HAD DISCOVERED A MAGIC"
                , "LAND, AND A SECRET FORMULA THAT COULD"
                , "TAKE HIM THERE. IN THAT LAND WAS A"
                , "MAGIC RUBY, AND HIS DIARY CONTAINED"
                , "THE INSTRUCTIONS FOR GOING THERE TO"
                , "FIND IT."
                , ""
                , "PRESS RETURN TO BEGIN"
        }).collect(Collectors.joining("\n"));
    }
}
