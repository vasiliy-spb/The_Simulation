package bio.world.entities;

import bio.world.Coordinates;
import bio.world.WorldMap;
import bio.world.path_finders.PathFinder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Herbivore extends Creature implements Hunter<Grass> {
    public Herbivore(Coordinates coordinates) {
        super(coordinates);
    }

    @Override
    public void makeMove(WorldMap worldMap, PathFinder pathFinder) {
        Optional<Grass> target = findNearestTarget(worldMap);
        if (target.isEmpty()) {
            throw new RuntimeException("Herbivore didn't find Grass.");
        }
        Grass grass = target.get();
        Coordinates nextCoordinates;
        if (canEat(grass)) {
            grass.takeDamage(this);
            nextCoordinates = grass.getCoordinates();
            worldMap.removeStaticEntity(grass);
        } else {
            List<Coordinates> path = pathFinder.find(this.coordinates, grass.getCoordinates());
//            System.out.println("For: " + coordinates + ", path = " + path);
            if (path.isEmpty()) {
                return;
            }
            nextCoordinates = path.get(0);
        }
        worldMap.moveCreature(this.coordinates, nextCoordinates);
        this.setCoordinates(nextCoordinates);
    }

    private Optional<Grass> findNearestTarget(WorldMap worldMap) {
        Set<StaticEntity> staticEntities = worldMap.getStaticEntities();
        List<Grass> possibleTargetList = staticEntities
                .stream()
                .filter(e -> e instanceof Grass)
                .map(e -> (Grass) e)
                .toList();
        int minHeuristicDistance = worldMap.getHeight() + worldMap.getWidth();
        Grass target = null;
        for (Grass grass : possibleTargetList) {
            int currentHeuristicDistance = Math.abs(this.coordinates.row() - grass.getCoordinates().row()) + Math.abs(this.coordinates.column() - grass.getCoordinates().column());
            if (currentHeuristicDistance < minHeuristicDistance) {
                minHeuristicDistance = currentHeuristicDistance;
                target = grass;
            }
        }
        return Optional.ofNullable(target);
    }

    private boolean canEat(Grass grass) {
        int rowDiff = Math.abs(this.coordinates.row() - grass.getCoordinates().row());
        int columnDiff = Math.abs(this.coordinates.column() - grass.getCoordinates().column());
        return rowDiff < 2 && columnDiff < 2;
    }

    @Override
    public int getDamage() {
        return 10;
    }
}
