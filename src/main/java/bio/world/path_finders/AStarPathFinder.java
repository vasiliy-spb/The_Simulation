package bio.world.path_finders;

import bio.world.Coordinates;
import bio.world.WorldMap;
import bio.world.entities.Entity;

import java.util.*;

public class AStarPathFinder implements PathFinder {
    private final WorldMap worldMap;
    private final Set<Coordinates> visited;
    private final Queue<PathNode> openList;
    private final Map<Coordinates, PathNode> nodeMap;

    public AStarPathFinder(WorldMap worldMap) {
        this.worldMap = worldMap;
        this.visited = new HashSet<>();
        this.openList = new PriorityQueue<>((p1, p2) -> Integer.compare(p1.value, p2.value));
        this.nodeMap = new HashMap<>();
    }

    public List<Coordinates> find(Coordinates start, Coordinates target) {
        Entity entity = worldMap.getEntityByCoordinates(start);
        Set<Coordinates> obstacles = worldMap.getObstaclesCoordinatesFor(entity);
        int height = worldMap.getHeight();
        int width = worldMap.getWidth();
        int approximateDistanceToTarget = calculateApproximateDistance(start, target);
        PathNode startNode = new PathNode(start, approximateDistanceToTarget, approximateDistanceToTarget, null);
        PathNode targetNode = new PathNode(target, 0, Integer.MAX_VALUE, null);
        nodeMap.put(start, startNode);
        nodeMap.put(target, targetNode);
        openList.offer(startNode);
        visited.add(start);
        one:
        while (!openList.isEmpty()) {
            int levelValue = openList.peek().value;
            while (!openList.isEmpty() && openList.peek().value == levelValue) {
                PathNode current = openList.poll();
                if (current.approximateDistanceToTarget == 0) {
                    break one;
                }
                if (current.coordinates.equals(target)) {
                    break;
                }
                for (Direction direction : directions) {
                    Coordinates nextCoordinates = new Coordinates(current.coordinates.row() + direction.deltaRow(), current.coordinates.column() + direction.deltaColumn());
                    if (!isPresent(nextCoordinates, height, width)) {
                        continue;
                    }
                    if (obstacles.contains(nextCoordinates)) {
                        continue;
                    }
                    if (visited.contains(nextCoordinates)) {
                        continue;
                    }
                    int approximateDistance = calculateApproximateDistance(nextCoordinates, target);
                    int value = current.value + direction.multiplier() + approximateDistance;
                    PathNode nextNode = nodeMap.getOrDefault(nextCoordinates, new PathNode(nextCoordinates, approximateDistance, value, current));
                    if (value < nextNode.value) {
                        nextNode.value = value;
                        nextNode.parent = current;
                    }
                    if (!nodeMap.containsKey(nextCoordinates) || nodeMap.get(nextCoordinates).approximateDistanceToTarget > approximateDistance) {
                        nodeMap.put(nextCoordinates, nextNode);
                        openList.offer(nextNode);
                    }
                    if (approximateDistance == 0) {
                        break one;
                    }
                }
                visited.add(current.coordinates);
            }
        }

        visited.clear();
        openList.clear();
        nodeMap.clear();

        return createPathOfCoordinateTo(targetNode);
    }

    private List<Coordinates> createPathOfCoordinateTo(PathNode node) {
        List<Coordinates> coordinates = new ArrayList<>();
        if (node.parent == null) {
            System.out.println("There is no path..");
            return coordinates;
        }
        while (node.parent != null) {
            coordinates.add(node.coordinates);
            node = node.parent;
        }
        coordinates.add(node.coordinates);
        coordinates.remove(coordinates.size() - 1);
        Collections.reverse(coordinates);
        return coordinates;
    }

    private int calculateApproximateDistance(Coordinates from, Coordinates target) {
        return Math.abs(from.row() - target.row()) + Math.abs(from.column() - target.column());
    }

    private boolean isPresent(Coordinates coordinates, int height, int width) {
        return coordinates.row() >= 0 && coordinates.row() < height && coordinates.column() >= 0 && coordinates.column() < width;
    }

    private static class PathNode {
        Coordinates coordinates;
        private PathNode parent;
        private int value;
        private final int approximateDistanceToTarget;

        public PathNode(Coordinates coordinates, int approximateDistance, int value, PathNode parent) {
            this.coordinates = coordinates;
            this.approximateDistanceToTarget = approximateDistance;
            this.value = value;
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

        @Override
        public String toString() {
            return "PathNode{" +
                    "coordinates=" + coordinates +
                    ", value=" + value +
                    ", approximateDistanceToTarget=" + approximateDistanceToTarget +
                    '}';
        }
    }

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

    private record Direction(int deltaRow, int deltaColumn, int multiplier) {
    }
}
