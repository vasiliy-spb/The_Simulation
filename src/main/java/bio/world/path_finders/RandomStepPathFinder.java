package bio.world.path_finders;

import bio.world.Coordinates;
import bio.world.WorldMap;

import java.util.List;
import java.util.Random;

public class RandomStepPathFinder implements PathFinder {
    private final WorldMap worldMap;
    private static final int[][] DIRECTIONS = {{-1, -1}, {-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}};

    public RandomStepPathFinder(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    @Override
    public List<Coordinates> find(Coordinates fromCoordinates, Coordinates toCoordinates) {
//        System.out.print("From: " + fromCoordinates.getPosition());
        Random random = new Random();
        List<Coordinates> path;
        if (!isMovePossible(fromCoordinates)) {
            path = List.of(fromCoordinates);
//            System.out.println("Move not possible..");
//            System.out.println("worldMap = " + worldMap.getBusyCoordinates());
//            System.out.print("To: " + fromCoordinates.getPosition());
            return path;
        }
        Coordinates nextCoordinates;
        do {
            int directionIndex = random.nextInt(DIRECTIONS.length);
            int[] direction = DIRECTIONS[directionIndex];
            int row = fromCoordinates.row() + direction[0];
            int column = fromCoordinates.column() + direction[1];
            nextCoordinates = new Coordinates(row, column);
        } while (!areCoordinatesExist(nextCoordinates) || worldMap.areBusy(nextCoordinates));
        path = List.of(nextCoordinates);
//        System.out.print("To: " + nextCoordinates.getPosition());
        return path;
    }

    private boolean isMovePossible(Coordinates fromCoordinates) {
        for (int[] direction : DIRECTIONS) {
            int row = fromCoordinates.row() + direction[0];
            int column = fromCoordinates.column() + direction[1];
            Coordinates toCoordinates = new Coordinates(row, column);
            if (areCoordinatesExist(toCoordinates) && !worldMap.areBusy(toCoordinates)) {
                return true;
            }
        }
        return false;
    }

    private boolean areCoordinatesExist(Coordinates coordinates) {
        int row = coordinates.row();
        int column = coordinates.column();
        int height = worldMap.getHeight();
        int width = worldMap.getWidth();
        return row >= 0 && row < height && column >= 0 && column < width;
    }
}
