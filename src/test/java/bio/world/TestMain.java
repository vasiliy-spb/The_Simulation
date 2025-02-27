package bio.world;

import bio.world.factories.MenuFactory;
import bio.world.menu.MainMenu;
import bio.world.menu.MenuItems;
import bio.world.menu.StartMenu;
import bio.world.simulation.Simulation;
import bio.world.simulation.init.InitParamsHandler;

public class TestMain {
    public static void main(String[] args) {
//        runMain();

//        String templateFilePath = "src/test/java/bio/world/factories/worldMap_templates/template05.txt";
//        TestSimulation testSimulation = new TestSimulation(templateFilePath);
//        testSimulation.startWithoutSpeed();
//        testSimulation.start();

//        String templateFilePath = "src/test/java/bio/world/factories/worldMap_templates/template06.txt";
//        TestSimulation testSimulation = new TestSimulation(templateFilePath);
//        testSimulation.startWithGrassGrow();

//        int height = 25;
//        int width = 45;
//        int countEntities = height * width / 8;
//        TestSimulation testSimulation = new TestSimulation(height, width);
//        testSimulation.start(countEntities);

        String templateFilePath = "src/test/java/bio/world/factories_for_tests/worldMap_templates/template07.txt";
        TestSimulation testSimulation = new TestSimulation(templateFilePath);
        testSimulation.startWithMoveCountWithHuntsmen(20);
    }

    private static void runMain() {
        InitParamsHandler initParamsHandler = new InitParamsHandler();
        StartMenu startMenu = MenuFactory.createStartMenu();
        startMenu.showTitle();
        MenuItems selectedStartMenuItem = startMenu.selectMenuItem();
        if (selectedStartMenuItem.equals(MenuItems.EXIT)) {
            return;
        }
        Simulation simulation = new Simulation(initParamsHandler);
        simulation.start();

        infinityLoop:
        while (true) {
            MainMenu mainMenu = MenuFactory.createMainMenu();
            mainMenu.showTitle();
            MenuItems selectedMainMenuItem = mainMenu.selectMenuItem();
            switch (selectedMainMenuItem) {
                case REPEAT -> {
                    Simulation nextSimulation = new Simulation(initParamsHandler);
                    nextSimulation.start();
                }
                case CHANGE_INITIAL_PARAMETERS -> {
                    initParamsHandler = new InitParamsHandler();
                    Simulation nextSimulation = new Simulation(initParamsHandler);
                    nextSimulation.start();
                }
                case EXIT -> {
                    break infinityLoop;
                }
            }
        }
        System.out.println("Follow the white rabbit..");
    }
}