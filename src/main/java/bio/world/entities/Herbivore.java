package bio.world.entities;

import bio.world.map.WorldMap;
import bio.world.path_finders.PathFinder;

import java.util.List;
import java.util.Optional;

public class Herbivore extends Creature implements Hunter<Grass>, Prey<Predator> {
    private int countMoveWithoutFood;
    private static final int INIT_HEALTH_POINT = 20;
    private static final int INIT_TURN_FREQUENCY = 2;
    private static final int INIT_ATTACK_POWER = 10;

    public Herbivore(Coordinates coordinates) {
        super(coordinates, INIT_ATTACK_POWER);
        this.healthPoint = INIT_HEALTH_POINT;
        this.turnFrequency = INIT_TURN_FREQUENCY;
        this.countMoveWithoutFood = 0;
    }

    @Override
    public void makeMove(WorldMap worldMap, PathFinder pathFinder) {
        if (countMoveWithoutFood >= 5) {
            this.healthPoint--;
        }
        if (!isAlive()) {
            worldMap.removeEntity(this);
            return;
        }
        Optional<Entity> target = findNearestTarget(worldMap, pathFinder);
        if (target.isEmpty()) {
            countMoveWithoutFood++;
            makeRandomStep(worldMap, pathFinder);
            return;
        }
        Grass grass = (Grass) target.get();
        Coordinates nextCoordinates;
        if (canAttack(grass)) {
            attack(grass);
            worldMap.removeEntity(grass);
            nextCoordinates = grass.getCoordinates();
        } else {
            countMoveWithoutFood++;
            List<Coordinates> pathToTarget = pathFinder.find(this.coordinates, grass.getCoordinates());
            if (pathToTarget.isEmpty()) {
                return;
            }
            nextCoordinates = pathToTarget.get(0);
        }
        moveTo(nextCoordinates, worldMap);
    }

    @Override
    public void attack(Grass grass) {
        int satiety = grass.getSatiety();
        this.healthPoint = Math.min(this.healthPoint + satiety, INIT_HEALTH_POINT);
        grass.takeDamage(this);
        countMoveWithoutFood = 0;
    }

    @Override
    public int getDamage() {
        return attackPower;
    }

    @Override
    public void takeDamage(Predator hunter) {
        this.healthPoint -= hunter.getDamage();
    }

    @Override
    public int getSatiety() {
        return this.healthPoint;
    }
}
