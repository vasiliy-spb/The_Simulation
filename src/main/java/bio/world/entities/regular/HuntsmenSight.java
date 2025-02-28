package bio.world.entities.regular;

import bio.world.entities.Coordinates;
import bio.world.entities.Entity;
import bio.world.entities.temporary.Flash;
import bio.world.map.WorldMap;

import java.util.Set;

public class HuntsmenSight {
    private static final Set<Class<? extends Entity>> NOT_BARRIER_TYPES = Set.of(Grass.class, Herbivore.class, Predator.class, Flash.class);
    private static final int[][] LINE_OFFSETS = {{0, 1}, {1, 1}, {1, 0}};

    public boolean hasBarrierBetween(Coordinates fromCoordinates, Coordinates toCoordinates, WorldMap worldMap) {
        int row1 = fromCoordinates.row();
        int column1 = fromCoordinates.column();
        int row2 = toCoordinates.row();
        int column2 = toCoordinates.column();

        int diffRow = row2 - row1;
        int diffColumn = column2 - column1;

        int signRowOffset = 1;
        int signColumnOffset = 1;

        if (diffRow < 0) {
            signRowOffset = -1;
        }
        if (diffColumn < 0) {
            signColumnOffset = -1;
        }

        diffRow *= signRowOffset;
        diffColumn *= signColumnOffset;

        /*
         🤠 ++ ++ 🐅
         .. .. .. ..
         .. .. .. ..
         .. .. .. ..
         */
        if (diffRow == 0) {
            return hasBarrierInLine(fromCoordinates, toCoordinates, LINE_OFFSETS[0], signRowOffset, signColumnOffset, worldMap);
        }

        /*
         🤠 .. .. ..
         .. ++ .. ..
         .. .. ++ ..
         .. .. .. 🐅
         */
        if (diffRow == diffColumn) {
            return hasBarrierInLine(fromCoordinates, toCoordinates, LINE_OFFSETS[1], signRowOffset, signColumnOffset, worldMap);
        }

        /*
         🤠 .. .. ..
         ++ .. .. ..
         ++ .. .. ..
         🐅 .. .. ..
         */
        if (diffColumn == 0) {
            return hasBarrierInLine(fromCoordinates, toCoordinates, LINE_OFFSETS[2], signRowOffset, signColumnOffset, worldMap);
        }

        /*
         🤠 ++ .. ..
         .. ++ 🐅 ..
         .. .. .. ..
         .. .. .. ..
         */
        if (diffRow == 1 && diffColumn == 2) {
            int[][] offsets = {{0, 1}, {1, 1}};
            return hasBarrierBy(fromCoordinates, offsets, signRowOffset, signColumnOffset, worldMap);
        }

        /*
         🤠 .. .. ..
         ++ ++ .. ..
         .. 🐅 .. ..
         .. .. .. ..
         */
        if (diffRow == 2 && diffColumn == 1) {
            int[][] offsets = {{1, 0}, {1, 1}};
            return hasBarrierBy(fromCoordinates, offsets, signRowOffset, signColumnOffset, worldMap);
        }

        /*
         🤠 ++ .. ..
         .. .. ++ 🐅
         .. .. .. ..
         .. .. .. ..
         */
        if (diffRow == 1 && diffColumn == 3) {
            int[][] offsets = {{0, 1}, {1, 2}};
            return hasBarrierBy(fromCoordinates, offsets, signRowOffset, signColumnOffset, worldMap);
        }

        /*
         🤠 .. .. ..
         ++ .. .. ..
         .. ++ .. ..
         .. 🐅 .. ..
         */
        if (diffRow == 3 && diffColumn == 1) {
            int[][] offsets = {{1, 0}, {2, 1}};
            return hasBarrierBy(fromCoordinates, offsets, signRowOffset, signColumnOffset, worldMap);
        }

        /*
         🤠 .. .. ..
         .. ++ ++ ..
         .. .. ++ 🐅
         .. .. .. ..
         */
        if (diffRow == 2 && diffColumn == 3) {
            int[][] offsets = {{1, 1}, {1, 2}, {2, 2}};
            return hasBarrierBy(fromCoordinates, offsets, signRowOffset, signColumnOffset, worldMap);
        }

        /*
         🤠 .. .. ..
         .. ++ .. ..
         .. ++ ++ ..
         .. .. 🐅 ..
         */
        if (diffRow == 3 && diffColumn == 2) {
            int[][] offsets = {{1, 1}, {2, 1}, {2, 2}};
            return hasBarrierBy(fromCoordinates, offsets, signRowOffset, signColumnOffset, worldMap);
        }

        return true;
    }

    private boolean hasBarrierInLine(Coordinates fromCoordinates, Coordinates toCoordinates, int[] lineOffset, int signRowOffset, int signColumnOffset, WorldMap worldMap) {
        int row = fromCoordinates.row() + lineOffset[0] * signRowOffset;
        int column = fromCoordinates.column() + lineOffset[1] * signColumnOffset;
        Coordinates coordinates = new Coordinates(row, column);
        while (!coordinates.equals(toCoordinates)) {
            if (worldMap.areBusy(coordinates)) {
                Entity entity = worldMap.getEntityByCoordinates(coordinates);
                if (!NOT_BARRIER_TYPES.contains(entity.getClass())) {
                    return true;
                }
            }
            row += lineOffset[0] * signRowOffset;
            column += lineOffset[1] * signColumnOffset;
            coordinates = new Coordinates(row, column);
        }
        return false;
    }

    private boolean hasBarrierBy(Coordinates sourceCoordinates, int[][] offsets, int signRowOffset, int signColumnOffset, WorldMap worldMap) {
        for (int[] offset : offsets) {
            int row = sourceCoordinates.row() + offset[0] * signRowOffset;
            int column = sourceCoordinates.column() + offset[1] * signColumnOffset;
            Coordinates coordinates = new Coordinates(row, column);
            if (worldMap.areBusy(coordinates)) {
                Entity entity = worldMap.getEntityByCoordinates(coordinates);
                if (!NOT_BARRIER_TYPES.contains(entity.getClass())) {
                    return true;
                }
            }
        }
        return false;
    }
}
