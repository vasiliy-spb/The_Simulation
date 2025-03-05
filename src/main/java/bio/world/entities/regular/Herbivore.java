package bio.world.entities.regular;

import bio.world.entities.Coordinates;
import bio.world.entities.Entity;
import bio.world.entities.statical.trap.Trap;
import bio.world.map.WorldMap;
import bio.world.path_finders.PathFinder;

import java.util.*;
import java.util.function.Predicate;

public class Herbivore extends Creature implements Hunter<Grass>, Prey<Hunter<Herbivore>> {
    private static final int INIT_HEALTH_POINT = 20;
    private static final int INIT_TURN_FREQUENCY = 2;
    private static final int ATTACK_DISTANCE = 1;
    private static final int INIT_ATTACK_POWER = 10;
    private static final int INIT_COUNT_WITHOUT_FOOD = 0;
    private static final int HUNGER_BORDER = 5;
    private static final Set<Class<? extends Entity>> NOT_OBSTACLES_TYPES_FOR_FINDER = Set.of(Grass.class, Trap.class);
    private static final Set<Class<? extends Entity>> NOT_OBSTACLES_TYPES_FOR_MOVE = Set.of(Grass.class, Trap.class);
    private static final Set<Class<? extends Entity>> TARGET_TYPES = Set.of(Grass.class);
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

    public Herbivore(Coordinates coordinates) {
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

    private void updateSatiety() {
        satiety = healthPoint;
    }

    @Override
    public int getSatiety() {
        return satiety;
    }

    @Override
    public void takeDamage(Hunter<Herbivore> hunter) {
        healthPoint -= hunter.getDamage();
        updateSatiety();
    }
}
