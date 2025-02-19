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
        testSimulation.startHerbivoresOnlyWithoutSpeed(moveCount);
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

    @Test
    @DisplayName("Speed test: herbivore stopped in front of the grass")
    public void checkTestcase02() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template05.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int moveCount = 14;
        testSimulation.startHerbivoresOnly(moveCount);
        Coordinates fihishCoordinates = new Coordinates(0, 1);
        assertTrue(testSimulation.getEntityByCoordinates(fihishCoordinates).get() instanceof Herbivore);
    }

    @Test
    @DisplayName("Speed test: grass still exists")
    public void checkTestcase03() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template05.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int moveCount = 14;
        testSimulation.startHerbivoresOnly(moveCount);
        Coordinates grassCoordinates = new Coordinates(0, 0);
        assertTrue(testSimulation.getEntityByCoordinates(grassCoordinates).get() instanceof Grass);
    }

    @Test
    @DisplayName("Speed test: herbivore ate the grass")
    public void checkTestcase04() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template05.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int moveCount = 15;
        testSimulation.startHerbivoresOnly(moveCount);
        Coordinates fihishCoordinates = new Coordinates(0, 0);
        assertTrue(testSimulation.getEntityByCoordinates(fihishCoordinates).get() instanceof Herbivore);
    }
}
