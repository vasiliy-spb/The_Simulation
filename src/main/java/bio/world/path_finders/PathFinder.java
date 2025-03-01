package bio.world.path_finders;

import bio.world.entities.Coordinates;
import bio.world.entities.Entity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PathFinder {
    List<Coordinates> find(Coordinates fromCoordinates, Coordinates toCoordinates, Set<Coordinates> obstacles);

    Optional<Coordinates> findRandomStepFrom(Coordinates fromCoordinates, Set<Class<? extends Entity>> notObstaclesTypes);
}
