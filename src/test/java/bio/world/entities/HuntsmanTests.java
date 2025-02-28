package bio.world.entities;

import bio.world.TestSimulation;
import bio.world.entities.regular.Herbivore;
import bio.world.entities.regular.Predator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class HuntsmanTests {

//    @Test
//    @DisplayName("Huntsmen hunt to Predator early Herbivore")
//    public void checkTestcase01() {
//        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template22.txt";
//        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
//        int moveCount = 16;
//        Coordinates predator1Coordinates = new Coordinates(17, 2);
//        Coordinates predator2Coordinates = new Coordinates(12, 7);
//        Coordinates herbivore1Coordinates = new Coordinates(13, 2);
//        Coordinates herbivore2Coordinates = new Coordinates(15, 7);
//
//        Optional<Entity> predator1Container = testSimulation.getEntityByCoordinates(predator1Coordinates);
//        Predator predator1 = (Predator) predator1Container.get();
//
//        Optional<Entity> predator2Container = testSimulation.getEntityByCoordinates(predator2Coordinates);
//        Predator predator2 = (Predator) predator2Container.get();
//
//        Optional<Entity> herbivore1Container = testSimulation.getEntityByCoordinates(herbivore1Coordinates);
//        Herbivore herbivore1 = (Herbivore) herbivore1Container.get();
//
//        Optional<Entity> herbivore2Container = testSimulation.getEntityByCoordinates(herbivore2Coordinates);
//        Herbivore herbivore2 = (Herbivore) herbivore2Container.get();
//
////        Huntsmen huntsmen = (Huntsmen) testSimulation.getEntityByCoordinates(new Coordinates(14, 4)).get();
//
//        testSimulation.startHuntsmenOnly(moveCount);
//
//        assertTrue(!predator1.isAlive() && !predator2.isAlive() && herbivore1.isAlive() && herbivore2.isAlive());
//    }

    @Test
    @DisplayName("Huntsmen hunt to Predator early Herbivore")
    public void checkTestcase02() {
        String worldMapTemplate = "src/test/java/bio/world/entities/templates/template23.txt";
        TestSimulation testSimulation = new TestSimulation(worldMapTemplate);
        int moveCount = 200;

        Set<Herbivore> herbivores = new HashSet<>();
        Set<Predator> predators = new HashSet<>();

        int side = 13;
        for (int row = 0; row < side; row++) {
            for (int column = 0; column < side; column++) {
                Coordinates coordinates = new Coordinates(row, column);
                Optional<Entity> entityContainer = testSimulation.getEntityByCoordinates(coordinates);
                if (entityContainer.isEmpty()) {
                    continue;
                }
                Entity entity = entityContainer.get();
                if (entity instanceof Herbivore herbivore) {
                    herbivores.add(herbivore);
                }
                if (entity instanceof Predator predator) {
                    predators.add(predator);
                }
            }
        }

        testSimulation.startHuntsmenOnly(moveCount);

        boolean allHerbivoresStillAlive = true;
        for (Herbivore herbivore : herbivores) {
            allHerbivoresStillAlive &= herbivore.isAlive();
        }

        assertTrue(allHerbivoresStillAlive);
    }
}
