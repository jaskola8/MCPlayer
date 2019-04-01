import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;

public class InputHandler extends Observable implements Runnable {
    private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    static long startTime;

    InputHandler(Player player) {
        this.addObserver(player);
    }

    @Override
    public void run() {
        String msg;
        while (!Thread.interrupted()) {
            try {
                msg = input.readLine();
                startTime = System.nanoTime();
            } catch (IOException e) {
                msg = "stop";
            }
            setChanged();
            notifyObservers(msg);
        }
    }
}
