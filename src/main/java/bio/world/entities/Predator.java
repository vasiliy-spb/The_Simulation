package bio.world.entities;

import bio.world.Coordinates;
import bio.world.WorldMap;
import bio.world.path_finders.PathFinder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Predator extends Creature implements Hunter<Herbivore> {
    private int attackPower;

    public Predator(Coordinates coordinates) {
        super(coordinates);
    }

    @Override
    public void makeMove(WorldMap worldMap, PathFinder pathFinder) {
//        System.out.println();
//        System.out.println("Predator move: " + this.coordinates);
        Optional<Herbivore> target = findNearestTarget(worldMap, pathFinder);
        if (target.isEmpty()) {
//            System.out.println("Target not found..");
            return;
        }
        Herbivore herbivore = target.get();
//        System.out.println("target = " + herbivore.getCoordinates());
        Coordinates nextCoordinates = this.coordinates;
        if (canEat(herbivore)) {
//            System.out.println("Predator eat Herbivore: " + herbivore.getCoordinates());
            herbivore.takeDamage(this);
            if (!herbivore.isAlive()) {
                worldMap.removeCreature(herbivore);
                nextCoordinates = herbivore.getCoordinates();
            }
        } else {
            List<Coordinates> pathToTarget = pathFinder.find(this.coordinates, herbivore.getCoordinates());
//            System.out.println("pathToTarget = " + pathToTarget);
            if (pathToTarget.isEmpty()) {
                return;
            }
            nextCoordinates = pathToTarget.get(0);
        }
//        System.out.println("nextCoordinates = " + nextCoordinates);
        worldMap.moveCreature(this.coordinates, nextCoordinates);
        this.setCoordinates(nextCoordinates);
    }

    private Optional<Herbivore> findNearestTarget(WorldMap worldMap, PathFinder pathFinder) {
        List<Herbivore> possibleTargetList = createListOfTarget(worldMap);
//        System.out.println("possibleTargetList:");
//        printTargets(possibleTargetList);
        List<Herbivore> targetPriorityList = possibleTargetList
                .stream()
                .sorted((t1, t2) -> calculateApproximateDistance(this.coordinates, t1.getCoordinates()) - calculateApproximateDistance(this.coordinates, t2.getCoordinates()))
                .toList();
//        System.out.println("targetPriorityList:");
//        printTargets(targetPriorityList);
        for (Herbivore herbivore : targetPriorityList) {
            List<Coordinates> path = pathFinder.find(this.coordinates, herbivore.getCoordinates());
//            System.out.println("herbivore = " + herbivore.getCoordinates());
//            System.out.println("path = " + path);
            if (!path.isEmpty() && path.get(path.size() - 1).equals(herbivore.getCoordinates())) {
                return Optional.of(herbivore);
            }
        }
        return Optional.empty();
    }

    private void printTargets(List<Herbivore> possibleTargetList) {
        for (Herbivore herbivore : possibleTargetList) {
            System.out.print(herbivore.getCoordinates() + ", ");
        }
        System.out.println();
    }

    private int calculateApproximateDistance(Coordinates from, Coordinates target) {
        return Math.abs(from.row() - target.row()) + Math.abs(from.column() - target.column());
    }

    private static List<Herbivore> createListOfTarget(WorldMap worldMap) {
        Set<Creature> creatures = worldMap.getCreatures();
        List<Herbivore> possibleTargetList = creatures
                .stream()
                .filter(e -> e instanceof Herbivore)
                .map(e -> (Herbivore) e)
                .toList();
        return possibleTargetList;
    }

    private boolean canEat(Herbivore herbivore) {
        int rowDiff = Math.abs(this.coordinates.row() - herbivore.getCoordinates().row());
        int columnDiff = Math.abs(this.coordinates.column() - herbivore.getCoordinates().column());
//        System.out.println("rowDiff = " + rowDiff);
//        System.out.println("columnDiff = " + columnDiff);
        return rowDiff < 2 && columnDiff < 2;
    }

    @Override
    public int getDamage() {
        return 10;
    }
}