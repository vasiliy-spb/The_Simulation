package bio.world;

import bio.world.factories.MenuFactory;
import bio.world.factories.ParamsFactory;
import bio.world.factories.WorldMapFactory;
import bio.world.map.WorldMap;
import bio.world.menu.MainMenu;
import bio.world.menu.MenuItems;
import bio.world.menu.StartMenu;
import bio.world.simulation.Simulation;
import bio.world.simulation.init.InitParams;
import bio.world.simulation.init.InitParamsHandler;

public class Main {
    public static void main(String[] args) {
        InitParamsHandler initParamsHandler = new InitParamsHandler();
        MenuItems selectedStartMenuItem = askStartMenuItem();

        switch (selectedStartMenuItem) {
            case PLAY_RANDOM -> generateRandomParams(initParamsHandler);
            case EXIT -> {
                finish();
                return;
            }
        }

        startSimulation(initParamsHandler);
        repeatSimulation(initParamsHandler);
        finish();
    }

    private static MenuItems askStartMenuItem() {
        StartMenu startMenu = MenuFactory.createStartMenu();
        startMenu.showTitle();
        return startMenu.selectMenuItem();
    }

    private static void finish() {
        System.out.println("Follow the white rabbit..");
    }

    private static void generateRandomParams(InitParamsHandler initParamsHandler) {
        ParamsFactory paramsFactory = new ParamsFactory();
        InitParams initParams = paramsFactory.generateRandomParams();
        initParamsHandler.saveInitParams(initParams);
        WorldMap worldMap = WorldMapFactory.createWorldMap(initParams);
        initParamsHandler.saveEntityPosition(worldMap);
    }

    private static void startSimulation(InitParamsHandler initParamsHandler) {
        Simulation nextSimulation = new Simulation(initParamsHandler);
        nextSimulation.start();
    }

    private static void repeatSimulation(InitParamsHandler initParamsHandler) {
        while (true) {
            MenuItems selectedMainMenuItem = askMainMenuItem();

            switch (selectedMainMenuItem) {
                case CHANGE_INITIAL_PARAMETERS -> initParamsHandler = new InitParamsHandler();
                case PLAY_RANDOM -> generateRandomParams(initParamsHandler);
                case EXIT -> {
                    return;
                }
            }

            startSimulation(initParamsHandler);
        }
    }

    private static MenuItems askMainMenuItem() {
        MainMenu mainMenu = MenuFactory.createMainMenu();
        mainMenu.showTitle();
        return mainMenu.selectMenuItem();
    }
}
