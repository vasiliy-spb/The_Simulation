package bio.world.entities.regular;

import bio.world.entities.Coordinates;
import bio.world.entities.Entity;
import bio.world.entities.statical.trap.Trap;
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
    private static final int MIN_TRAP_DISTANCE = 3;
    private static final int INIT_TRAP_COUNT = 3;
    private static final Random random = new Random();
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
    private int availableTrapCount;
    private int attackDistance;
    private int attackPower;
    private int sharpshooting;

    public Huntsman(Coordinates coordinates) {
        super(coordinates, INIT_HEALTH_POINT, INIT_TURN_FREQUENCY);
        this.attackDistance = ATTACK_DISTANCE;
        this.attackPower = INIT_ATTACK_POWER;
        this.sharpshooting = INIT_SHARPSHOOTING;
        this.availableTrapCount = INIT_TRAP_COUNT;
    }

    @Override
    public void makeMove(WorldMap worldMap, PathFinder pathFinder) {
        List<Creature> targets = getTargetsInPriorityOrder(worldMap);

        Optional<Creature> targetContainer = selectTarget(targets, worldMap);
        if (targetContainer.isPresent()) {
            Creature target = targetContainer.get();
            attack(target);
            return;
        }

        if (canMakeStep(pathFinder)) {
            putTrap(worldMap);
            makeRandomStep(worldMap, pathFinder);
        }
    }

    private List<Creature> getTargetsInPriorityOrder(WorldMap worldMap) {
        List<Entity> entities = worldMap.getAllEntities();
        List<Creature> targets = new ArrayList<>();

        for (Entity entity : entities) {
            if (!TARGET_TYPES.contains(entity.getClass())) {
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

    private Optional<Creature> selectTarget(List<Creature> targets, WorldMap worldMap) {
        for (Creature target : targets) {
            if (!canAttack(target, worldMap)) {
                continue;
            }
            return Optional.of(target);
        }
        return Optional.empty();
    }

    private boolean canAttack(Creature target, WorldMap worldMap) {
        return HUNTSMEN_SCOPE.canAim(this.coordinates, target.getCoordinates(), worldMap);
    }

    @Override
    public void attack(Creature prey) {
        if (!wasShotSuccessful()) {
            decreaseSharpshooting();
            return;
        }
        increaseSharpshooting();
        prey.setDead();
    }

    private boolean wasShotSuccessful() {
        int successShotBound = random.nextInt(MAX_SHARPSHOOTING + 1);
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

    private boolean canMakeStep(PathFinder pathFinder) {
        Optional<Coordinates> nextCoordinatesContainer = pathFinder.findRandomStepFrom(this.coordinates, NOT_OBSTACLES_FOR_MOVE_CHECKER);
        if (nextCoordinatesContainer.isPresent()) {
            Coordinates nextCoordinates = nextCoordinatesContainer.get();
            return !this.coordinates.equals(nextCoordinates);
        }
        return false;
    }

    private void putTrap(WorldMap worldMap) {
        if (hasTrapNear(worldMap) || availableTrapCount == 0) {
            return;
        }
        Trap trap = new Trap(this.coordinates, this);
        worldMap.addEntity(trap);
        availableTrapCount--;
    }

    private boolean hasTrapNear(WorldMap worldMap) {
        List<Entity> entities = worldMap.getAllEntities();
        for (Entity entity : entities) {
            if (entity instanceof Trap trap) {
                int distance = calculateApproximateDistance(this.coordinates, trap.getCoordinates());
                if (distance <= MIN_TRAP_DISTANCE) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getDamage() {
        return attackPower;
    }

    public void increaseAvailableTrapCount() {
        this.availableTrapCount++;
    }
}
