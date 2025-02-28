package bio.world.entities;

import bio.world.entities.regular.HuntsmenSight;
import bio.world.entities.regular.Huntsmen;
import bio.world.entities.regular.Predator;
import bio.world.entities.statical.Rock;
import bio.world.entities.statical.Tree;
import bio.world.map.WorldMap;
import bio.world.render.ConsoleMapRender;
import bio.world.render.WorldMapRender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HuntsmenSightTests {
    private WorldMap worldMap;
    private HuntsmenSight huntsmenSight;
    private WorldMapRender mapRender;

    @BeforeEach
    public void initMap() {
        this.worldMap = new WorldMap(7, 7);
        this.huntsmenSight = new HuntsmenSight();
        this.mapRender = new ConsoleMapRender(worldMap);
    }

    @Test
    public void checkTestcase00() {
        /*
         .. .. .. .. .. .. ..
         .. .. .. .. .. .. ..
         .. .. 🐅 🐅 🐅 .. ..
         .. .. 🐅 🤠 🐅 .. ..
         .. .. 🐅 🐅 🐅 .. ..
         .. .. .. .. .. .. ..
         .. .. .. .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        boolean canAim = true;
        for (int row = -1; row <= 1; row++) {
            for (int column = -1; column <= 1; column++) {
                int predatorRow = huntsmenCoordinates.row() + row;
                int predatorColumn = huntsmenCoordinates.column() + column;
                Coordinates predatorCoordinates = new Coordinates(predatorRow, predatorColumn);
                if (predatorCoordinates.equals(huntsmenCoordinates)) {
                    continue;
                }
                Predator predator = new Predator(predatorCoordinates);
                worldMap.addEntity(predator);

                canAim &= !huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap);
                mapRender.renderMap();

                worldMap.removeEntity(predator);
            }
        }

        assertTrue(canAim);
    }

    @Test
    public void checkTestcase01() {
        /*
         .. .. .. .. .. .. ..
         .. 🐅 .. 🐅 .. 🐅 ..
         .. .. .. .. .. .. ..
         .. 🐅 .. 🤠 .. 🐅 ..
         .. .. .. .. .. .. ..
         .. 🐅 .. 🐅 .. 🐅 ..
         .. .. .. .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        boolean canAim = true;
        for (int row = -2; row <= 2; row += 2) {
            for (int column = -2; column <= 2; column += 2) {
                int predatorRow = huntsmenCoordinates.row() + row;
                int predatorColumn = huntsmenCoordinates.column() + column;
                Coordinates predatorCoordinates = new Coordinates(predatorRow, predatorColumn);
                if (predatorCoordinates.equals(huntsmenCoordinates)) {
                    continue;
                }
                Predator predator = new Predator(predatorCoordinates);
                worldMap.addEntity(predator);

                canAim &= !huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap);
                mapRender.renderMap();

                worldMap.removeEntity(predator);
            }
        }

        assertTrue(canAim);
    }

    @Test
    public void checkTestcase02() {
        /*
         .. .. .. .. .. .. ..
         .. 🐅 .. 🐅 .. 🐅 ..
         .. .. 🌲 🌲 🌲 .. ..
         .. 🐅 🌲 🤠 🌲 🐅 ..
         .. .. 🌲 🌲 🌲 .. ..
         .. 🐅 .. 🐅 .. 🐅 ..
         .. .. .. .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        boolean canAim = false;
        for (int row = -1; row <= 1; row++) {
            for (int column = -1; column <= 1; column++) {
                int predatorRow = huntsmenCoordinates.row() + row * 2;
                int predatorColumn = huntsmenCoordinates.column() + column * 2;
                Coordinates predatorCoordinates = new Coordinates(predatorRow, predatorColumn);
                if (predatorCoordinates.equals(huntsmenCoordinates)) {
                    continue;
                }
                Predator predator = new Predator(predatorCoordinates);
                worldMap.addEntity(predator);

                int treeRow = huntsmenCoordinates.row() + row;
                int treeColumn = huntsmenCoordinates.column() + column;
                Coordinates treeCoordinates = new Coordinates(treeRow, treeColumn);
                if (treeCoordinates.equals(huntsmenCoordinates)) {
                    continue;
                }
                Tree tree = new Tree(treeCoordinates);
                worldMap.addEntity(tree);

                canAim |= !huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap);
                mapRender.renderMap();

                worldMap.removeEntity(predator);
                worldMap.removeEntity(tree);
            }
        }

        assertFalse(canAim);
    }

    // ==================================== RIGHT-DOWN SQUARE ====================================
    @Test
    public void checkTestcase010() {
        /*
         🤠 ++ ++ 🐅
         .. .. .. ..
         .. .. .. ..
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(3, 6);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(3, 5);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase011() {
        /*
         🤠 ++ ++ 🐅
         .. .. .. ..
         .. .. .. ..
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(3, 6);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(3, 4);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase012() {
        /*
         🤠 ++ ++ 🐅
         .. .. .. ..
         .. .. .. ..
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(3, 6);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        for (int column = 3; column < 7; column++) {
            Coordinates rockCoordinates = new Coordinates(2, column);
            Rock rock = new Rock(rockCoordinates);
            worldMap.addEntity(rock);

            Coordinates treeCoordinates = new Coordinates(4, column);
            Tree tree = new Tree(treeCoordinates);
            worldMap.addEntity(tree);
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase020() {
        /*
         🤠 .. .. ..
         .. ++ .. ..
         .. .. ++ ..
         .. .. .. 🐅
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(6, 6);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(5, 5);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase021() {
        /*
         🤠 .. .. ..
         .. ++ .. ..
         .. .. ++ ..
         .. .. .. 🐅
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(6, 6);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(4, 4);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase022() {
        /*
         🤠 .. .. ..
         .. ++ .. ..
         .. .. ++ ..
         .. .. .. 🐅
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(6, 6);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        for (int row = 2; row < 7; row++) {
            for (int column = 2; column < 7; column++) {
                if (row == column) {
                    continue;
                }
                Coordinates rockCoordinates = new Coordinates(row, column);
                Rock rock = new Rock(rockCoordinates);
                worldMap.addEntity(rock);
            }
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase030() {
        /*
         🤠 .. .. ..
         ++ .. .. ..
         ++ .. .. ..
         🐅 .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(6, 3);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(5, 3);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase031() {
        /*
         🤠 .. .. ..
         ++ .. .. ..
         ++ .. .. ..
         🐅 .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(6, 3);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(4, 3);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase032() {
        /*
         🤠 .. .. ..
         ++ .. .. ..
         ++ .. .. ..
         🐅 .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(6, 3);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        for (int row = 2; row < 7; row++) {
            Coordinates rockCoordinates = new Coordinates(row, 2);
            Rock rock = new Rock(rockCoordinates);
            worldMap.addEntity(rock);

            Coordinates treeCoordinates = new Coordinates(row, 4);
            Tree tree = new Tree(treeCoordinates);
            worldMap.addEntity(tree);
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase040() {
        /*
         🤠 ++ .. ..
         .. ++ 🐅 ..
         .. .. .. ..
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(4, 5);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(3, 4);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase041() {
        /*
         🤠 ++ .. ..
         .. ++ 🐅 ..
         .. .. .. ..
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(4, 5);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(4, 4);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase042() {
        /*
         🤠 ++ .. ..
         .. ++ 🐅 ..
         .. .. .. ..
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(4, 5);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Set<Coordinates> exceptCoordinates = Set.of(
                huntsmenCoordinates,
                predatorCoordinates,
                new Coordinates(3, 4),
                new Coordinates(4, 4)
        );

        for (int row = 2; row < 7; row++) {
            for (int column = 2; column < 7; column++) {
                Coordinates rockCoordinates = new Coordinates(row, column);
                if (exceptCoordinates.contains(rockCoordinates)) {
                    continue;
                }
                Rock rock = new Rock(rockCoordinates);
                worldMap.addEntity(rock);
            }
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase050() {
        /*
         🤠 .. .. ..
         ++ ++ .. ..
         .. 🐅 .. ..
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(5, 4);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(4, 3);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase051() {
        /*
         🤠 .. .. ..
         ++ ++ .. ..
         .. 🐅 .. ..
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(5, 4);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(4, 4);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase052() {
        /*
         🤠 .. .. ..
         ++ ++ .. ..
         .. 🐅 .. ..
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(5, 4);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Set<Coordinates> exceptCoordinates = Set.of(
                huntsmenCoordinates,
                predatorCoordinates,
                new Coordinates(4, 3),
                new Coordinates(4, 4)
        );

        for (int row = 2; row < 7; row++) {
            for (int column = 2; column < 7; column++) {
                Coordinates treeCoordinates = new Coordinates(row, column);
                if (exceptCoordinates.contains(treeCoordinates)) {
                    continue;
                }
                Tree tree = new Tree(treeCoordinates);
                worldMap.addEntity(tree);
            }
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase060() {
        /*
         🤠 ++ .. ..
         .. .. ++ 🐅
         .. .. .. ..
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(4, 6);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(3, 4);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase061() {
        /*
         🤠 ++ .. ..
         .. .. ++ 🐅
         .. .. .. ..
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(4, 6);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(4, 5);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase062() {
        /*
         🤠 ++ .. ..
         .. .. ++ 🐅
         .. .. .. ..
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(4, 6);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Set<Coordinates> exceptCoordinates = Set.of(
                huntsmenCoordinates,
                predatorCoordinates,
                new Coordinates(3, 4),
                new Coordinates(4, 5)
        );

        for (int row = 2; row < 7; row++) {
            for (int column = 2; column < 7; column++) {
                Coordinates treeCoordinates = new Coordinates(row, column);
                if (exceptCoordinates.contains(treeCoordinates)) {
                    continue;
                }
                Tree tree = new Tree(treeCoordinates);
                worldMap.addEntity(tree);
            }
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase070() {
        /*
         🤠 .. .. ..
         ++ .. .. ..
         .. ++ .. ..
         .. 🐅 .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(6, 4);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(4, 3);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase071() {
        /*
         🤠 .. .. ..
         ++ .. .. ..
         .. ++ .. ..
         .. 🐅 .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(6, 4);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(5, 4);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase072() {
        /*
         🤠 .. .. ..
         ++ .. .. ..
         .. ++ .. ..
         .. 🐅 .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(6, 4);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Set<Coordinates> exceptCoordinates = Set.of(
                huntsmenCoordinates,
                predatorCoordinates,
                new Coordinates(4, 3),
                new Coordinates(5, 4)
        );

        for (int row = 2; row < 7; row++) {
            for (int column = 2; column < 7; column++) {
                Coordinates treeCoordinates = new Coordinates(row, column);
                if (exceptCoordinates.contains(treeCoordinates)) {
                    continue;
                }
                Tree tree = new Tree(treeCoordinates);
                worldMap.addEntity(tree);
            }
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase080() {
        /*
         🤠 .. .. ..
         .. ++ ++ ..
         .. .. ++ 🐅
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(5, 6);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(4, 4);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase081() {
        /*
         🤠 .. .. ..
         .. ++ ++ ..
         .. .. ++ 🐅
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(5, 6);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(4, 5);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase082() {
        /*
         🤠 .. .. ..
         .. ++ ++ ..
         .. .. ++ 🐅
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(5, 6);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(5, 5);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase083() {
        /*
         🤠 .. .. ..
         .. ++ ++ ..
         .. .. ++ 🐅
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(5, 6);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Set<Coordinates> exceptCoordinates = Set.of(
                huntsmenCoordinates,
                predatorCoordinates,
                new Coordinates(4, 5),
                new Coordinates(4, 4),
                new Coordinates(5, 5)
        );

        for (int row = 2; row < 7; row++) {
            for (int column = 2; column < 7; column++) {
                Coordinates treeCoordinates = new Coordinates(row, column);
                if (exceptCoordinates.contains(treeCoordinates)) {
                    continue;
                }
                Tree tree = new Tree(treeCoordinates);
                worldMap.addEntity(tree);
            }
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase090() {
        /*
         🤠 .. .. ..
         .. ++ .. ..
         .. ++ ++ ..
         .. .. 🐅 ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(6, 5);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(4, 4);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase091() {
        /*
         🤠 .. .. ..
         .. ++ .. ..
         .. ++ ++ ..
         .. .. 🐅 ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(6, 5);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(5, 4);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase092() {
        /*
         🤠 .. .. ..
         .. ++ .. ..
         .. ++ ++ ..
         .. .. 🐅 ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(6, 5);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(5, 5);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase093() {
        /*
         🤠 .. .. ..
         .. ++ .. ..
         .. ++ ++ ..
         .. .. 🐅 ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(6, 5);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Set<Coordinates> exceptCoordinates = Set.of(
                huntsmenCoordinates,
                predatorCoordinates,
                new Coordinates(5, 4),
                new Coordinates(4, 4),
                new Coordinates(5, 5)
        );

        for (int row = 2; row < 7; row++) {
            for (int column = 2; column < 7; column++) {
                Coordinates treeCoordinates = new Coordinates(row, column);
                if (exceptCoordinates.contains(treeCoordinates)) {
                    continue;
                }
                Tree tree = new Tree(treeCoordinates);
                worldMap.addEntity(tree);
            }
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    // ==================================== LEFT-UP SQUARE ====================================
    @Test
    public void checkTestcase110() {
        /*
         🐅 ++ ++ 🤠
         .. .. .. ..
         .. .. .. ..
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(3, 0);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(3, 1);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase111() {
        /*
         🐅 ++ ++ 🤠
         .. .. .. ..
         .. .. .. ..
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(3, 0);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(3, 2);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase112() {
        /*
         🐅 ++ ++ 🤠
         .. .. .. ..
         .. .. .. ..
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(3, 0);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        for (int column = 0; column < 4; column++) {
            Coordinates rockCoordinates = new Coordinates(2, column);
            Rock rock = new Rock(rockCoordinates);
            worldMap.addEntity(rock);

            Coordinates treeCoordinates = new Coordinates(4, column);
            Tree tree = new Tree(treeCoordinates);
            worldMap.addEntity(tree);
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase120() {
        /*
         🐅 .. .. ..
         .. ++ .. ..
         .. .. ++ ..
         .. .. .. 🤠
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(0, 0);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(1, 1);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase121() {
        /*
         🐅 .. .. ..
         .. ++ .. ..
         .. .. ++ ..
         .. .. .. 🤠
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(0, 0);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(2, 2);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase122() {
        /*
         🐅 .. .. ..
         .. ++ .. ..
         .. .. ++ ..
         .. .. .. 🤠
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(0, 0);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        for (int row = 0; row < 5; row++) {
            for (int column = 0; column < 5; column++) {
                if (row == column) {
                    continue;
                }
                Coordinates rockCoordinates = new Coordinates(row, column);
                Rock rock = new Rock(rockCoordinates);
                worldMap.addEntity(rock);
            }
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase130() {
        /*
         🐅 .. .. ..
         ++ .. .. ..
         ++ .. .. ..
         🤠 .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(0, 3);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(1, 3);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase131() {
        /*
         🐅 .. .. ..
         ++ .. .. ..
         ++ .. .. ..
         🤠 .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(0, 3);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(2, 3);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase132() {
        /*
         🐅 .. .. ..
         ++ .. .. ..
         ++ .. .. ..
         🤠 .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(0, 3);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        for (int row = 0; row < 5; row++) {
            Coordinates rockCoordinates = new Coordinates(row, 2);
            Rock rock = new Rock(rockCoordinates);
            worldMap.addEntity(rock);

            Coordinates treeCoordinates = new Coordinates(row, 4);
            Tree tree = new Tree(treeCoordinates);
            worldMap.addEntity(tree);
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase140() {
        /*
         .. .. .. ..
         .. .. .. ..
         .. 🐅 ++ ..
         .. .. ++ 🤠
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(2, 1);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(2, 2);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase141() {
        /*
         .. .. .. ..
         .. .. .. ..
         .. 🐅 ++ ..
         .. .. ++ 🤠
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(2, 1);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(3, 2);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase142() {
        /*
         .. .. .. ..
         .. .. .. ..
         .. 🐅 ++ ..
         .. .. ++ 🤠
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(2, 1);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Set<Coordinates> exceptCoordinates = Set.of(
                huntsmenCoordinates,
                predatorCoordinates,
                new Coordinates(2, 2),
                new Coordinates(3, 2)
        );

        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                Coordinates rockCoordinates = new Coordinates(row, column);
                if (exceptCoordinates.contains(rockCoordinates)) {
                    continue;
                }
                Rock rock = new Rock(rockCoordinates);
                worldMap.addEntity(rock);
            }
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase150() {
        /*
         .. .. .. ..
         .. .. 🐅 ..
         .. .. ++ ++
         .. .. .. 🤠
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(1, 2);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(2, 2);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase151() {
        /*
         .. .. .. ..
         .. .. 🐅 ..
         .. .. ++ ++
         .. .. .. 🤠
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(1, 2);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(2, 3);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase152() {
        /*
         .. .. .. ..
         .. .. 🐅 ..
         .. .. ++ ++
         .. .. .. 🤠
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(1, 2);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Set<Coordinates> exceptCoordinates = Set.of(
                huntsmenCoordinates,
                predatorCoordinates,
                new Coordinates(2, 2),
                new Coordinates(2, 3)
        );

        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                Coordinates treeCoordinates = new Coordinates(row, column);
                if (exceptCoordinates.contains(treeCoordinates)) {
                    continue;
                }
                Tree tree = new Tree(treeCoordinates);
                worldMap.addEntity(tree);
            }
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase160() {
        /*
         .. .. .. ..
         .. .. .. ..
         🐅 ++ .. ..
         .. .. ++ 🤠
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(2, 0);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(2, 1);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase161() {
        /*
         .. .. .. ..
         .. .. .. ..
         🐅 ++ .. ..
         .. .. ++ 🤠
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(2, 0);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(3, 2);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase162() {
        /*
         .. .. .. ..
         .. .. .. ..
         🐅 ++ .. ..
         .. .. ++ 🤠
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(2, 0);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Set<Coordinates> exceptCoordinates = Set.of(
                huntsmenCoordinates,
                predatorCoordinates,
                new Coordinates(2, 1),
                new Coordinates(3, 2)
        );

        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                Coordinates treeCoordinates = new Coordinates(row, column);
                if (exceptCoordinates.contains(treeCoordinates)) {
                    continue;
                }
                Tree tree = new Tree(treeCoordinates);
                worldMap.addEntity(tree);
            }
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase170() {
        /*
         .. .. 🐅 ..
         .. .. ++ ..
         .. .. .. ++
         .. .. .. 🤠
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(0, 2);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(1, 2);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase171() {
        /*
         .. .. 🐅 ..
         .. .. ++ ..
         .. .. .. ++
         .. .. .. 🤠
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(0, 2);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(2, 3);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase172() {
        /*
         .. .. 🐅 ..
         .. .. ++ ..
         .. .. .. ++
         .. .. .. 🤠
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(0, 2);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Set<Coordinates> exceptCoordinates = Set.of(
                huntsmenCoordinates,
                predatorCoordinates,
                new Coordinates(1, 2),
                new Coordinates(2, 3)
        );

        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                Coordinates treeCoordinates = new Coordinates(row, column);
                if (exceptCoordinates.contains(treeCoordinates)) {
                    continue;
                }
                Tree tree = new Tree(treeCoordinates);
                worldMap.addEntity(tree);
            }
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase180() {
        /*
         .. .. .. ..
         🐅 ++ .. ..
         .. ++ ++ ..
         .. .. .. 🤠
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(1, 0);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(1, 1);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase181() {
        /*
         .. .. .. ..
         🐅 ++ .. ..
         .. ++ ++ ..
         .. .. .. 🤠
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(1, 0);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(2, 1);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase182() {
        /*
         .. .. .. ..
         🐅 ++ .. ..
         .. ++ ++ ..
         .. .. .. 🤠
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(1, 0);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(2, 2);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase183() {
        /*
         .. .. .. ..
         🐅 ++ .. ..
         .. ++ ++ ..
         .. .. .. 🤠
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(1, 0);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Set<Coordinates> exceptCoordinates = Set.of(
                huntsmenCoordinates,
                predatorCoordinates,
                new Coordinates(1, 1),
                new Coordinates(2, 1),
                new Coordinates(2, 2)
        );

        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                Coordinates treeCoordinates = new Coordinates(row, column);
                if (exceptCoordinates.contains(treeCoordinates)) {
                    continue;
                }
                Tree tree = new Tree(treeCoordinates);
                worldMap.addEntity(tree);
            }
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase190() {
        /*
         .. 🐅 .. ..
         .. ++ ++ ..
         .. .. ++ ..
         .. .. .. 🤠
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(0, 1);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(1, 1);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase191() {
        /*
         .. 🐅 .. ..
         .. ++ ++ ..
         .. .. ++ ..
         .. .. .. 🤠
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(0, 1);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(1, 2);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase192() {
        /*
         .. 🐅 .. ..
         .. ++ ++ ..
         .. .. ++ ..
         .. .. .. 🤠
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(0, 1);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(2, 2);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase193() {
        /*
         .. 🐅 .. ..
         .. ++ ++ ..
         .. .. ++ ..
         .. .. .. 🤠
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(0, 1);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Set<Coordinates> exceptCoordinates = Set.of(
                huntsmenCoordinates,
                predatorCoordinates,
                new Coordinates(1, 1),
                new Coordinates(1, 2),
                new Coordinates(2, 2)
        );

        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                Coordinates treeCoordinates = new Coordinates(row, column);
                if (exceptCoordinates.contains(treeCoordinates)) {
                    continue;
                }
                Tree tree = new Tree(treeCoordinates);
                worldMap.addEntity(tree);
            }
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    // ==================================== LEFT-DOWN SQUARE ====================================
    @Test
    public void checkTestcase210() {
        /*
         🐅 ++ ++ 🤠
         .. .. .. ..
         .. .. .. ..
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(3, 0);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(3, 1);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase211() {
        /*
         🐅 ++ ++ 🤠
         .. .. .. ..
         .. .. .. ..
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(3, 0);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(3, 2);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase212() {
        /*
         🐅 ++ ++ 🤠
         .. .. .. ..
         .. .. .. ..
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(3, 0);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        for (int column = 0; column < 7; column++) {
            Coordinates rockCoordinates = new Coordinates(2, column);
            Rock rock = new Rock(rockCoordinates);
            worldMap.addEntity(rock);

            Coordinates treeCoordinates = new Coordinates(4, column);
            Tree tree = new Tree(treeCoordinates);
            worldMap.addEntity(tree);
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase220() {
        /*
         .. .. .. 🤠
         .. .. ++ ..
         .. ++ .. ..
         🐅 .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(6, 0);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(5, 1);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase221() {
        /*
         .. .. .. 🤠
         .. .. ++ ..
         .. ++ .. ..
         🐅 .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(6, 0);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(4, 2);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase222() {
        /*
         .. .. .. 🤠
         .. .. ++ ..
         .. ++ .. ..
         🐅 .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(6, 0);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Set<Coordinates> exceptCoordinates = Set.of(
                huntsmenCoordinates,
                predatorCoordinates,
                new Coordinates(5, 1),
                new Coordinates(4, 2)
        );

        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                Coordinates treeCoordinates = new Coordinates(row, column);
                if (exceptCoordinates.contains(treeCoordinates)) {
                    continue;
                }
                Tree tree = new Tree(treeCoordinates);
                worldMap.addEntity(tree);
            }
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase230() {
        /*
         .. .. .. 🤠
         .. .. .. ++
         .. .. .. ++
         .. .. .. 🐅
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(6, 3);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(5, 3);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase231() {
        /*
         .. .. .. 🤠
         .. .. .. ++
         .. .. .. ++
         .. .. .. 🐅
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(6, 3);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(4, 3);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase232() {
        /*
         .. .. .. 🤠
         .. .. .. ++
         .. .. .. ++
         .. .. .. 🐅
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(6, 3);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        for (int row = 0; row < 7; row++) {
            Coordinates rockCoordinates = new Coordinates(row, 2);
            Rock rock = new Rock(rockCoordinates);
            worldMap.addEntity(rock);

            Coordinates treeCoordinates = new Coordinates(row, 4);
            Tree tree = new Tree(treeCoordinates);
            worldMap.addEntity(tree);
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase240() {
        /*
         .. .. ++ 🤠
         .. 🐅 ++ ..
         .. .. .. ..
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(4, 1);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(3, 2);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase241() {
        /*
         .. .. ++ 🤠
         .. 🐅 ++ ..
         .. .. .. ..
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(4, 1);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(4, 2);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase242() {
        /*
         .. .. ++ 🤠
         .. 🐅 ++ ..
         .. .. .. ..
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(4, 1);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Set<Coordinates> exceptCoordinates = Set.of(
                huntsmenCoordinates,
                predatorCoordinates,
                new Coordinates(3, 2),
                new Coordinates(4, 2)
        );

        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                Coordinates rockCoordinates = new Coordinates(row, column);
                if (exceptCoordinates.contains(rockCoordinates)) {
                    continue;
                }
                Rock rock = new Rock(rockCoordinates);
                worldMap.addEntity(rock);
            }
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase250() {
        /*
         .. .. .. 🤠
         .. .. ++ ++
         .. .. 🐅 ..
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(5, 2);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(4, 2);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase251() {
        /*
         .. .. .. 🤠
         .. .. ++ ++
         .. .. 🐅 ..
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(5, 2);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(4, 3);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase252() {
        /*
         .. .. .. 🤠
         .. .. ++ ++
         .. .. 🐅 ..
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(5, 2);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Set<Coordinates> exceptCoordinates = Set.of(
                huntsmenCoordinates,
                predatorCoordinates,
                new Coordinates(4, 2),
                new Coordinates(4, 3)
        );

        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                Coordinates treeCoordinates = new Coordinates(row, column);
                if (exceptCoordinates.contains(treeCoordinates)) {
                    continue;
                }
                Tree tree = new Tree(treeCoordinates);
                worldMap.addEntity(tree);
            }
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase260() {
        /*
         .. .. ++ 🤠
         🐅 ++ .. ..
         .. .. .. ..
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(4, 0);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(3, 2);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase261() {
        /*
         .. .. ++ 🤠
         🐅 ++ .. ..
         .. .. .. ..
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(4, 0);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(4, 1);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase262() {
        /*
         .. .. ++ 🤠
         🐅 ++ .. ..
         .. .. .. ..
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(4, 0);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Set<Coordinates> exceptCoordinates = Set.of(
                huntsmenCoordinates,
                predatorCoordinates,
                new Coordinates(4, 1),
                new Coordinates(3, 2)
        );

        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                Coordinates treeCoordinates = new Coordinates(row, column);
                if (exceptCoordinates.contains(treeCoordinates)) {
                    continue;
                }
                Tree tree = new Tree(treeCoordinates);
                worldMap.addEntity(tree);
            }
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase270() {
        /*
         .. .. .. 🤠
         .. .. .. ++
         .. .. ++ ..
         .. .. 🐅 ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(6, 2);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(5, 2);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase271() {
        /*
         .. .. .. 🤠
         .. .. .. ++
         .. .. ++ ..
         .. .. 🐅 ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(6, 2);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(4, 3);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase272() {
        /*
         .. .. .. 🤠
         .. .. .. ++
         .. .. ++ ..
         .. .. 🐅 ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(6, 2);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Set<Coordinates> exceptCoordinates = Set.of(
                huntsmenCoordinates,
                predatorCoordinates,
                new Coordinates(4, 3),
                new Coordinates(5, 2)
        );

        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                Coordinates treeCoordinates = new Coordinates(row, column);
                if (exceptCoordinates.contains(treeCoordinates)) {
                    continue;
                }
                Tree tree = new Tree(treeCoordinates);
                worldMap.addEntity(tree);
            }
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase280() {
        /*
         .. .. .. 🤠
         .. ++ ++ ..
         🐅 ++ .. ..
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(5, 0);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(4, 1);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase281() {
        /*
         .. .. .. 🤠
         .. ++ ++ ..
         🐅 ++ .. ..
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(5, 0);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(4, 2);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase282() {
        /*
         .. .. .. 🤠
         .. ++ ++ ..
         🐅 ++ .. ..
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(5, 0);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(5, 1);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase283() {
        /*
         .. .. .. 🤠
         .. ++ ++ ..
         🐅 ++ .. ..
         .. .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(5, 0);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Set<Coordinates> exceptCoordinates = Set.of(
                huntsmenCoordinates,
                predatorCoordinates,
                new Coordinates(4, 1),
                new Coordinates(4, 2),
                new Coordinates(5, 1)
        );

        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                Coordinates treeCoordinates = new Coordinates(row, column);
                if (exceptCoordinates.contains(treeCoordinates)) {
                    continue;
                }
                Tree tree = new Tree(treeCoordinates);
                worldMap.addEntity(tree);
            }
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase290() {
        /*
         .. .. .. 🤠
         .. .. ++ ..
         .. ++ ++ ..
         .. 🐅 .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(6, 1);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(4, 2);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase291() {
        /*
         .. .. .. 🤠
         .. .. ++ ..
         .. ++ ++ ..
         .. 🐅 .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(6, 1);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(5, 1);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase292() {
        /*
         .. .. .. 🤠
         .. .. ++ ..
         .. ++ ++ ..
         .. 🐅 .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(6, 1);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(5, 2);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase293() {
        /*
         .. .. .. 🤠
         .. .. ++ ..
         .. ++ ++ ..
         .. 🐅 .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(6, 1);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Set<Coordinates> exceptCoordinates = Set.of(
                huntsmenCoordinates,
                predatorCoordinates,
                new Coordinates(4, 2),
                new Coordinates(5, 1),
                new Coordinates(5, 2)
        );

        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                Coordinates treeCoordinates = new Coordinates(row, column);
                if (exceptCoordinates.contains(treeCoordinates)) {
                    continue;
                }
                Tree tree = new Tree(treeCoordinates);
                worldMap.addEntity(tree);
            }
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    // ==================================== RIGHT-UP SQUARE ====================================
    @Test
    public void checkTestcase310() {
        /*
         .. .. .. ..
         .. .. .. ..
         .. .. .. ..
         🤠 ++ ++ 🐅
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(3, 6);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(3, 4);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase311() {
        /*
         .. .. .. ..
         .. .. .. ..
         .. .. .. ..
         🤠 ++ ++ 🐅
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(3, 6);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(3, 5);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase312() {
        /*
         .. .. .. ..
         .. .. .. ..
         .. .. .. ..
         🤠 ++ ++ 🐅
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(3, 6);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        for (int column = 0; column < 7; column++) {
            Coordinates rockCoordinates = new Coordinates(2, column);
            Rock rock = new Rock(rockCoordinates);
            worldMap.addEntity(rock);

            Coordinates treeCoordinates = new Coordinates(4, column);
            Tree tree = new Tree(treeCoordinates);
            worldMap.addEntity(tree);
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase320() {
        /*
         .. .. .. 🐅
         .. .. ++ ..
         .. ++ .. ..
         🤠 .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(0, 6);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(1, 5);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase321() {
        /*
         .. .. .. 🐅
         .. .. ++ ..
         .. ++ .. ..
         🤠 .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(0, 6);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(2, 4);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase322() {
        /*
         .. .. .. 🐅
         .. .. ++ ..
         .. ++ .. ..
         🤠 .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(0, 6);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Set<Coordinates> exceptCoordinates = Set.of(
                huntsmenCoordinates,
                predatorCoordinates,
                new Coordinates(2, 4),
                new Coordinates(1, 5)
        );

        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                Coordinates treeCoordinates = new Coordinates(row, column);
                if (exceptCoordinates.contains(treeCoordinates)) {
                    continue;
                }
                Tree tree = new Tree(treeCoordinates);
                worldMap.addEntity(tree);
            }
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase330() {
        /*
         .. .. .. 🐅
         .. .. .. ++
         .. .. .. ++
         .. .. .. 🤠
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(0, 3);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(1, 3);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase331() {
        /*
         .. .. .. 🐅
         .. .. .. ++
         .. .. .. ++
         .. .. .. 🤠
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(0, 3);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(2, 3);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase332() {
        /*
         .. .. .. 🐅
         .. .. .. ++
         .. .. .. ++
         .. .. .. 🤠
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(0, 3);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        for (int row = 0; row < 7; row++) {
            Coordinates rockCoordinates = new Coordinates(row, 2);
            Rock rock = new Rock(rockCoordinates);
            worldMap.addEntity(rock);

            Coordinates treeCoordinates = new Coordinates(row, 4);
            Tree tree = new Tree(treeCoordinates);
            worldMap.addEntity(tree);
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase340() {
        /*
         .. .. .. ..
         .. .. .. ..
         .. ++ 🐅 ..
         🤠 ++ .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(2, 5);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(2, 4);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase341() {
        /*
         .. .. .. ..
         .. .. .. ..
         .. ++ 🐅 ..
         🤠 ++ .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(2, 5);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(3, 4);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase342() {
        /*
         .. .. .. ..
         .. .. .. ..
         .. ++ 🐅 ..
         🤠 ++ .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(2, 5);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Set<Coordinates> exceptCoordinates = Set.of(
                huntsmenCoordinates,
                predatorCoordinates,
                new Coordinates(2, 4),
                new Coordinates(3, 4)
        );

        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                Coordinates rockCoordinates = new Coordinates(row, column);
                if (exceptCoordinates.contains(rockCoordinates)) {
                    continue;
                }
                Rock rock = new Rock(rockCoordinates);
                worldMap.addEntity(rock);
            }
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase350() {
        /*
         .. .. .. ..
         .. 🐅 .. ..
         ++ ++ .. ..
         🤠 .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(1, 4);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(2, 3);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase351() {
        /*
         .. .. .. ..
         .. 🐅 .. ..
         ++ ++ .. ..
         🤠 .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(1, 4);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(2, 4);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase352() {
        /*
         .. .. .. ..
         .. 🐅 .. ..
         ++ ++ .. ..
         🤠 .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(1, 4);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Set<Coordinates> exceptCoordinates = Set.of(
                huntsmenCoordinates,
                predatorCoordinates,
                new Coordinates(2, 3),
                new Coordinates(2, 4)
        );

        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                Coordinates treeCoordinates = new Coordinates(row, column);
                if (exceptCoordinates.contains(treeCoordinates)) {
                    continue;
                }
                Tree tree = new Tree(treeCoordinates);
                worldMap.addEntity(tree);
            }
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase360() {
        /*
         .. .. .. ..
         .. .. .. ..
         .. .. ++ 🐅
         🤠 ++ .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(2, 6);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(2, 5);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase361() {
        /*
         .. .. .. ..
         .. .. .. ..
         .. .. ++ 🐅
         🤠 ++ .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(2, 6);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(3, 4);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase362() {
        /*
         .. .. .. ..
         .. .. .. ..
         .. .. ++ 🐅
         🤠 ++ .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(2, 6);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Set<Coordinates> exceptCoordinates = Set.of(
                huntsmenCoordinates,
                predatorCoordinates,
                new Coordinates(2, 5),
                new Coordinates(3, 4)
        );

        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                Coordinates treeCoordinates = new Coordinates(row, column);
                if (exceptCoordinates.contains(treeCoordinates)) {
                    continue;
                }
                Tree tree = new Tree(treeCoordinates);
                worldMap.addEntity(tree);
            }
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase370() {
        /*
         .. 🐅 .. ..
         .. ++ .. ..
         ++ .. .. ..
         🤠 .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(0, 4);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(1, 4);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase371() {
        /*
         .. 🐅 .. ..
         .. ++ .. ..
         ++ .. .. ..
         🤠 .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(0, 4);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(2, 3);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase372() {
        /*
         .. 🐅 .. ..
         .. ++ .. ..
         ++ .. .. ..
         🤠 .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(0, 4);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Set<Coordinates> exceptCoordinates = Set.of(
                huntsmenCoordinates,
                predatorCoordinates,
                new Coordinates(1, 4),
                new Coordinates(2, 3)
        );

        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                Coordinates treeCoordinates = new Coordinates(row, column);
                if (exceptCoordinates.contains(treeCoordinates)) {
                    continue;
                }
                Tree tree = new Tree(treeCoordinates);
                worldMap.addEntity(tree);
            }
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase380() {
        /*
         .. .. .. ..
         .. .. ++ 🐅
         .. ++ ++ ..
         🤠 .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(1, 6);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(1, 5);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase381() {
        /*
         .. .. .. ..
         .. .. ++ 🐅
         .. ++ ++ ..
         🤠 .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(1, 6);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(2, 4);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase382() {
        /*
         .. .. .. ..
         .. .. ++ 🐅
         .. ++ ++ ..
         🤠 .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(1, 6);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(2, 5);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase383() {
        /*
         .. .. .. ..
         .. .. ++ 🐅
         .. ++ ++ ..
         🤠 .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(1, 6);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Set<Coordinates> exceptCoordinates = Set.of(
                huntsmenCoordinates,
                predatorCoordinates,
                new Coordinates(1, 5),
                new Coordinates(2, 4),
                new Coordinates(2, 5)
        );

        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                Coordinates treeCoordinates = new Coordinates(row, column);
                if (exceptCoordinates.contains(treeCoordinates)) {
                    continue;
                }
                Tree tree = new Tree(treeCoordinates);
                worldMap.addEntity(tree);
            }
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase390() {
        /*
         .. .. 🐅 ..
         .. ++ ++ ..
         .. ++ .. ..
         🤠 .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(0, 5);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates rockCoordinates = new Coordinates(1, 4);
        Rock rock = new Rock(rockCoordinates);
        worldMap.addEntity(rock);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase391() {
        /*
         .. .. 🐅 ..
         .. ++ ++ ..
         .. ++ .. ..
         🤠 .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(0, 5);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(1, 5);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase392() {
        /*
         .. .. 🐅 ..
         .. ++ ++ ..
         .. ++ .. ..
         🤠 .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(0, 5);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Coordinates treeCoordinates = new Coordinates(2, 4);
        Tree tree = new Tree(treeCoordinates);
        worldMap.addEntity(tree);

        mapRender.renderMap();

        assertTrue(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }

    @Test
    public void checkTestcase393() {
        /*
         .. .. 🐅 ..
         .. ++ ++ ..
         .. ++ .. ..
         🤠 .. .. ..
         */
        Coordinates huntsmenCoordinates = new Coordinates(3, 3);
        Huntsmen huntsmen = new Huntsmen(huntsmenCoordinates);
        worldMap.addEntity(huntsmen);

        Coordinates predatorCoordinates = new Coordinates(0, 5);
        Predator predator = new Predator(predatorCoordinates);
        worldMap.addEntity(predator);

        Set<Coordinates> exceptCoordinates = Set.of(
                huntsmenCoordinates,
                predatorCoordinates,
                new Coordinates(1, 4),
                new Coordinates(1, 5),
                new Coordinates(2, 4)
        );

        for (int row = 0; row < 7; row++) {
            for (int column = 0; column < 7; column++) {
                Coordinates treeCoordinates = new Coordinates(row, column);
                if (exceptCoordinates.contains(treeCoordinates)) {
                    continue;
                }
                Tree tree = new Tree(treeCoordinates);
                worldMap.addEntity(tree);
            }
        }

        mapRender.renderMap();

        assertFalse(huntsmenSight.hasBarrierBetween(huntsmenCoordinates, predatorCoordinates, worldMap));
    }
}
