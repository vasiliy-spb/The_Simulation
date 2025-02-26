package bio.world.menu;

public abstract class AbstractMenu implements Menu {
    protected final String title;
    protected final String selectMessage;
    protected final String errorMessage;
    protected final int minItemNumber;
    protected final int maxItemNumber;

    public AbstractMenu(String title, String selectMessage, String errorMessage, int minItemNumber, int maxItemNumber) {
        this.title = title;
        this.selectMessage = selectMessage;
        this.errorMessage = errorMessage;
        this.minItemNumber = minItemNumber;
        this.maxItemNumber = maxItemNumber;
    }

    @Override
    public void showTitle() {
        System.out.println(title);
    }

    @Override
    abstract public MenuItems selectMenuItem();
}
