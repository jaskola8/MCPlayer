import java.util.Observable;
import java.util.Observer;

public class Player implements Observer {

    private InputHandler inputHandler = new InputHandler(this);
    private MoveMaker moveMaker;
    private Thread inputThread;
    private boolean isGameRunning;
    private boolean justStarted = true;
    private boolean boardInitialized = false;


    Player() {
        this.isGameRunning = true;
    }

    void startGame() {
        this.inputThread = new Thread(inputHandler);
        this.inputThread.start();
    }

    @Override
    public void update(Observable observable, Object o) {
        String input = String.valueOf(o);
        if (input.isEmpty()) {
            boardInitialized = true;
            System.out.println("ok");
            this.moveMaker.initializeMCTS();
        } else if (input.charAt(0) == '{' && boardInitialized) {
            moveMaker.applyMove(new Move(input));
            moveMaker.makeMove();
        } else if ((input.charAt(0) == '{') && !boardInitialized) {

            String[] moves = input.split(",");
            for (String move : moves) {
                moveMaker.setField(move);
            }
            boardInitialized = true;
            System.out.println("ok");
            this.moveMaker.initializeMCTS();
        } else if (input.equals("stop")) {
            this.isGameRunning = false;
        } else if (justStarted) {
            this.moveMaker = new MoveMaker(Integer.valueOf(input));
            justStarted = false;
            System.out.println("ok");
        } else if (input.equals("start")) {
            this.moveMaker.iStarted();
            this.moveMaker.makeMove();
        }  else {
            System.out.println("Invalid inputHandler!");
            this.isGameRunning = false;
        }
        if (!this.isGameRunning) {
            this.inputThread.interrupt();
        }
    }
}
