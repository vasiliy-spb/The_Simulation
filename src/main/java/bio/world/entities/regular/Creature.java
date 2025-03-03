package bio.world.entities.regular;

import bio.world.entities.Coordinates;
import bio.world.entities.Entity;
import bio.world.entities.statical.trap.Trap;
import bio.world.map.WorldMap;
import bio.world.path_finders.PathFinder;

import java.util.*;

public abstract class Creature extends Entity {
    protected int healthPoint;
    protected int turnFrequency;
    protected int attackDistance;
    protected int attackPower;
    protected int countMoveWithoutFood;
    protected int hungerBorder;
    private boolean isShotted = false;
    protected boolean captured;
    protected final Comparator<Entity> priorityTargetComparator;
    private final Set<Class<? extends Entity>> NOT_OBSTACLES_TYPES_FOR_MOVE = Set.of(Grass.class);

    public Creature(Coordinates coordinates, int healthPoint, int turnFrequency, int attackDistance, int attackPower, int countMoveWithoutFood, int hungerBorder) {
        super(coordinates);
        this.healthPoint = healthPoint;
        this.turnFrequency = turnFrequency;
        this.attackDistance = attackDistance;
        this.attackPower = attackPower;
        this.countMoveWithoutFood = countMoveWithoutFood;
        this.hungerBorder = hungerBorder;
        this.captured = false;
        this.priorityTargetComparator = (t1, t2) ->
                calculateApproximateDistance(this.coordinates, t1.getCoordinates()) -
                        calculateApproximateDistance(this.coordinates, t2.getCoordinates());
    }

    abstract public void makeMove(WorldMap worldMap, PathFinder pathFinder);

    protected void checkHealth() {
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

    public boolean isAlive() {
        return this.healthPoint > 0;
    }

    protected List<? extends Entity> getTargetsInPriorityOrder(WorldMap worldMap, Set<Class<? extends Entity>> targetTypes) {
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

    protected Set<Coordinates> getObstaclesCoordinates(WorldMap worldMap, Set<Class<? extends Entity>> notObstaclesTypes) {
        List<Entity> entities = worldMap.getAllEntities();
        Set<Coordinates> obstacles = new HashSet<>();

        for (Entity entity : entities) {

            if (notObstaclesTypes.contains(entity.getClass())) {
                continue;
            }

            obstacles.add(entity.getCoordinates());
        }

        return obstacles;
    }

    protected void makeRandomStep(WorldMap worldMap, PathFinder pathFinder, Set<Class<? extends Entity>> notObstaclesTypes) {
        Optional<Coordinates> nextCoordinatesContainer = pathFinder.findRandomStepFrom(this.coordinates, notObstaclesTypes);

        if (nextCoordinatesContainer.isEmpty()) {
            return;
        }

        Coordinates nextCoordinates = nextCoordinatesContainer.get();

        if (worldMap.areBusy(nextCoordinates)) {
            Entity entity = worldMap.getEntityByCoordinates(nextCoordinates);
            if (entity instanceof Trap trap) {
                trap.capture(this);
                this.captured = true;
                return;
            }
        }

        moveTo(nextCoordinates, worldMap);
    }

    protected void moveTo(Coordinates nextCoordinates, WorldMap worldMap) {
        if (worldMap.areBusy(nextCoordinates)) {
            Entity entity = worldMap.getEntityByCoordinates(nextCoordinates);
            if (entity instanceof Trap trap) {
                trap.capture(this);
                this.captured = true;
                worldMap.removeEntity(this);
                worldMap.addEntity(trap);
                return;
            }
        }
        worldMap.moveEntity(this.coordinates, nextCoordinates);
        this.setCoordinates(nextCoordinates);
    }

    protected boolean canAttack(Entity entity) {
        int rowDiff = Math.abs(this.coordinates.row() - entity.getCoordinates().row());
        int columnDiff = Math.abs(this.coordinates.column() - entity.getCoordinates().column());
        return rowDiff <= attackDistance && columnDiff <= attackDistance;
    }

    public boolean shouldMove(int currentTick) {
        return currentTick % turnFrequency == 0;
    }

    private int calculateApproximateDistance(Coordinates from, Coordinates target) {
        return Math.max(Math.abs(from.row() - target.row()), Math.abs(from.column() - target.column()));
    }

    public void die() {
        this.isShotted = true;
        this.healthPoint = 0;
    }

    public boolean wasShot() {
        return isShotted;
    }
}
