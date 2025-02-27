package bio.world.entities;

import bio.world.map.WorldMap;
import bio.world.path_finders.PathFinder;

import java.util.*;

public class Huntsmen extends Human implements Hunter<Creature> {
    private static final int INIT_HEALTH_POINT = 100;
    private static final int INIT_TURN_FREQUENCY = 5;
    private static final int ATTACK_DISTANCE = 3;
    private static final int INIT_ATTACK_POWER = 40;
    private static final int INIT_SHARPSHOOTING = 50;
    private static final int MIN_SHARPSHOOTING = 20;
    private static final int MAX_SHARPSHOOTING = 100;
    private final Random shotRandom;
    private static final Set<Class<? extends Entity>> NOT_OBSTACLES_TYPES = Set.of(Grass.class, Herbivore.class, Predator.class);
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
    private int healthPoint;
    private int turnFrequency;
    private int attackDistance;
    private int attackPower;
    private int sharpshooting;

    public Huntsmen(Coordinates coordinates) {
        super(coordinates);
        this.healthPoint = INIT_HEALTH_POINT;
        this.turnFrequency = INIT_TURN_FREQUENCY;
        this.attackDistance = ATTACK_DISTANCE;
        this.attackPower = INIT_ATTACK_POWER;
        this.sharpshooting = INIT_SHARPSHOOTING;
        this.shotRandom = new Random();
    }

    @Override
    public int getDamage() {
        return attackPower;
    }

    @Override
    public void attack(Creature prey) {
        if (!(prey instanceof Prey p)) {
            return;
        }
        if (!shoot()) {
            decreaseSharpshooting();
            return;
        }
        increaseSharpshooting();
        p.takeDamage(this);
    }

    private boolean shoot() {
        int successShotBound = shotRandom.nextInt(MAX_SHARPSHOOTING + 1);
        return successShotBound <= sharpshooting;
    }

    private void decreaseSharpshooting() {
        sharpshooting--;
        sharpshooting = Math.min(sharpshooting, MIN_SHARPSHOOTING);
    }

    /*
    Находит список целей
        если цели есть
            -> проходит по списку целей
                если между ним и целью нет препятствий
                    -> стреляет
                    если выстрел успешный
                        -> цель получает урон
                        обновляет меткость
                    иначе
                        -> ничего
                    выстрел сделан = true
                    break;
                иначе
                    -> переходит к следующей цели
        иначе
            -> делает случайный ход

        если выстрел не сделан
            -> делает случайный ход
     */
    public void makeMove(WorldMap worldMap, PathFinder pathFinder) {
        List<Creature> targets = getTargetsInPriorityOrder(worldMap, TARGET_TYPES);
        if (targets.isEmpty()) {
            makeRandomStep(worldMap, pathFinder);
            return;
        }
        boolean madeShot = false;
        for (Creature target : targets) {
            if (!canAttack(target)) {
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

    private boolean canAttack(Creature target) {
        return true;
    }

    private void increaseSharpshooting() {
        sharpshooting++;
        sharpshooting = Math.max(sharpshooting, MAX_SHARPSHOOTING);
    }

    private List<Creature> getTargetsInPriorityOrder(WorldMap worldMap, Set<Class<? extends Entity>> targetTypes) {
        List<Entity> entities = worldMap.getAllEntities();
        List<Creature> targets = new ArrayList<>();

        for (Entity entity : entities) {
            if (targetTypes.contains(entity.getClass())) {
                if (isInShotArea(entity)) {
                    targets.add((Creature) entity);
                }
            }
        }

        targets.sort(priorityTargetComparator);
        return targets;
    }

    private boolean isInShotArea(Entity entity) {
        return calculateApproximateDistance(this.coordinates, entity.getCoordinates()) <= attackDistance;
    }

    private void makeRandomStep(WorldMap worldMap, PathFinder pathFinder) {
        Optional<Coordinates> nextCoordinatesContainer = pathFinder.findRandomStepFrom(this.coordinates);

        if (nextCoordinatesContainer.isEmpty()) {
            return;
        }

        Coordinates nextCoordinates = nextCoordinatesContainer.get();
        moveTo(nextCoordinates, worldMap);
    }

    protected void moveTo(Coordinates nextCoordinates, WorldMap worldMap) {
        worldMap.moveEntity(this.coordinates, nextCoordinates);
        this.setCoordinates(nextCoordinates);
    }

    private int calculateApproximateDistance(Coordinates current, Coordinates target) {
        return Math.max(Math.abs(current.row() - target.row()), Math.abs(current.column() - target.column()));
    }

    public boolean shouldMove(int currentTick) {
        return currentTick % turnFrequency == 0;
    }
}
