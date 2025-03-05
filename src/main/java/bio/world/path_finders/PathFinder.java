package bio.world.path_finders;

import bio.world.entities.Coordinates;
import bio.world.entities.Entity;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public interface PathFinder {
    List<Coordinates> find(Coordinates fromCoordinates, Coordinates toCoordinates, Set<Coordinates> obstacles);

    Optional<Coordinates> findRandomStepFrom(Coordinates fromCoordinates, Predicate<Entity> notObstaclesForMoveChecker);
}
