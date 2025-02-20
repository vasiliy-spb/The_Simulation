package bio.world.dialogs;

import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractDialog<T> implements Dialog<T> {
    private final String title;
    private final String error;
    private final Function<String, T> mapper;
    private final Predicate<T> validator;
    private final Scanner scanner = new Scanner(System.in);

    public AbstractDialog(String title, String error, Function<String, T> mapper, Predicate<T> validator) {
        this.title = title;
        this.error = error;
        this.mapper = mapper;
        this.validator = validator;
    }

    @Override
    public T input() {
        showMessage(title);
        while (true) {
            String data = scanner.nextLine();
            try {
                T result = mapper.apply(data);
                if (validator.test(result)) {
                    return result;
                }
            } catch (IllegalArgumentException ignore) {
            }
            showMessage(error);
        }
    }

    private void showMessage(String message) {
        System.out.println(message);
    }
}

