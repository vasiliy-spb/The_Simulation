package bio.world.entities.regular;

import bio.world.entities.Coordinates;
import bio.world.entities.Entity;
import bio.world.map.WorldMap;
import bio.world.path_finders.PathFinder;

import java.util.*;

public class Huntsman extends Human implements Hunter<Creature> {
    private static final int INIT_HEALTH_POINT = 100;
    private static final int INIT_TURN_FREQUENCY = 5;
    private static final int ATTACK_DISTANCE = 3;
    private static final int INIT_ATTACK_POWER = 40;
    private static final int INIT_SHARPSHOOTING = 50;
    private static final int MIN_SHARPSHOOTING = 20;
    private static final int MAX_SHARPSHOOTING = 100;
    private final Random shotRandom;
    private static final HuntsmenScope HUNTSMEN_SCOPE = new HuntsmenScope();
    private static final Set<Class<? extends Entity>> TARGET_TYPES = Set.of(Herbivore.class, Predator.class);
    private static final Comparator<Entity> priorityTargetComparator = (t1, t2) -> {
        if (t1 instanceof Predator p1 && t2 instanceof Predator p2) {
            return Integer.compare(p1.healthPoint, p2.healthPoint);
        }
        if (t1 instanceof Predator && t2 instanceof Herbivore) {
            return -1;
        }
        if (t1 instanceof Herbivore && t2 instanceof Predator) {
            return 1;
        }
        return 0;
    };
    private int attackDistance;
    private int attackPower;
    private int sharpshooting;

    public Huntsman(Coordinates coordinates) {
        super(coordinates, INIT_HEALTH_POINT, INIT_TURN_FREQUENCY);
        this.attackDistance = ATTACK_DISTANCE;
        this.attackPower = INIT_ATTACK_POWER;
        this.sharpshooting = INIT_SHARPSHOOTING;
        this.shotRandom = new Random();
    }

    public void makeMove(WorldMap worldMap, PathFinder pathFinder) {
        List<Creature> targets = getTargetsInPriorityOrder(worldMap, TARGET_TYPES);
        if (targets.isEmpty()) {
            makeRandomStep(worldMap, pathFinder);
            return;
        }
        boolean madeShot = false;
        for (Creature target : targets) {
            if (!canAttack(target, worldMap)) {
                continue;
            }
            attack(target);
            madeShot = true;
            break;
        }

        if (!madeShot) {
            makeRandomStep(worldMap, pathFinder);
        }
    }

    private List<Creature> getTargetsInPriorityOrder(WorldMap worldMap, Set<Class<? extends Entity>> targetTypes) {
        List<Entity> entities = worldMap.getAllEntities();
        List<Creature> targets = new ArrayList<>();

        for (Entity entity : entities) {
            if (!targetTypes.contains(entity.getClass())) {
                continue;
            }

            if (!isInShotArea(entity)) {
                continue;
            }

            Creature creature = (Creature) entity;
            if (creature.isAlive()) {
                targets.add(creature);
            }
        }

        targets.sort(priorityTargetComparator);
        return targets;
    }

    private boolean isInShotArea(Entity entity) {
        return calculateApproximateDistance(this.coordinates, entity.getCoordinates()) <= attackDistance;
    }

    private int calculateApproximateDistance(Coordinates current, Coordinates target) {
        return Math.max(Math.abs(current.row() - target.row()), Math.abs(current.column() - target.column()));
    }

    private boolean canAttack(Creature target, WorldMap worldMap) {
        return HUNTSMEN_SCOPE.canAim(this.coordinates, target.getCoordinates(), worldMap);
    }

    @Override
    public void attack(Creature prey) {
        if (!shoot()) {
            decreaseSharpshooting();
            return;
        }
        increaseSharpshooting();
        prey.die();
    }

    private boolean shoot() {
        int successShotBound = shotRandom.nextInt(MAX_SHARPSHOOTING + 1);
        return successShotBound <= sharpshooting;
    }

    private void decreaseSharpshooting() {
        sharpshooting--;
        sharpshooting = Math.max(sharpshooting, MIN_SHARPSHOOTING);
    }

    private void increaseSharpshooting() {
        sharpshooting++;
        sharpshooting = Math.min(sharpshooting, MAX_SHARPSHOOTING);
    }

    @Override
    public int getDamage() {
        return attackPower;
    }
}
