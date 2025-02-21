package bio.world.entities;

import bio.world.Coordinates;
import bio.world.WorldMap;
import bio.world.path_finders.PathFinder;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Predator extends Creature implements Hunter<Herbivore> {
    private final int attackPower;
    private final Comparator<Herbivore> priorityTargetComparator;

    public Predator(Coordinates coordinates) {
        super(coordinates);
        this.priorityTargetComparator = (t1, t2) ->
                calculateApproximateDistance(this.coordinates, t1.getCoordinates()) -
                        calculateApproximateDistance(this.coordinates, t2.getCoordinates());
        this.healthPoint = 30;
        this.turnFrequency = 3;
        this.attackPower = 10;
    }

    @Override
    public void makeMove(WorldMap worldMap, PathFinder pathFinder) {
        Optional<Herbivore> target = findNearestTarget(worldMap, pathFinder);
        if (target.isEmpty()) {
            makeRandomStep(worldMap, pathFinder);
            return;
        }
        Herbivore herbivore = target.get();
        Coordinates nextCoordinates = this.coordinates;
        if (canEat(herbivore)) {
            herbivore.takeDamage(this);
            if (!herbivore.isAlive()) {
                worldMap.removeEntity(herbivore);
                nextCoordinates = herbivore.getCoordinates();
            }
        } else {
            List<Coordinates> pathToTarget = pathFinder.find(this.coordinates, herbivore.getCoordinates());
            if (pathToTarget.isEmpty()) {
                return;
            }
            nextCoordinates = pathToTarget.get(0);
        }
        worldMap.moveEntity(this.coordinates, nextCoordinates);
        this.setCoordinates(nextCoordinates);
    }

    private Optional<Herbivore> findNearestTarget(WorldMap worldMap, PathFinder pathFinder) {
        List<Herbivore> herbivores = worldMap.getHerbivores();
        herbivores = herbivores.stream()
                .sorted(this.priorityTargetComparator)
                .toList();
        for (Herbivore herbivore : herbivores) {
            if (hasPathFor(herbivore, pathFinder)) {
                return Optional.of(herbivore);
            }
        }
        return Optional.empty();
    }

    @Override
    public int getDamage() {
        return attackPower;
    }
}