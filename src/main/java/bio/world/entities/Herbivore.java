package bio.world.entities;

import bio.world.Coordinates;
import bio.world.WorldMap;
import bio.world.path_finders.PathFinder;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Herbivore extends Creature implements Hunter<Grass>, Prey<Predator> {
    private final int powerAttack;
    private final Comparator<Grass> priorityTargetComparator;
    public Herbivore(Coordinates coordinates) {
        super(coordinates);
        this.priorityTargetComparator = (t1, t2) ->
                calculateApproximateDistance(this.coordinates, t1.getCoordinates()) -
                        calculateApproximateDistance(this.coordinates, t2.getCoordinates());
        this.healthPoint = 20;
        this.turnFrequency = 2;
        this.powerAttack = 10;
    }

    @Override
    public void makeMove(WorldMap worldMap, PathFinder pathFinder) {
        if (!isAlive()) {
            worldMap.removeEntity(this);
            return;
        }
        Optional<Grass> target = findNearestTarget(worldMap, pathFinder);
        if (target.isEmpty()) {
            return;
        }
        Grass grass = target.get();
        Coordinates nextCoordinates;
        if (canEat(grass)) {
            nextCoordinates = grass.getCoordinates();
            grass.takeDamage(this);
            worldMap.removeEntity(grass);
        } else {
            List<Coordinates> pathToTarget = pathFinder.find(this.coordinates, grass.getCoordinates());
            if (pathToTarget.isEmpty()) {
                return;
            }
            nextCoordinates = pathToTarget.get(0);
        }
        worldMap.moveEntity(this.coordinates, nextCoordinates);
        this.setCoordinates(nextCoordinates);
    }

    private Optional<Grass> findNearestTarget(WorldMap worldMap, PathFinder pathFinder) {
        List<Grass> grasses = worldMap.getGrasses();
        grasses = grasses.stream()
                .sorted(this.priorityTargetComparator)
                .toList();
        for (Grass grass : grasses) {
            if (hasPathFor(grass, pathFinder)) {
                return Optional.of(grass);
            }
        }
        return Optional.empty();
    }

    @Override
    public int getDamage() {
        return powerAttack;
    }

    public boolean isAlive() {
        return this.healthPoint > 0;
    }

    @Override
    public void takeDamage(Predator hunter) {
        this.healthPoint -= hunter.getDamage();
    }
}
