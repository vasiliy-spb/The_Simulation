package bio.world.dialogs;

import java.util.Set;
import java.util.function.Function;

public class CharacterDialog extends AbstractDialog<Character> {
    public CharacterDialog(String title, String error, Set<Character> keySet) {
        super(title, error, getMapper(), keySet::contains);
    }

    private static Function<String, Character> getMapper() {
        return new Function<String, Character>() {
            @Override
            public Character apply(String s) {
                if (s.isEmpty()) {
                    return '\0';
                }
                s = s.trim().toLowerCase();
                if (s.length() != 1) {
                    throw new IllegalArgumentException();
                }
                return s.charAt(0);
            }
        };
    }
}
