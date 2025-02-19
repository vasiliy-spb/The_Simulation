package bio.world.entities;

import bio.world.Coordinates;
import bio.world.TestSimulation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HerbivoresTests {

    @Test
    @DisplayName("The herbivore found all grasses")
    public void checkTestcase01() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template04.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int height = worldMapTemplate.split("\n").length;
        int width = worldMapTemplate.split("\n")[0].trim().split(" ").length;
        int moveCount = 28;
        testSimulation.startHerbivoresOnly(moveCount);
        boolean hasNotGrass = true;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {

                Coordinates coordinates = new Coordinates(i, j);
                Optional<Entity> currentEntity = testSimulation.getEntityByCoordinates(coordinates);
                hasNotGrass = currentEntity.isEmpty() || !(currentEntity.get() instanceof Grass);
            }
        }
        assertTrue(hasNotGrass);
    }
}
