package bio.world.entities;

import bio.world.map.WorldMap;
import bio.world.path_finders.PathFinder;

import java.util.*;

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
            Set<Coordinates> obstacles = getObstaclesCoordinates(worldMap);
            List<Coordinates> pathToTarget = pathFinder.find(this.coordinates, grass.getCoordinates(), obstacles);
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
                .filter(e -> e instanceof Grass)
                .toList();
        return targets;
    }

    @Override
    protected Set<Coordinates> getObstaclesCoordinates(WorldMap worldMap) {
        List<Entity> entities = worldMap.getAllEntities();
        Set<Coordinates> obstacles = new HashSet<>();
        for (Entity entity : entities) {
            if (entity instanceof Grass) {
                continue;
            }
            obstacles.add(entity.getCoordinates());
        }
        return obstacles;
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
