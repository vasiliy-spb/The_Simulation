package bio.world.entities;

import bio.world.Coordinates;
import bio.world.TestSimulation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PredatorsTests {

    @Test
    @DisplayName("The predator found herbivore")
    public void checkTestcase01() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template01.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int moveCount = 9;
        testSimulation.startPredatorsOnlyWithoutSpeed(moveCount);
        Coordinates coordinates = new Coordinates(7, 4);
        Optional<Entity> expectedEntity = testSimulation.getEntityByCoordinates(coordinates);
        assertTrue(expectedEntity.isPresent() && expectedEntity.get() instanceof Predator);
    }

    @Test
    @DisplayName("The predator tramples the grass")
    public void checkTestcase02() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template01.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int moveCount = 9;
        testSimulation.startPredatorsOnlyWithoutSpeed(moveCount);
        Coordinates grassCoordinates1 = new Coordinates(0, 1);
        Coordinates grassCoordinates2 = new Coordinates(1, 2);
        Coordinates grassCoordinates3 = new Coordinates(5, 4);
        Coordinates grassCoordinates4 = new Coordinates(6, 4);
        assertTrue(
                testSimulation.areEmptyCoordinates(grassCoordinates1)
                        && testSimulation.areEmptyCoordinates(grassCoordinates2)
                        && testSimulation.areEmptyCoordinates(grassCoordinates3)
                        && testSimulation.areEmptyCoordinates(grassCoordinates4)
        );
    }

    @Test
    @DisplayName("The predator change the target")
    public void checkTestcase03() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template03.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int moveCount = 10;
        testSimulation.startHerbivoreFirstWithoutSpeed(moveCount);
        Coordinates predatorFinishCoordinates = new Coordinates(6, 5);
        Optional<Entity> expectedEntity = testSimulation.getEntityByCoordinates(predatorFinishCoordinates);
        assertTrue(expectedEntity.isPresent() && expectedEntity.get() instanceof Predator);
    }

    @Test
    @DisplayName("Speed test: predator stopped in front of the herbivore")
    public void checkTestcase04() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template06.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int moveCount = 21;
        testSimulation.startPredatorsOnly(moveCount);
        Coordinates fihishCoordinates = new Coordinates(6, 4);
        assertTrue(testSimulation.getEntityByCoordinates(fihishCoordinates).get() instanceof Predator);
    }

    @Test
    @DisplayName("Speed test: herbivore still alive")
    public void checkTestcase05() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template06.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int moveCount = 21;
        testSimulation.startPredatorsOnly(moveCount);
        Coordinates herbivoresCoordinates = new Coordinates(7, 4);
        assertTrue(testSimulation.getEntityByCoordinates(herbivoresCoordinates).get() instanceof Herbivore);
    }

    @Test
    @DisplayName("Speed test: predator ate the herbivore")
    public void checkTestcase06() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template06.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int moveCount = 24;
        testSimulation.startPredatorsOnly(moveCount);
        Coordinates fihishCoordinates = new Coordinates(7, 4);
        assertTrue(testSimulation.getEntityByCoordinates(fihishCoordinates).get() instanceof Herbivore);
    }

    @Test
    @DisplayName("Speed test: predator ate the herbivore")
    public void checkTestcase07() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template06.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int moveCount = 25;
        testSimulation.startPredatorsOnly(moveCount);
        Coordinates fihishCoordinates = new Coordinates(7, 4);
        assertTrue(testSimulation.getEntityByCoordinates(fihishCoordinates).get() instanceof Predator);
    }
}
