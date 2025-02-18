package bio.world.path_finders;

import bio.world.Coordinates;

import java.util.List;

public interface PathFinder {
    List<Coordinates> find(Coordinates fromCoordinates, Coordinates toCoordinates);
}
