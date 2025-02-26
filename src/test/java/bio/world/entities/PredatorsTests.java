package bio.world.entities;

import bio.world.TestSimulation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
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

    @Test
    @DisplayName("Random step test: predator make random step when cannot find the herbivore")
    public void checkTestcase08() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template11.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int moveCount = 1;
        testSimulation.startPredatorsOnly(moveCount);
        Set<Coordinates> predator1finishCoordinates = Set.of(
                new Coordinates(0, 3),
                new Coordinates(1, 4)
        );
        Set<Coordinates> predator2finishCoordinates = Set.of(
                new Coordinates(5, 2),
                new Coordinates(7, 2)
        );
        boolean predator1InPlace = false;
        boolean predator2InPlace = false;
        for (Coordinates coordinates : predator1finishCoordinates) {
            Optional<Entity> entityContainer = testSimulation.getEntityByCoordinates(coordinates);
            if (entityContainer.isPresent()) {
                predator1InPlace = entityContainer.get() instanceof Predator;
            }
        }
        for (Coordinates coordinates : predator2finishCoordinates) {
            Optional<Entity> entityContainer = testSimulation.getEntityByCoordinates(coordinates);
            if (entityContainer.isPresent()) {
                predator2InPlace = entityContainer.get() instanceof Predator;
            }
        }
        assertTrue(predator1InPlace && predator2InPlace);
    }

    @Test
    @DisplayName("Static test 1: predator stay in place when cannot eat the herbivore and has not free space near")
    public void checkTestcase09() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template12.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int moveCount = 10;
        testSimulation.startPredatorsOnly(moveCount);
        Coordinates predator1finishCoordinates = new Coordinates(0, 4);
        Coordinates predator2finishCoordinates = new Coordinates(6, 2);
        boolean predator1InPlace = false;
        boolean predator2InPlace = false;
        Optional<Entity> entity1Container = testSimulation.getEntityByCoordinates(predator1finishCoordinates);
        if (entity1Container.isPresent()) {
            predator1InPlace = entity1Container.get() instanceof Predator;
        }
        Optional<Entity> entity2Container = testSimulation.getEntityByCoordinates(predator2finishCoordinates);
        if (entity2Container.isPresent()) {
            predator2InPlace = entity2Container.get() instanceof Predator;
        }
        assertTrue(predator1InPlace && predator2InPlace);
    }

    @Test
    @DisplayName("Static test 2: predator stay in place when cannot eat the herbivore and has not free space near")
    public void checkTestcase10() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template13.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int moveCount = 10;
        testSimulation.startPredatorsOnly(moveCount);
        Coordinates predator1finishCoordinates = new Coordinates(0, 2);
        boolean predatorInPlace = false;
        Optional<Entity> entityContainer = testSimulation.getEntityByCoordinates(predator1finishCoordinates);
        if (entityContainer.isPresent()) {
            predatorInPlace = entityContainer.get() instanceof Predator;
        }
        assertTrue(predatorInPlace);
    }

    @Test
    @DisplayName("Static test 3: predator stay in place when cannot eat the herbivore and has not free space near")
    public void checkTestcase11() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template14.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int moveCount = 10;
        testSimulation.startPredatorsOnly(moveCount);
        Coordinates predator1finishCoordinates = new Coordinates(2, 2);
        boolean predatorInPlace = false;
        Optional<Entity> entityContainer = testSimulation.getEntityByCoordinates(predator1finishCoordinates);
        if (entityContainer.isPresent()) {
            predatorInPlace = entityContainer.get() instanceof Predator;
        }
        assertTrue(predatorInPlace);
    }

    @Test
    @DisplayName("Hunger test 1: predator loses its healthPoint if it doesn't eat for too long")
    public void checkTestcase12() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template18.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int moveCount = 102;
        testSimulation.startPredatorsOnly(moveCount);
        Coordinates predatorFinishCoordinates = new Coordinates(2, 2);
        boolean predatorStillAlive = false;
        Optional<Entity> entityContainer = testSimulation.getEntityByCoordinates(predatorFinishCoordinates);
        if (entityContainer.isPresent()) {
            predatorStillAlive = entityContainer.get() instanceof Predator;
        }
        assertTrue(predatorStillAlive);
    }

    @Test
    @DisplayName("Hunger test 2: predator loses its healthPoint if it doesn't eat for too long")
    public void checkTestcase13() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template18.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int moveCount = 103;
        testSimulation.startPredatorsOnly(moveCount);
        Coordinates predatorFinishCoordinates = new Coordinates(2, 2);
        boolean predatorStillAlive = false;
        Optional<Entity> entityContainer = testSimulation.getEntityByCoordinates(predatorFinishCoordinates);
        if (entityContainer.isPresent()) {
            predatorStillAlive = entityContainer.get() instanceof Predator;
        }
        assertFalse(predatorStillAlive);
    }

    @Test
    @DisplayName("Hunger test 3: predator loses its healthPoint if it doesn't eat for too long")
    public void checkTestcase14() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template19.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int moveCount = 103;
        Coordinates predatorFinishCoordinates = new Coordinates(2, 2);
        Optional<Entity> entityContainer = testSimulation.getEntityByCoordinates(predatorFinishCoordinates);
        Predator predator = (Predator) entityContainer.get();
        testSimulation.startPredatorsOnly(moveCount);
        boolean predatorDoesNotExist = testSimulation.areEmptyCoordinates(predator.getCoordinates());
        assertTrue(predatorDoesNotExist);
    }

    @Test
    @DisplayName("Recovery health test 1: predator refills its healthPoint after eat herbivore")
    public void checkTestcase15() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template20.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int moveCount = 179;
        Coordinates predatorStartCoordinates = new Coordinates(0, 0);
        Optional<Entity> entityContainer = testSimulation.getEntityByCoordinates(predatorStartCoordinates);
        Predator predator = (Predator) entityContainer.get();
        testSimulation.startPredatorsOnly(moveCount);
        boolean predatorStillAlive = !testSimulation.areEmptyCoordinates(predator.getCoordinates());
        assertTrue(predatorStillAlive);
    }

    @Test
    @DisplayName("Recovery health test 2: predator refills its healthPoint after eat herbivore")
    public void checkTestcase16() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template20.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int moveCount = 180;
        Coordinates predatorStartCoordinates = new Coordinates(0, 0);
        Optional<Entity> entityContainer = testSimulation.getEntityByCoordinates(predatorStartCoordinates);
        Predator predator = (Predator) entityContainer.get();
        testSimulation.startPredatorsOnly(moveCount);
        boolean predatorStillAlive = !testSimulation.areEmptyCoordinates(predator.getCoordinates());
        assertTrue(predatorStillAlive);
    }

    @Test
    @DisplayName("Recovery health test 3: predator refills its healthPoint partially after eat herbivore, who are hunger")
    public void checkTestcase17() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template21.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int moveCount = 144;
        Coordinates predatorStartCoordinates = new Coordinates(1, 0);
        Optional<Entity> entityContainer = testSimulation.getEntityByCoordinates(predatorStartCoordinates);
        Predator predator = (Predator) entityContainer.get();
        testSimulation.startWithMoveCount(moveCount);
        boolean predatorStillAlive = !testSimulation.areEmptyCoordinates(predator.getCoordinates());
        assertTrue(predatorStillAlive);
    }

    @Test
    @DisplayName("Recovery health test 4: predator refills its healthPoint partially after eat herbivore, who are hunger")
    public void checkTestcase18() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template21.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int moveCount = 145;
        Coordinates predatorStartCoordinates = new Coordinates(1, 0);
        Optional<Entity> entityContainer = testSimulation.getEntityByCoordinates(predatorStartCoordinates);
        Predator predator = (Predator) entityContainer.get();
        testSimulation.startWithMoveCount(moveCount);
        boolean predatorDoesNotExist = testSimulation.areEmptyCoordinates(predator.getCoordinates());
        assertTrue(predatorDoesNotExist);
    }
}
