package bio.world.entities;

import bio.world.Coordinates;
import bio.world.TestSimulation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
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

    @Test
    @DisplayName("Random step test: herbivore make random step when cannot eat the grass")
    public void checkTestcase05() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template07.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int moveCount = 1;
        testSimulation.startHerbivoresOnly(moveCount);
        Set<Coordinates> herbivores1finishCoordinates = Set.of(
                new Coordinates(0, 3),
                new Coordinates(1, 4)
        );
        Set<Coordinates> herbivores2finishCoordinates = Set.of(
                new Coordinates(5, 2),
                new Coordinates(7, 2)
        );
        boolean herbivore1InPlace = false;
        boolean herbivore2InPlace = false;
        for (Coordinates coordinates : herbivores1finishCoordinates) {
            Optional<Entity> entityContainer = testSimulation.getEntityByCoordinates(coordinates);
            if (entityContainer.isPresent()) {
                herbivore1InPlace = entityContainer.get() instanceof Herbivore;
            }
        }
        for (Coordinates coordinates : herbivores2finishCoordinates) {
            Optional<Entity> entityContainer = testSimulation.getEntityByCoordinates(coordinates);
            if (entityContainer.isPresent()) {
                herbivore2InPlace = entityContainer.get() instanceof Herbivore;
            }
        }
        assertTrue(herbivore1InPlace && herbivore2InPlace);
    }

    @Test
    @DisplayName("Static test 1: herbivore stay in place when cannot eat the grass and has not free space near")
    public void checkTestcase06() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template08.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int moveCount = 10;
        testSimulation.startHerbivoresOnly(moveCount);
        Coordinates herbivores1finishCoordinates = new Coordinates(0, 4);
        Coordinates herbivores2finishCoordinates = new Coordinates(6, 2);
        boolean herbivore1InPlace = false;
        boolean herbivore2InPlace = false;
        Optional<Entity> entity1Container = testSimulation.getEntityByCoordinates(herbivores1finishCoordinates);
        if (entity1Container.isPresent()) {
            herbivore1InPlace = entity1Container.get() instanceof Herbivore;
        }
        Optional<Entity> entity2Container = testSimulation.getEntityByCoordinates(herbivores2finishCoordinates);
        if (entity2Container.isPresent()) {
            herbivore2InPlace = entity2Container.get() instanceof Herbivore;
        }
        assertTrue(herbivore1InPlace && herbivore2InPlace);
    }

    @Test
    @DisplayName("Static test 2: herbivore stay in place when cannot eat the grass and has not free space near")
    public void checkTestcase07() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template09.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int moveCount = 10;
        testSimulation.startHerbivoresOnly(moveCount);
        Coordinates herbivoresFinishCoordinates = new Coordinates(4, 2);
        boolean herbivoreInPlace = false;
        Optional<Entity> entityContainer = testSimulation.getEntityByCoordinates(herbivoresFinishCoordinates);
        if (entityContainer.isPresent()) {
            herbivoreInPlace = entityContainer.get() instanceof Herbivore;
        }
        assertTrue(herbivoreInPlace);
    }

    @Test
    @DisplayName("Static test 3: herbivore stay in place when cannot eat the grass and has not free space near")
    public void checkTestcase08() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template10.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int moveCount = 10;
        testSimulation.startHerbivoresOnly(moveCount);
        Coordinates herbivoresFinishCoordinates = new Coordinates(2, 2);
        boolean herbivoreInPlace = false;
        Optional<Entity> entityContainer = testSimulation.getEntityByCoordinates(herbivoresFinishCoordinates);
        if (entityContainer.isPresent()) {
            herbivoreInPlace = entityContainer.get() instanceof Herbivore;
        }
        assertTrue(herbivoreInPlace);
    }

    @Test
    @DisplayName("Hunger test 1: herbivore loses its healthPoint if it doesn't eat for too long")
    public void checkTestcase09() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template15.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int moveCount = 48;
        testSimulation.startHerbivoresOnly(moveCount);
        Coordinates herbivoresFinishCoordinates = new Coordinates(2, 2);
        boolean herbivoreStillAlive = false;
        Optional<Entity> entityContainer = testSimulation.getEntityByCoordinates(herbivoresFinishCoordinates);
        if (entityContainer.isPresent()) {
            herbivoreStillAlive = entityContainer.get() instanceof Herbivore;
        }
        assertTrue(herbivoreStillAlive);
    }

    @Test
    @DisplayName("Hunger test 2: herbivore loses its healthPoint if it doesn't eat for too long")
    public void checkTestcase10() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template15.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int moveCount = 49;
        testSimulation.startHerbivoresOnly(moveCount);
        Coordinates herbivoresFinishCoordinates = new Coordinates(2, 2);
        boolean herbivoreStillAlive = false;
        Optional<Entity> entityContainer = testSimulation.getEntityByCoordinates(herbivoresFinishCoordinates);
        if (entityContainer.isPresent()) {
            herbivoreStillAlive = entityContainer.get() instanceof Herbivore;
        }
        assertFalse(herbivoreStillAlive);
    }

    @Test
    @DisplayName("Hunger test 3: herbivore loses its healthPoint if it doesn't eat for too long")
    public void checkTestcase11() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template16.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int moveCount = 49;
        Coordinates herbivoresStartCoordinates = new Coordinates(2, 2);
        Optional<Entity> entityContainer = testSimulation.getEntityByCoordinates(herbivoresStartCoordinates);
        Herbivore herbivore = (Herbivore) entityContainer.get();
        testSimulation.startHerbivoresOnly(moveCount);
        boolean herbivoreDoesNotExist = testSimulation.areEmptyCoordinates(herbivore.getCoordinates());
        assertTrue(herbivoreDoesNotExist);
    }

    @Test
    @DisplayName("Recovery health test 1: herbivore refills its healthPoint after eat grass")
    public void checkTestcase12() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template17.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int moveCount = 68;
        Coordinates herbivoresStartCoordinates = new Coordinates(0, 0);
        Optional<Entity> entityContainer = testSimulation.getEntityByCoordinates(herbivoresStartCoordinates);
        Herbivore herbivore = (Herbivore) entityContainer.get();
        testSimulation.startHerbivoresOnly(moveCount);
        boolean herbivoreStillAlive = !testSimulation.areEmptyCoordinates(herbivore.getCoordinates());
        assertTrue(herbivoreStillAlive);
    }

    @Test
    @DisplayName("Recovery health test 2: herbivore refills its healthPoint after eat grass")
    public void checkTestcase13() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template17.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int moveCount = 69;
        Coordinates herbivoresStartCoordinates = new Coordinates(0, 0);
        Optional<Entity> entityContainer = testSimulation.getEntityByCoordinates(herbivoresStartCoordinates);
        Herbivore herbivore = (Herbivore) entityContainer.get();
        testSimulation.startHerbivoresOnly(moveCount);
        boolean herbivoreDoesNotExist = testSimulation.areEmptyCoordinates(herbivore.getCoordinates());
        assertTrue(herbivoreDoesNotExist);
    }
}
