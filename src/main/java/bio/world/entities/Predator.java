package bio.world.entities;

import bio.world.map.WorldMap;
import bio.world.path_finders.PathFinder;

import java.util.*;

public class Predator extends Creature implements Hunter<Herbivore> {
    private static final int INIT_HEALTH_POINT = 30;
    private static final int INIT_TURN_FREQUENCY = 3;
    private static final int ATTACK_DISTANCE = 1;
    private static final int INIT_ATTACK_POWER = 15;
    private static final int INIT_COUNT_WITHOUT_FOOD = 0;
    private static final int HUNGER_BORDER = 5;
    private final Set<Class<? extends Entity>> NOT_OBSTACLES_TYPES = Set.of(Grass.class, Herbivore.class);
    private final Set<Class<? extends Entity>> TARGET_TYPES = Set.of(Herbivore.class);

    public Predator(Coordinates coordinates) {
        super(coordinates, INIT_HEALTH_POINT, INIT_TURN_FREQUENCY, ATTACK_DISTANCE, INIT_ATTACK_POWER, INIT_COUNT_WITHOUT_FOOD);
    }

    @Override
    public void makeMove(WorldMap worldMap, PathFinder pathFinder) {
        if (countMoveWithoutFood >= HUNGER_BORDER) {
            this.healthPoint--;
        }
        if (!isAlive()) {
            worldMap.removeEntity(this);
            return;
        }

        List<? extends Entity> targets = getTargetEntitiesInPriorityOrder(worldMap, TARGET_TYPES);
        Set<Coordinates> obstacles = getObstaclesCoordinates(worldMap, NOT_OBSTACLES_TYPES);
        Coordinates nextCoordinates = this.coordinates;
        boolean ateInThisMove = false;
        for (Entity target : targets) {
            if (!(target instanceof Herbivore herbivore)) {
                continue;
            }
            List<Coordinates> pathToTarget = pathFinder.find(this.coordinates, herbivore.getCoordinates(), obstacles);
            if (pathToTarget.isEmpty()) {
                continue;
            }
            if (canAttack(herbivore)) {
                attack(herbivore);
                if (!herbivore.isAlive()) {
                    nextCoordinates = herbivore.getCoordinates();
                    worldMap.removeEntity(herbivore);
                }
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
        if (nextCoordinates.equals(this.coordinates) && !ateInThisMove) {
            makeRandomStep(worldMap, pathFinder);
        } else {
            moveTo(nextCoordinates, worldMap);
        }
    }

    @Override
    public void attack(Herbivore herbivore) {
        int satiety = Math.min(herbivore.getSatiety(), this.attackPower);
        this.healthPoint = Math.min(this.healthPoint + satiety, INIT_HEALTH_POINT);
        herbivore.takeDamage(this);
    }

    @Override
    public int getDamage() {
        return attackPower;
    }
}