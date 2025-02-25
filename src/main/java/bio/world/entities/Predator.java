package bio.world.entities;

import bio.world.map.WorldMap;
import bio.world.path_finders.PathFinder;

import java.util.*;

public class Predator extends Creature implements Hunter<Herbivore> {
    private static final int INIT_HEALTH_POINT = 30;
    private static final int INIT_TURN_FREQUENCY = 3;
    private static final int INIT_ATTACK_POWER = 15;
    private static final int INIT_COUNT_WITHOUT_FOOD = 0;
    private static final int HUNGER_BORDER = 5;
    private final Set<Class<? extends Entity>> notObstaclesTypes = Set.of(Grass.class, Herbivore.class);

    public Predator(Coordinates coordinates) {
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

        List<Herbivore> herbivores = getTargetEntities(worldMap);
        Set<Coordinates> obstacles = getObstaclesCoordinates(worldMap, notObstaclesTypes);
        Coordinates nextCoordinates = this.coordinates;
        boolean ateInThisMove = false;
        for (Herbivore herbivore : herbivores) {
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
    protected List<Herbivore> getTargetEntities(WorldMap worldMap) {
        List<Entity> entities = worldMap.getAllEntities();
        List<Herbivore> targets = entities.stream()
                .filter(e -> e instanceof Herbivore)
                .map(e -> (Herbivore) e)
                .sorted(priorityTargetComparator)
                .toList();
        return targets;
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