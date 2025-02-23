package bio.world.menu;

import bio.world.dialogs.CharacterDialog;
import bio.world.dialogs.Dialog;

import java.util.Set;

public class PauseMenu extends AbstractMenu {
    public PauseMenu(String title, String selectMessage, String errorMessage, int minItemNumber, int maxItemNumber) {
        super(title, selectMessage, errorMessage, minItemNumber, maxItemNumber);
    }

    @Override
    public MenuItems selectMenuItem() {
        Dialog<Character> characterDialog = new CharacterDialog(this.selectMessage, this.errorMessage, Set.of('1', '2', '\0'));
        Character ch = characterDialog.input();
        return switch (ch) {
            case '\0' -> MenuItems.CONTINUE;
            case '1' -> MenuItems.CHANGE_MOVES_DELAY;
            case '2' -> MenuItems.EXIT;
            default -> throw new IllegalStateException("Unexpected value: " + ch);
        };
    }
}
