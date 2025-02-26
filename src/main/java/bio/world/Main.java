package bio.world;

import bio.world.factories.MenuFactory;
import bio.world.menu.MainMenu;
import bio.world.menu.MenuItems;
import bio.world.menu.StartMenu;
import bio.world.simulation.Simulation;
import bio.world.simulation.init.InitParamsHandler;

public class Main {
    public static void main(String[] args) {
        InitParamsHandler initParamsHandler = new InitParamsHandler();
        MenuItems selectedStartMenuItem = askStartMenuItems();

        if (selectedStartMenuItem.equals(MenuItems.EXIT)) {
            return;
        }

        startSimulation(initParamsHandler);
        repeatSimulation(initParamsHandler);
        finish();
    }

    private static MenuItems askStartMenuItems() {
        StartMenu startMenu = MenuFactory.createStartMenu();
        startMenu.showTitle();
        return startMenu.selectMenuItem();
    }

    private static void startSimulation(InitParamsHandler initParamsHandler) {
        Simulation nextSimulation = new Simulation(initParamsHandler);
        nextSimulation.start();
    }

    private static void repeatSimulation(InitParamsHandler initParamsHandler) {
        while (true) {
            MenuItems selectedMainMenuItem = askMainMenuItems();

            if (selectedMainMenuItem.equals(MenuItems.EXIT)) {
                break;
            }

            if (selectedMainMenuItem.equals(MenuItems.CHANGE_INITIAL_PARAMETERS)) {
                initParamsHandler = new InitParamsHandler();
            }

            startSimulation(initParamsHandler);
        }
    }

    private static MenuItems askMainMenuItems() {
        MainMenu mainMenu = MenuFactory.createMainMenu();
        mainMenu.showTitle();
        return mainMenu.selectMenuItem();
    }

    private static void finish() {
        System.out.println("Follow the white rabbit..");
    }
}
