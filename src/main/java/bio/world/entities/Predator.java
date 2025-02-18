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
        Optional<Herbivore> target = findNearestTarget(worldMap, pathFinder);
        if (target.isEmpty()) {
            return;
        }
        Herbivore herbivore = target.get();
        Coordinates nextCoordinates = this.coordinates;
        if (canEat(herbivore)) {
            herbivore.takeDamage(this);
            if (!herbivore.isAlive()) {
                worldMap.removeCreature(herbivore);
                nextCoordinates = herbivore.getCoordinates();
            }
        } else {
            List<Coordinates> pathToTarget = pathFinder.find(this.coordinates, herbivore.getCoordinates());
            if (pathToTarget.isEmpty()) {
                return;
            }
            nextCoordinates = pathToTarget.get(0);
        }
        worldMap.moveCreature(this.coordinates, nextCoordinates);
        this.setCoordinates(nextCoordinates);
    }

    private Optional<Herbivore> findNearestTarget(WorldMap worldMap, PathFinder pathFinder) {
        List<Herbivore> possibleTargetList = createListOfTarget(worldMap);
        List<Herbivore> targetPriorityList = possibleTargetList
                .stream()
                .sorted((t1, t2) -> calculateApproximateDistance(this.coordinates, t1.getCoordinates()) - calculateApproximateDistance(this.coordinates, t2.getCoordinates()))
                .toList();
        for (Herbivore herbivore : targetPriorityList) {
            List<Coordinates> path = pathFinder.find(this.coordinates, herbivore.getCoordinates());
            if (!path.isEmpty() && path.get(path.size() - 1).equals(herbivore.getCoordinates())) {
                return Optional.of(herbivore);
            }
        }
        return Optional.empty();
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
        return rowDiff < 2 && columnDiff < 2;
    }

    @Override
    public int getDamage() {
        return 10;
    }
}