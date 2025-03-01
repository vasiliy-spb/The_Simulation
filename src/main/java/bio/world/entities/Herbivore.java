package bio.world.entities;

import bio.world.map.WorldMap;
import bio.world.path_finders.PathFinder;

import java.util.*;

public class Herbivore extends Creature implements Hunter<Grass>, Prey<Predator> {
    private static final int INIT_HEALTH_POINT = 20;
    private static final int INIT_TURN_FREQUENCY = 2;
    private static final int ATTACK_DISTANCE = 1;
    private static final int INIT_ATTACK_POWER = 10;
    private static final int INIT_COUNT_WITHOUT_FOOD = 0;
    private static final int HUNGER_BORDER = 5;
    private final Set<Class<? extends Entity>> NOT_OBSTACLES_TYPES_FOR_FINDER = Set.of(Grass.class);
    private final Set<Class<? extends Entity>> TARGET_TYPES = Set.of(Grass.class);
    private int satiety;

    public Herbivore(Coordinates coordinates) {
        super(coordinates, INIT_HEALTH_POINT, INIT_TURN_FREQUENCY, ATTACK_DISTANCE, INIT_ATTACK_POWER, INIT_COUNT_WITHOUT_FOOD, HUNGER_BORDER);
        this.satiety = this.healthPoint;
    }

    @Override
    public void makeMove(WorldMap worldMap, PathFinder pathFinder) {
        checkHealth();
        updateSatiety();

        if (!isAlive()) {
            worldMap.removeEntity(this);
            return;
        }

        List<? extends Entity> targets = getTargetsInPriorityOrder(worldMap, TARGET_TYPES);
        Set<Coordinates> obstacles = getObstaclesCoordinates(worldMap, NOT_OBSTACLES_TYPES_FOR_FINDER);
        Coordinates nextCoordinates = this.coordinates;
        boolean ateInThisMove = false;

        for (Entity target : targets) {

            if (!(target instanceof Grass grass)) {
                continue;
            }

            List<Coordinates> pathToTarget = pathFinder.find(this.coordinates, grass.getCoordinates(), obstacles);
            if (pathToTarget.isEmpty()) {
                continue;
            }

            if (canAttack(grass)) {
                attack(grass);
                worldMap.removeEntity(grass);
                nextCoordinates = grass.getCoordinates();
                countMoveWithoutFood = INIT_COUNT_WITHOUT_FOOD;
                ateInThisMove = true;
            } else {
                nextCoordinates = pathToTarget.get(0);
            }

            break;
        }

        if (!ateInThisMove) {
            countMoveWithoutFood++;
        }

        if (nextCoordinates.equals(this.coordinates)) {
            makeRandomStep(worldMap, pathFinder);
        } else {
            moveTo(nextCoordinates, worldMap);
        }
    }

    @Override
    public void attack(Grass grass) {
        int grassSatiety = grass.getSatiety();
        this.healthPoint = Math.min(this.healthPoint + grassSatiety, INIT_HEALTH_POINT);
        updateSatiety();
        grass.takeDamage(this);
    }

    @Override
    public int getDamage() {
        return attackPower;
    }

    @Override
    public void takeDamage(Predator hunter) {
        healthPoint -= hunter.getDamage();
        updateSatiety();
    }

    private void updateSatiety() {
        satiety = healthPoint;
    }

    @Override
    public int getSatiety() {
        return satiety;
    }
}
