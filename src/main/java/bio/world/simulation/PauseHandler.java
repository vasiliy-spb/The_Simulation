package bio.world.simulation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PauseHandler {
    private boolean onPause;
    private final BufferedReader reader;

    public PauseHandler() {
        this.onPause = false;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public void checkPause() throws IOException {
        if (reader.ready()) {
            String input = reader.readLine();
            if (input != null) {
                togglePause();
            }
        }
    }

    public void togglePause() {
        onPause = !onPause;
    }

    public boolean isPauseOn() {
        return onPause;
    }
}
