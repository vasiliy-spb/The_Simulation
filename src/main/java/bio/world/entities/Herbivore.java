package bio.world.entities;

import bio.world.Coordinates;
import bio.world.WorldMap;
import bio.world.path_finders.PathFinder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Herbivore extends Creature implements Hunter<Grass> {
    public Herbivore(Coordinates coordinates) {
        super(coordinates);
    }

    @Override
    public void makeMove(WorldMap worldMap, PathFinder pathFinder) {
//        System.out.println();
//        System.out.println(this.coordinates + " makeMove()");
        Optional<Grass> target = findNearestTarget(worldMap, pathFinder);
        if (target.isEmpty()) {
//            System.err.println("Herbivore: " + this.coordinates + " didn't find Grass.");
//            throw new RuntimeException("Herbivore didn't find Grass.");
            return;
        }
        Grass grass = target.get();
//        System.out.println("Nearest target for: " + this.coordinates + " >>> " + grass.getCoordinates());
        Coordinates nextCoordinates;
        if (canEat(grass)) {
            grass.takeDamage(this);
            nextCoordinates = grass.getCoordinates();
            worldMap.removeStaticEntity(grass);
        } else {
            List<Coordinates> path = pathFinder.find(this.coordinates, grass.getCoordinates());
            if (path.isEmpty()) {
                return;
            }
            nextCoordinates = path.get(0);
        }
//        System.out.println("nextCoordinates = " + nextCoordinates);
        worldMap.moveCreature(this.coordinates, nextCoordinates);
        this.setCoordinates(nextCoordinates);
    }

    private Optional<Grass> findNearestTarget(WorldMap worldMap, PathFinder pathFinder) {
        List<Grass> possibleTargetList = createListOfTarget(worldMap);
//        System.out.println("possibleTargetList = ");
//        printEntities(possibleTargetList);
        List<Grass> targetPriorityList = possibleTargetList
                .stream()
                .sorted((t1, t2) -> calculateApproximateDistance(this.coordinates, t1.getCoordinates()) - calculateApproximateDistance(this.coordinates, t2.getCoordinates()))
                .toList();
//        System.out.println("targetPriorityList = ");
//        printEntities(targetPriorityList);
        for (Grass grass : targetPriorityList) {
            List<Coordinates> path = pathFinder.find(this.coordinates, grass.getCoordinates());
//            System.out.println("path to: " + grass.getCoordinates() + " = " + path);
            if (!path.isEmpty() && path.get(path.size() - 1).equals(grass.getCoordinates())) {
                return Optional.of(grass);
            }
        }
        return Optional.empty();
    }

    private void printEntities(List<Grass> entityList) {
        for (Grass grass : entityList) {
            System.out.print(grass.getClass().getSimpleName() + " " + grass.getCoordinates() + " ");
        }
        System.out.println();
    }

    private int calculateApproximateDistance(Coordinates from, Coordinates target) {
        return Math.abs(from.row() - target.row()) + Math.abs(from.column() - target.column());
    }

    private static List<Grass> createListOfTarget(WorldMap worldMap) {
        Set<StaticEntity> staticEntities = worldMap.getStaticEntities();
        List<Grass> possibleTargetList = staticEntities
                .stream()
                .filter(e -> e instanceof Grass)
                .map(e -> (Grass) e)
                .toList();
        return possibleTargetList;
    }

    private boolean canEat(Grass grass) {
        int rowDiff = Math.abs(this.coordinates.row() - grass.getCoordinates().row());
        int columnDiff = Math.abs(this.coordinates.column() - grass.getCoordinates().column());
        return rowDiff < 2 && columnDiff < 2;
    }

    @Override
    public int getDamage() {
        return 10;
    }
}
