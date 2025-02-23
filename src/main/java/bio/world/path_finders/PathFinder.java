package bio.world.path_finders;

import bio.world.entities.Coordinates;

import java.util.List;
import java.util.Optional;

public interface PathFinder {
    List<Coordinates> find(Coordinates fromCoordinates, Coordinates toCoordinates);

    Optional<Coordinates> findRandomStepFrom(Coordinates fromCoordinates);
}
