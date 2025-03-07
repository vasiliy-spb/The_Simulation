package bio.world.menu;

import bio.world.dialogs.Dialog;
import bio.world.dialogs.IntegerMinMaxDialog;

public class MainMenu extends AbstractMenu {
    public MainMenu(String title, String selectMessage, String errorMessage, int minItemNumber, int maxItemNumber) {
        super(title, selectMessage, errorMessage, minItemNumber, maxItemNumber);
    }

    @Override
    public MenuItems selectMenuItem() {
        Dialog<Integer> integerDialog = new IntegerMinMaxDialog(this.selectMessage, this.errorMessage, this.minItemNumber, this.maxItemNumber);
        int selectedMenuItem = integerDialog.input();

        return switch (selectedMenuItem) {
            case 1 -> MenuItems.REPEAT;
            case 2 -> MenuItems.CHANGE_INITIAL_PARAMETERS;
            case 3 -> MenuItems.PLAY_RANDOM;
            case 4 -> MenuItems.EXIT;
            default -> throw new IllegalStateException("Unexpected value: " + selectedMenuItem);
        };
    }
}
