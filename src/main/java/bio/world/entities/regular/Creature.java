package bio.world.entities.regular;

import bio.world.entities.Coordinates;
import bio.world.entities.Entity;
import bio.world.entities.statical.trap.Trap;
import bio.world.map.WorldMap;
import bio.world.path_finders.PathFinder;

import java.util.*;
import java.util.function.Predicate;

public abstract class Creature extends Entity {
    protected int healthPoint;
    protected int attackPower;
    protected int countMoveWithoutFood;
    protected int satiety;
    private final int turnFrequency;
    private final int attackDistance;
    private final int hungerBorder;
    private final Comparator<Entity> priorityTargetComparator;
    private final Set<Class<? extends Entity>> targetTypes;
    private final Predicate<Entity> notObstaclesForFinderChecker;
    private final Predicate<Entity> notObstaclesForMoveChecker;
    private boolean isShotted = false;
    private boolean captured = false;

    public Creature(Coordinates coordinates, int healthPoint, int turnFrequency,
                    int attackDistance, int attackPower, int countMoveWithoutFood,
                    int hungerBorder, Set<Class<? extends Entity>> targetTypes,
                    Predicate<Entity> notObstaclesForFinderChecker,
                    Predicate<Entity> notObstaclesForMoveChecker) {
        super(coordinates);
        this.healthPoint = healthPoint;
        this.attackPower = attackPower;
        this.countMoveWithoutFood = countMoveWithoutFood;
        this.satiety = this.healthPoint;
        this.turnFrequency = turnFrequency;
        this.attackDistance = attackDistance;
        this.hungerBorder = hungerBorder;
        this.priorityTargetComparator = (t1, t2) ->
                calculateApproximateDistance(this.coordinates, t1.getCoordinates()) -
                        calculateApproximateDistance(this.coordinates, t2.getCoordinates());
        this.targetTypes = targetTypes;
        this.notObstaclesForFinderChecker = notObstaclesForFinderChecker;
        this.notObstaclesForMoveChecker = notObstaclesForMoveChecker;
    }

    public void makeMove(WorldMap worldMap, PathFinder pathFinder) {
        if (!canMakeStep(worldMap)) {
            return;
        }

        List<? extends Entity> targets = getTargetsInPriorityOrder(worldMap, targetTypes);
        Set<Coordinates> obstacles = getObstaclesCoordinates(worldMap, notObstaclesForFinderChecker);
        List<Coordinates> pathToNearestTarget = findPathToNearestTarget(targets, pathFinder, obstacles);

        if (!pathToNearestTarget.isEmpty()) {
            processAttack(pathToNearestTarget, worldMap);
            return;
        }

        countMoveWithoutFood++;
        makeRandomStep(worldMap, pathFinder, notObstaclesForMoveChecker);
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

    public boolean wasShot() {
        return isShotted;
    }

    private void checkHealth() {
        if (captured) {
            this.healthPoint -= 2;
        }
        if (isHungry()) {
            this.healthPoint--;
        }
    }

    private boolean isHungry() {
        return countMoveWithoutFood >= this.hungerBorder;
    }

    protected void updateSatiety() {
        satiety = healthPoint;
    }

    public boolean isAlive() {
        return this.healthPoint > 0;
    }

    private List<? extends Entity> getTargetsInPriorityOrder(WorldMap worldMap, Set<Class<? extends Entity>> targetTypes) {
        List<Entity> entities = worldMap.getAllEntities();
        List<Entity> targets = new ArrayList<>();

        for (Entity entity : entities) {
            if (targetTypes.contains(entity.getClass())) {
                targets.add(entity);
            }
        }

        targets.sort(priorityTargetComparator);
        return targets;
    }

    private Set<Coordinates> getObstaclesCoordinates(WorldMap worldMap, Predicate<Entity> notObstaclesForMoveChecker) {
        List<Entity> entities = worldMap.getAllEntities();
        Set<Coordinates> obstacles = new HashSet<>();

        for (Entity entity : entities) {
            if (notObstaclesForMoveChecker.test(entity)) {
                continue;
            }

            obstacles.add(entity.getCoordinates());
        }

        return obstacles;
    }

    private List<Coordinates> findPathToNearestTarget(List<? extends Entity> targets, PathFinder pathFinder, Set<Coordinates> obstacles) {
        for (Entity target : targets) {
            List<Coordinates> pathToTarget = pathFinder.find(this.coordinates, target.getCoordinates(), obstacles);
            if (pathToTarget.isEmpty()) {
                continue;
            }

            return pathToTarget;
        }
        return new ArrayList<>();
    }

    abstract protected void processAttack(List<Coordinates> pathToTarget, WorldMap worldMap);

    private void makeRandomStep(WorldMap worldMap, PathFinder pathFinder, Predicate<Entity> notObstaclesForMoveChecker) {
        Optional<Coordinates> nextCoordinatesContainer = pathFinder.findRandomStepFrom(this.coordinates, notObstaclesForMoveChecker);
        if (nextCoordinatesContainer.isEmpty()) {
            return;
        }

        Coordinates nextCoordinates = nextCoordinatesContainer.get();

        if (worldMap.areBusy(nextCoordinates)) {
            Entity entity = worldMap.getEntityByCoordinates(nextCoordinates);
            if (entity instanceof Trap trap) {
                fallIntoTrap(trap, worldMap);
                return;
            }
        }

        moveTo(nextCoordinates, worldMap);
    }

    private void fallIntoTrap(Trap trap, WorldMap worldMap) {
        trap.capture(this);
        this.captured = true;
        worldMap.removeEntity(this);
        worldMap.addEntity(trap);
        this.setCoordinates(trap.getCoordinates());
    }

    protected void moveTo(Coordinates nextCoordinates, WorldMap worldMap) {
        if (worldMap.areBusy(nextCoordinates)) {
            Entity entity = worldMap.getEntityByCoordinates(nextCoordinates);
            if (entity instanceof Trap trap) {
                fallIntoTrap(trap, worldMap);
                return;
            }
        }
        worldMap.removeEntity(this);
        this.setCoordinates(nextCoordinates);
        worldMap.addEntity(this);
    }

    public boolean shouldMove(int currentTick) {
        return currentTick % turnFrequency == 0;
    }

    public void die() {
        this.isShotted = true;
        this.healthPoint = 0;
    }

    protected boolean canAttack(Entity entity) {
        int rowDiff = Math.abs(this.coordinates.row() - entity.getCoordinates().row());
        int columnDiff = Math.abs(this.coordinates.column() - entity.getCoordinates().column());
        return rowDiff <= attackDistance && columnDiff <= attackDistance;
    }

    private int calculateApproximateDistance(Coordinates from, Coordinates target) {
        return Math.max(Math.abs(from.row() - target.row()), Math.abs(from.column() - target.column()));
    }
}
