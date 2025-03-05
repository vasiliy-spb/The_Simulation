package bio.world.entities.regular;

import bio.world.entities.Coordinates;
import bio.world.entities.Entity;
import bio.world.entities.statical.trap.Trap;
import bio.world.map.WorldMap;

import java.util.*;
import java.util.function.Predicate;

public class Herbivore extends Creature implements Hunter<Grass>, Prey<Hunter<Herbivore>> {
    private static final int INIT_HEALTH_POINT = 20;
    private static final int INIT_TURN_FREQUENCY = 2;
    private static final int ATTACK_DISTANCE = 1;
    private static final int INIT_ATTACK_POWER = 10;
    private static final int INIT_COUNT_WITHOUT_FOOD = 0;
    private static final int HUNGER_BORDER = 5;
    private static final Set<Class<? extends Entity>> TARGET_TYPES = Set.of(Grass.class);
    private static final Set<Class<? extends Entity>> NOT_OBSTACLES_TYPES_FOR_FINDER = Set.of(Grass.class, Trap.class);
    private static final Set<Class<? extends Entity>> NOT_OBSTACLES_TYPES_FOR_MOVE = Set.of(Grass.class, Trap.class);
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

    public Herbivore(Coordinates coordinates) {
        super(coordinates, INIT_HEALTH_POINT, INIT_TURN_FREQUENCY, ATTACK_DISTANCE,
                INIT_ATTACK_POWER, INIT_COUNT_WITHOUT_FOOD, HUNGER_BORDER, TARGET_TYPES,
                NOT_OBSTACLES_FOR_FINDER_CHECKER, NOT_OBSTACLES_FOR_MOVE_CHECKER);
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
    public int getSatiety() {
        return satiety;
    }

    @Override
    public void takeDamage(Hunter<Herbivore> hunter) {
        healthPoint -= hunter.getDamage();
        updateSatiety();
    }

    @Override
    protected void processAttack(List<Coordinates> pathToTarget, WorldMap worldMap) {
        Grass grass = getTargetEntityFromPath(pathToTarget, worldMap);
        if (canAttack(grass)) {
            attack(grass);
            worldMap.removeEntity(grass);
            moveTo(grass.getCoordinates(), worldMap);
            countMoveWithoutFood = INIT_COUNT_WITHOUT_FOOD;
        } else {
            countMoveWithoutFood++;
            moveTo(pathToTarget.get(0), worldMap);
        }
    }

    private Grass getTargetEntityFromPath(List<Coordinates> pathToTarget, WorldMap worldMap) {
        int lastIndex = pathToTarget.size() - 1;
        Coordinates lastCoordinates = pathToTarget.get(lastIndex);
        Entity targetEntity = worldMap.getEntityByCoordinates(lastCoordinates);
        if (targetEntity instanceof Grass grass) {
            return grass;
        }
        throw new ClassCastException();
    }
}
