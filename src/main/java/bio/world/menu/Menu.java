package bio.world.menu;

public interface Menu {
    void showMessage(String message);
    void showTitle();
    MenuItems selectMenuItem();
}
