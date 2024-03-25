package com.github.progdojo.adventure;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private String[] rooms = initializeRooms();
    private Map<Integer,List<Integer>> roomConnections = initializeRoomConnections();
    private Integer[][] roomDirections = initializeRoomDirections(initializeRoomConnections());

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
}
