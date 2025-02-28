package bio.world.path_finders;

import bio.world.entities.Coordinates;
import bio.world.entities.Entity;
import bio.world.entities.regular.Grass;
import bio.world.map.WorldMap;

import java.util.*;

public class AStarPathFinder implements PathFinder {
    private static final int[][] OFFSETS_TO_NEIGHBOURS = {{-1, -1}, {-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}};
    private static final Set<Direction> directions = Set.of(
            new Direction(-1, -1, 14),
            new Direction(-1, 0, 10),
            new Direction(-1, 1, 14),
            new Direction(0, -1, 10),
            new Direction(0, 1, 10),
            new Direction(1, -1, 14),
            new Direction(1, 0, 10),
            new Direction(1, 1, 14)
    );
    private final WorldMap worldMap;
    private final Set<Coordinates> visitedCoordinates;
    private final Queue<PathNode> openList;
    private final Map<Coordinates, PathNode> nodeRegistry;
    private final Random random;

    public AStarPathFinder(WorldMap worldMap) {
        this.worldMap = worldMap;
        this.visitedCoordinates = new HashSet<>();
        this.openList = new PriorityQueue<>((p1, p2) -> Integer.compare(p1.cost, p2.cost));
        this.nodeRegistry = new HashMap<>();
        this.random = new Random();
    }

    @Override
    public Optional<Coordinates> findRandomStepFrom(Coordinates coordinates, Set<Class<? extends Entity>> notObstaclesTypes) {
        if (!isMovePossibleFrom(coordinates, notObstaclesTypes)) {
            return Optional.of(coordinates);
        }
        Coordinates nextCoordinates = generateRandomCoordinatesNextTo(coordinates, notObstaclesTypes);
        return Optional.of(nextCoordinates);
    }

    private boolean isMovePossibleFrom(Coordinates coordinates, Set<Class<? extends Entity>> notObstaclesTypes) {
        for (int i = 0; i < OFFSETS_TO_NEIGHBOURS.length; i++) {
            Coordinates toCoordinates = createNeighbouringCoordinates(coordinates, i);
            if (areCoordinatesAvailable(toCoordinates, notObstaclesTypes)) {
                return true;
            }
        }
        return false;
    }

    private boolean areCoordinatesAvailable(Coordinates coordinates, Set<Class<? extends Entity>> notObstaclesTypes) {
        boolean coordinatesValid = areCoordinatesValid(coordinates);
        if (!coordinatesValid) {
            return false;
        }

        boolean coordinatesFree = !worldMap.areBusy(coordinates);
        if (coordinatesFree) {
            return true;
        }

        Entity entity = worldMap.getEntityByCoordinates(coordinates);
        return notObstaclesTypes.contains(entity.getClass());
    }

    private boolean areCoordinatesValid(Coordinates coordinates) {
        int row = coordinates.row();
        int column = coordinates.column();
        int height = worldMap.getHeight();
        int width = worldMap.getWidth();
        return row >= 0 && row < height && column >= 0 && column < width;
    }

    private Coordinates createNeighbouringCoordinates(Coordinates fromCoordinates, int directionIndex) {
        int[] offset = OFFSETS_TO_NEIGHBOURS[directionIndex];
        int row = fromCoordinates.row() + offset[0];
        int column = fromCoordinates.column() + offset[1];
        return new Coordinates(row, column);
    }

    private Coordinates generateRandomCoordinatesNextTo(Coordinates currentCoordinates, Set<Class<? extends Entity>> notObstaclesTypes) {
        Coordinates nextCoordinates;
        do {
            int neighboursIndex = getRandomNeighbourIndex();
            nextCoordinates = createNeighbouringCoordinates(currentCoordinates, neighboursIndex);
        } while (!areCoordinatesAvailable(nextCoordinates, notObstaclesTypes));
        return nextCoordinates;
    }

    private int getRandomNeighbourIndex() {
        return random.nextInt(OFFSETS_TO_NEIGHBOURS.length);
    }

    public List<Coordinates> find(Coordinates start, Coordinates target, Set<Coordinates> obstacles) {
        initializeSearch(start, target);
        boolean foundTarget = false;
        while (!openList.isEmpty() && !foundTarget) {
            int levelCost = openList.peek().cost;
            while (isCurrentLevelCost(levelCost)) {
                PathNode current = openList.poll();
                if (current.heuristicDistance == 0) {
                    foundTarget = true;
                    break;
                }
                foundTarget = canFindTargetFrom(current, target, obstacles);
                visitedCoordinates.add(current.coordinates);
            }
        }
        PathNode targetNode = nodeRegistry.get(target);
        clearSearchStorages();
        return buildPathTo(targetNode);
    }

