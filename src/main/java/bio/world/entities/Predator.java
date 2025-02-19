package bio.world.entities;

import bio.world.Coordinates;
import bio.world.WorldMap;
import bio.world.path_finders.PathFinder;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class Predator extends Creature implements Hunter<Herbivore> {
    private int attackPower;
    private final Predicate<Entity> filter;
    private final Function<Entity, Herbivore> mapper;

    public Predator(Coordinates coordinates) {
        super(coordinates);
        this.filter = e -> e instanceof Herbivore;
        this.mapper = e -> (Herbivore) e;
    }

    @Override
    public void makeMove(WorldMap worldMap, PathFinder pathFinder) {
//        System.out.println();
//        System.out.println("Predator move: " + this.coordinates);
        Optional<Herbivore> target = findNearestTarget(worldMap, pathFinder);
        if (target.isEmpty()) {
            return;
        }
        Herbivore herbivore = target.get();
        Coordinates nextCoordinates = this.coordinates;
//        System.out.println("target = " + herbivore.getCoordinates());
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
//        System.out.println("possibleTargetList:");
//        printEntities(possibleTargetList);
        List<Herbivore> targetPriorityList = possibleTargetList
                .stream()
                .sorted((t1, t2) -> calculateApproximateDistance(this.coordinates, t1.getCoordinates()) - calculateApproximateDistance(this.coordinates, t2.getCoordinates()))
                .toList();
//        System.out.println("targetPriorityList:");
//        printEntities(targetPriorityList);
        for (Herbivore herbivore : targetPriorityList) {
            List<Coordinates> path = pathFinder.find(this.coordinates, herbivore.getCoordinates());
            if (!path.isEmpty() && path.get(path.size() - 1).equals(herbivore.getCoordinates())) {
                return Optional.of(herbivore);
            }
        }
        return Optional.empty();
    }

    private List<Herbivore> createListOfTarget(WorldMap worldMap) {
        Set<Creature> creatures = worldMap.getCreatures();
        List<Herbivore> possibleTargetList = creatures
                .stream()
                .filter(this.filter)
                .map(this.mapper)
                .toList();
        return possibleTargetList;
    }

    @Override
    public int getDamage() {
        return 10;
    }
}