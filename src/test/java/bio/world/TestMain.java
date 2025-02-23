package bio.world;

import bio.world.factories.MenuFactory;
import bio.world.menu.MainMenu;
import bio.world.menu.MenuItems;
import bio.world.menu.StartMenu;

public class TestMain {
    public static void main(String[] args) {
        runMain();

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
    }

    private static void runMain() {
        StartMenu startMenu = MenuFactory.createStartMenu();
        startMenu.showTitle();
        MenuItems selectedStartMenuItem = startMenu.selectMenuItem();
        if (selectedStartMenuItem.equals(MenuItems.EXIT)) {
            return;
        }
        Simulation simulation = new Simulation();
        simulation.start();

        infinityLoop:
        while (true) {
            MainMenu mainMenu = MenuFactory.createMainMenu();
            mainMenu.showTitle();
            MenuItems selectedMainMenuItem = mainMenu.selectMenuItem();
            switch (selectedMainMenuItem) {
                case REPEAT -> {
                    System.out.println("— — — Здесь надо начать игру заново с теми же параметрами (но пока просто заново создаём симуляцию) — — —");
                    Simulation nextSimulation = new Simulation();
                    nextSimulation.start();
                }
                case CHANGE_INITIAL_PARAMETERS -> {
                    Simulation nextSimulation = new Simulation();
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