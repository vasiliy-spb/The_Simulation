package bio.world.entities;

import bio.world.map.WorldMap;
import bio.world.path_finders.PathFinder;

import java.util.*;

public class Predator extends Creature implements Hunter<Herbivore> {
    private int countMoveWithoutFood;
    private static final int INIT_HEALTH_POINT = 30;
    private static final int INIT_TURN_FREQUENCY = 3;
    private static final int INIT_ATTACK_POWER = 15;

    public Predator(Coordinates coordinates) {
        super(coordinates, INIT_ATTACK_POWER);
        this.healthPoint = INIT_HEALTH_POINT;
        this.turnFrequency = INIT_TURN_FREQUENCY;
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
        Herbivore herbivore = (Herbivore) target.get();
        Coordinates nextCoordinates = this.coordinates;
        if (canAttack(herbivore)) {
            attack(herbivore);
            if (!herbivore.isAlive()) {
                nextCoordinates = herbivore.getCoordinates();
                worldMap.removeEntity(herbivore);
            }
        } else {
            countMoveWithoutFood++;
            Set<Coordinates> obstacles = getObstaclesCoordinates(worldMap);
            List<Coordinates> pathToTarget = pathFinder.find(this.coordinates, herbivore.getCoordinates(), obstacles);
            if (pathToTarget.isEmpty()) {
                return;
            }
            nextCoordinates = pathToTarget.get(0);
        }
        moveTo(nextCoordinates, worldMap);
    }

    @Override
    protected List<Entity> getTargetEntities(WorldMap worldMap) {
        List<Entity> entities = worldMap.getAllEntities();
        List<Entity> targets = entities.stream()
                .filter(e -> e instanceof Herbivore)
                .toList();
        return targets;
    }

    @Override
    protected Set<Coordinates> getObstaclesCoordinates(WorldMap worldMap) {
        List<Entity> entities = worldMap.getAllEntities();
        Set<Coordinates> obstacles = new HashSet<>();
        for (Entity entity : entities) {
            if (entity instanceof Grass || entity instanceof Herbivore) {
                continue;
            }
            obstacles.add(entity.getCoordinates());
        }
        return obstacles;
    }

    @Override
    public void attack(Herbivore herbivore) {
        int satiety = Math.min(herbivore.getSatiety(), this.attackPower);
        this.healthPoint = Math.min(this.healthPoint + satiety, INIT_HEALTH_POINT);
        herbivore.takeDamage(this);
        countMoveWithoutFood = 0;
    }

    @Override
    public int getDamage() {
        return attackPower;
    }
}