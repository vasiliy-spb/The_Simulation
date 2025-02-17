package bio.world.render;

import bio.world.Coordinates;
import bio.world.WorldMap;
import bio.world.entities.*;

import java.util.Map;
import java.util.Set;

public class ConsoleMapRender implements WorldMapRender {
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String RESET = "\033[0m";  // Text Reset

    private WorldMap worldMap;

    public ConsoleMapRender(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    @Override
    public void renderMap() {
        int height = worldMap.getHeight();
        int width = worldMap.getWidth();
        Set<Coordinates> busyCoordinates = worldMap.getBusyCoordinates();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Coordinates coordinates = new Coordinates(i, j);
                if (!busyCoordinates.contains(coordinates)) {
                    System.out.print(GREEN + " .." + RESET);
                    continue;
                }
                Entity entity = worldMap.getEntityByCoordinates(coordinates);

                String picture = "";
                if (entity instanceof Grass) {
                    picture = Pictures.GRASS.getValue();
                } else if (entity instanceof Rock) {
                    picture = Pictures.STONE.getValue();
                } else if (entity instanceof Tree) {
                    picture = Pictures.SPRUCE_TREE.getValue();
                }

//                if (entity instanceof Grass) {
//                    picture = Pictures.GRASS.getValue();
//                } else if (entity instanceof Herbivore) {
//                    picture = Pictures.RABBIT.getValue();
//                } else if (entity instanceof Predator) {
//                    picture = Pictures.TIGER.getValue();
//                } else if (entity instanceof Rock) {
//                    picture = Pictures.STONE.getValue();
//                } else if (entity instanceof Tree) {
//                    picture = Pictures.SPRUCE_TREE.getValue();
//                } else if (entity instanceof Camp) {
//                    picture = Pictures.CAMP.getValue();
//                } else if (entity instanceof Huntsman) {
//                    picture = Pictures.HUNTSMAN.getValue();
//                } else if (entity instanceof Flash) {
//                    picture = Pictures.FLASH.getValue();
//                } else if (entity instanceof Trap trap) {
//                    String creaturePicture = "";
//                    if (!trap.isEmpty()) {
//                        if (trap.getCaughtCreatureOptional() instanceof Predator) {
//                            creaturePicture = Pictures.TIGER.getValue().trim();
//                        }
//                        if (trap.getCaughtCreatureOptional() instanceof Herbivore) {
//                            creaturePicture = Pictures.RABBIT.getValue();
//                        }
//                        picture = Pictures.TRAP_OPEN.getValue() + creaturePicture + Pictures.TRAP_CLOSE.getValue();
//                    } else {
//                        picture = Pictures.TRAP_EMPTY.getValue();
//                    }
//                }
                System.out.print(" " + picture);
            }
            System.out.println();
        }
    }
}
