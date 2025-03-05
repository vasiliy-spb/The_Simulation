package bio.world.entities.regular;

import bio.world.entities.Coordinates;
import bio.world.entities.Entity;
import bio.world.entities.statical.trap.Trap;
import bio.world.map.WorldMap;
import bio.world.path_finders.PathFinder;

import java.util.*;
import java.util.function.Predicate;

public class Predator extends Creature implements Hunter<Herbivore>, Prey<Hunter<Predator>> {
    private static final int INIT_HEALTH_POINT = 30;
    private static final int INIT_TURN_FREQUENCY = 3;
    private static final int ATTACK_DISTANCE = 1;
    private static final int INIT_ATTACK_POWER = 15;
    private static final int INIT_COUNT_WITHOUT_FOOD = 0;
    private static final int HUNGER_BORDER = 5;
    private static final Set<Class<? extends Entity>> NOT_OBSTACLES_TYPES_FOR_FINDER = Set.of(Grass.class, Herbivore.class, Trap.class);
    private static final Set<Class<? extends Entity>> NOT_OBSTACLES_TYPES_FOR_MOVE = Set.of(Grass.class, Trap.class);
    private static final Set<Class<? extends Entity>> TARGET_TYPES = Set.of(Herbivore.class);
    private static final Predicate<Entity> NOT_OBSTACLES_FOR_MOVE_CHECKER = e -> {
        if (e instanceof Trap trap) {
            return !trap.hasCapturedCreature();
        }
        return NOT_OBSTACLES_TYPES_FOR_MOVE.contains(e.getClass());
    };
    private static final Predicate<Entity> NOT_OBSTACLES_FOR_FINDER_CHECKER = e -> {
        if (e instanceof Trap trap) {
            return !trap.hasCapturedCreature();
        }
        return NOT_OBSTACLES_TYPES_FOR_FINDER.contains(e.getClass());
    };
    private int satiety;

    public Predator(Coordinates coordinates) {
        super(coordinates, INIT_HEALTH_POINT, INIT_TURN_FREQUENCY, ATTACK_DISTANCE, INIT_ATTACK_POWER, INIT_COUNT_WITHOUT_FOOD, HUNGER_BORDER);
        this.satiety = this.healthPoint;
    }

    @Override
    public void makeMove(WorldMap worldMap, PathFinder pathFinder) {
        if (!canMakeStep(worldMap)) {
            return;
        }

        List<? extends Entity> targets = getTargetsInPriorityOrder(worldMap, TARGET_TYPES);
        Set<Coordinates> obstacles = getObstaclesCoordinates(worldMap, NOT_OBSTACLES_FOR_FINDER_CHECKER);
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
                    worldMap.removeEntity(herbivore);
                    nextCoordinates = herbivore.getCoordinates();
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
            makeRandomStep(worldMap, pathFinder, NOT_OBSTACLES_FOR_MOVE_CHECKER);
        } else {
            moveTo(nextCoordinates, worldMap);
        }
    }

    private boolean canMakeStep(WorldMap worldMap) {
        if (wasShot()) {
            return false;
        }

        checkHealth();
        updateSatiety();

        if (!isAlive()) {
            worldMap.removeEntity(this);
            return false;
        }

        return !captured;
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

    private void updateSatiety() {
        satiety = healthPoint;
    }

    @Override
    public int getSatiety() {
        return satiety;
    }

    @Override
    public void takeDamage(Hunter<Predator> hunter) {
        this.healthPoint -= hunter.getDamage();
    }
}