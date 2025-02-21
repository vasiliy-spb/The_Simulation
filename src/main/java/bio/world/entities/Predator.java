package bio.world.entities;

import bio.world.Coordinates;
import bio.world.WorldMap;
import bio.world.path_finders.PathFinder;

import java.util.List;
import java.util.Optional;

public class Predator extends Creature implements Hunter<Herbivore> {
    public Predator(Coordinates coordinates) {
        super(coordinates, 15);
        this.healthPoint = 30;
        this.turnFrequency = 3;
    }

    @Override
    public void makeMove(WorldMap worldMap, PathFinder pathFinder) {
        Optional<Entity> target = findNearestTarget(worldMap, pathFinder);
        if (target.isEmpty()) {
            makeRandomStep(worldMap, pathFinder);
            return;
        }
        Herbivore herbivore = (Herbivore) target.get();
        Coordinates nextCoordinates = this.coordinates;
        if (canEat(herbivore)) {
            herbivore.takeDamage(this);
            if (!herbivore.isAlive()) {
                nextCoordinates = herbivore.getCoordinates();
                worldMap.removeEntity(herbivore);
            }
        } else {
            List<Coordinates> pathToTarget = pathFinder.find(this.coordinates, herbivore.getCoordinates());
            if (pathToTarget.isEmpty()) {
                return;
            }
            nextCoordinates = pathToTarget.get(0);
        }
        moveTo(nextCoordinates, worldMap);
    }

    @Override
    public int getDamage() {
        return attackPower;
    }
}