package bio.world.entities.regular;

import bio.world.entities.Coordinates;
import bio.world.entities.Entity;
import bio.world.entities.statical.trap.Trap;
import bio.world.map.WorldMap;

import java.util.*;
import java.util.function.Predicate;

public class Predator extends Creature implements Hunter<Herbivore>, Prey<Hunter<Predator>> {
    private static final int INIT_HEALTH_POINT = 30;
    private static final int INIT_TURN_FREQUENCY = 3;
    private static final int ATTACK_DISTANCE = 1;
    private static final int INIT_ATTACK_POWER = 15;
    private static final int INIT_COUNT_WITHOUT_FOOD = 0;
    private static final int HUNGER_BORDER = 5;
    private static final Set<Class<? extends Entity>> TARGET_TYPES = Set.of(Herbivore.class);
    private static final Set<Class<? extends Entity>> NOT_OBSTACLES_TYPES_FOR_FINDER = Set.of(Grass.class, Herbivore.class, Trap.class);
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

    public Predator(Coordinates coordinates) {
        super(coordinates, INIT_HEALTH_POINT, INIT_TURN_FREQUENCY, ATTACK_DISTANCE,
                INIT_ATTACK_POWER, INIT_COUNT_WITHOUT_FOOD, HUNGER_BORDER, TARGET_TYPES,
                NOT_OBSTACLES_FOR_FINDER_CHECKER, NOT_OBSTACLES_FOR_MOVE_CHECKER);
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

    @Override
    public void takeDamage(Hunter<Predator> hunter) {
        this.healthPoint -= hunter.getDamage();
    }

    @Override
    public int getSatiety() {
        return satiety;
    }

    @Override
    protected void processAttack(List<Coordinates> pathToTarget, WorldMap worldMap) {
        Herbivore herbivore = getTargetEntityFromPath(pathToTarget, worldMap);
        if (canAttack(herbivore)) {
            attack(herbivore);
            if (!herbivore.isAlive()) {
                worldMap.removeEntity(herbivore);
                moveTo(herbivore.getCoordinates(), worldMap);
            }
            countMoveWithoutFood = INIT_COUNT_WITHOUT_FOOD;
        } else {
            countMoveWithoutFood++;
            moveTo(pathToTarget.get(0), worldMap);
        }
    }

    private Herbivore getTargetEntityFromPath(List<Coordinates> pathToTarget, WorldMap worldMap) {
        int lastIndex = pathToTarget.size() - 1;
        Coordinates lastCoordinates = pathToTarget.get(lastIndex);
        Entity targetEntity = worldMap.getEntityByCoordinates(lastCoordinates);
        if (targetEntity instanceof Herbivore herbivore) {
            return herbivore;
        }
        throw new ClassCastException();
    }
}