    private void initializeSearch(Coordinates start, Coordinates target) {
        int heuristicDistance = calculateManhattanDistance(start, target);
        PathNode startNode = new PathNode(start, heuristicDistance, heuristicDistance, null);
        PathNode targetNode = new PathNode(target, 0, Integer.MAX_VALUE, null);
        nodeRegistry.put(start, startNode);
        nodeRegistry.put(target, targetNode);
        openList.offer(startNode);
        visitedCoordinates.add(start);
    }

    private int calculateManhattanDistance(Coordinates from, Coordinates target) {
        return Math.abs(from.row() - target.row()) + Math.abs(from.column() - target.column());
    }

    private boolean isCurrentLevelCost(int levelCost) {
        return !openList.isEmpty() && openList.peek().cost == levelCost;
    }

    private boolean canFindTargetFrom(PathNode current, Coordinates target, Set<Coordinates> obstacles) {
        for (Direction direction : directions) {
            Coordinates nextCoordinates = createCoordinatesForDirection(direction, current);
            if (shouldSkipCoordinates(nextCoordinates, obstacles)) {
                continue;
            }
            if (canReachTargetWithDirection(direction, current, nextCoordinates, target)) {
                return true;
            }
        }
        return false;
    }

    private static Coordinates createCoordinatesForDirection(Direction direction, PathNode currentNode) {
        return new Coordinates(
                currentNode.coordinates.row() + direction.rowOffset(),
                currentNode.coordinates.column() + direction.columnOffset());
    }

    private boolean shouldSkipCoordinates(Coordinates coordinates, Set<Coordinates> obstacles) {
        if (!areCoordinatesWithinBounds(coordinates)) {
            return true;
        }
        if (obstacles.contains(coordinates)) {
            return true;
        }
        return visitedCoordinates.contains(coordinates);
    }

    private boolean areCoordinatesWithinBounds(Coordinates coordinates) {
        int height = worldMap.getHeight();
        int width = worldMap.getWidth();
        return coordinates.row() >= 0 && coordinates.row() < height && coordinates.column() >= 0 && coordinates.column() < width;
    }

    private boolean canReachTargetWithDirection(Direction direction, PathNode currentNode, Coordinates nextCoordinates, Coordinates targetsCoordinates) {
        int heuristicDistance = calculateManhattanDistance(nextCoordinates, targetsCoordinates);
        int nextNodeCost = currentNode.cost + direction.multiplier() + heuristicDistance;
        PathNode nextNode = nodeRegistry.getOrDefault(nextCoordinates, new PathNode(nextCoordinates, heuristicDistance, nextNodeCost, currentNode));
        if (nextNodeCost < nextNode.cost) {
            nextNode.cost = nextNodeCost;
            nextNode.parent = currentNode;
        }
        if (isNodeNewOrImproved(nextNode, heuristicDistance)) {
            nodeRegistry.put(nextCoordinates, nextNode);
            openList.offer(nextNode);
        }
        return heuristicDistance == 0;
    }

    private boolean isNodeNewOrImproved(PathNode node, int heuristicDistance) {
        Coordinates coordinates = node.coordinates;
        return !nodeRegistry.containsKey(coordinates) ||
                nodeRegistry.get(coordinates).heuristicDistance > heuristicDistance;
    }

    private void clearSearchStorages() {
        visitedCoordinates.clear();
        openList.clear();
        nodeRegistry.clear();
    }

    private List<Coordinates> buildPathTo(PathNode targetNode) {
        List<Coordinates> coordinates = new ArrayList<>();
        if (targetNode.parent == null) {
            return coordinates;
        }
        while (targetNode.parent != null) {
            coordinates.add(targetNode.coordinates);
            targetNode = targetNode.parent;
        }
        coordinates.add(targetNode.coordinates);
        coordinates.remove(coordinates.size() - 1);
        Collections.reverse(coordinates);
        return coordinates;
    }

    private static class PathNode {
        private final Coordinates coordinates;
        private PathNode parent;
        private int cost;
        private final int heuristicDistance;

        public PathNode(Coordinates coordinates, int heuristicDistance, int cost, PathNode parent) {
            this.coordinates = coordinates;
            this.heuristicDistance = heuristicDistance;
            this.cost = cost;
            this.parent = parent;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PathNode pathNode = (PathNode) o;
            return Objects.equals(coordinates, pathNode.coordinates);
        }

        @Override
        public int hashCode() {
            return Objects.hash(coordinates);
        }
    }

    private record Direction(int rowOffset, int columnOffset, int multiplier) {
    }
}
