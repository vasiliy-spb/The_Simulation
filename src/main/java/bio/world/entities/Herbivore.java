package bio.world.entities;

import bio.world.Coordinates;
import bio.world.WorldMap;
import bio.world.path_finders.PathFinder;

import java.util.List;
import java.util.Optional;

public class Herbivore extends Creature implements Hunter<Grass>, Prey<Predator> {

    public Herbivore(Coordinates coordinates) {
        super(coordinates, 10);
        this.healthPoint = 20;
        this.turnFrequency = 2;
    }

    @Override
    public void makeMove(WorldMap worldMap, PathFinder pathFinder) {
        if (!isAlive()) {
            worldMap.removeEntity(this);
            return;
        }
        Optional<Entity> target = findNearestTarget(worldMap, pathFinder);
        if (target.isEmpty()) {
            makeRandomStep(worldMap, pathFinder);
            return;
        }
        Grass grass = (Grass) target.get();
        Coordinates nextCoordinates;
        if (canEat(grass)) {
            grass.takeDamage(this);
            nextCoordinates = grass.getCoordinates();
            worldMap.removeEntity(grass);
        } else {
            List<Coordinates> pathToTarget = pathFinder.find(this.coordinates, grass.getCoordinates());
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

    public boolean isAlive() {
        return this.healthPoint > 0;
    }

    @Override
    public void takeDamage(Predator hunter) {
        this.healthPoint -= hunter.getDamage();
    }
}
