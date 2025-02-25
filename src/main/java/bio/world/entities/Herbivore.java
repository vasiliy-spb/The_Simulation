package bio.world.entities;

import bio.world.map.WorldMap;
import bio.world.path_finders.PathFinder;

import java.util.*;

public class Herbivore extends Creature implements Hunter<Grass>, Prey<Predator> {
    private static final int INIT_HEALTH_POINT = 20;
    private static final int INIT_TURN_FREQUENCY = 2;
    private static final int INIT_ATTACK_POWER = 10;
    private static final int INIT_COUNT_WITHOUT_FOOD = 0;
    private static final int HUNGER_BORDER = 5;
    private final Set<Class<? extends Entity>> notObstaclesTypes = Set.of(Grass.class);

    public Herbivore(Coordinates coordinates) {
        super(coordinates, INIT_HEALTH_POINT, INIT_TURN_FREQUENCY, INIT_ATTACK_POWER, INIT_COUNT_WITHOUT_FOOD);
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

        List<Grass> grasses = getTargetEntities(worldMap);
        Set<Coordinates> obstacles = getObstaclesCoordinates(worldMap, notObstaclesTypes);
        Coordinates nextCoordinates = this.coordinates;
        boolean ateInThisMove = false;
        for (Grass grass : grasses) {
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
    protected List<Grass> getTargetEntities(WorldMap worldMap) {
        List<Entity> entities = worldMap.getAllEntities();
        List<Grass> targets = entities.stream()
                .filter(e -> e instanceof Grass)
                .map(e -> (Grass) e)
                .sorted(priorityTargetComparator)
                .toList();
        return targets;
    }

    @Override
    public void attack(Grass grass) {
        int satiety = grass.getSatiety();
        this.healthPoint = Math.min(this.healthPoint + satiety, INIT_HEALTH_POINT);
        grass.takeDamage(this);
    }

    @Override
    public int getDamage() {
        return attackPower;
    }

    @Override
    public void takeDamage(Predator hunter) {
        healthPoint -= hunter.getDamage();
    }

    @Override
    public int getSatiety() {
        return healthPoint;
    }
}
