package com.github.progdojo.adventure;

public class TextAdventure {
    private String[] rooms = initializeRooms();

    // NR - number of rooms

    // 700
    /** Room description routine */
    public String roomDescription(int room) {
        return ("YOU ARE %s".formatted(rooms[room]));
    }


    // 27000 - 29900
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

    public int getNumberOfRooms() {
        return rooms.length;
    }
}
