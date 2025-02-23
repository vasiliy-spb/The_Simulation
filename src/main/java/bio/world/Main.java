package bio.world;

import bio.world.factories.MenuFactory;
import bio.world.menu.MainMenu;
import bio.world.menu.MenuItems;
import bio.world.menu.StartMenu;

public class Main {
    public static void main(String[] args) {
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
