import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.*;

class MoveMaker {
    private int boardSize;
    private HashSet<Move> moveSet;
    private Move bestMove;
    private int[][] board;
    private MonteCarloTreeSearch MCTS;
    private ExecutorService executorService;
    private FutureTask<Move> newMoveTask;

    MoveMaker(int boardSize) {
        this.board = new int[boardSize][boardSize];
        for (int[] row : this.board) Arrays.fill(row, 0);
        this.boardSize = boardSize;
        Move.setBoardSize(boardSize);
        this.executorService = Executors.newSingleThreadExecutor();
    }

    void iStarted() {
        this.MCTS.setPlayerNumber(1);
    }

    void initializeMCTS() {
        this.moveSet = getMoveSet(this.boardSize);
        this.MCTS = new MonteCarloTreeSearch(moveSet);
        this.newMoveTask = new FutureTask<>(MCTS);
    }

    void makeMove() {
        if(this.MCTS.getRoot().getChildrenStates().size() > 300){
            this.bestMove = MCTS.getDefaultMove();
        } else {
            this.newMoveTask = new FutureTask<>(MCTS);
            this.executorService.execute(this.newMoveTask);
            this.bestMove = getMove();
        }
        System.out.println(this.bestMove.toString());
        applyMove(this.bestMove);
    }

    void applyMove(Move move) {
        this.MCTS.makeMove(move);

    }

    private Move getMove() {
        Move move;
        try {
            move = newMoveTask.get(200, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | TimeoutException e) {
            move = MCTS.getDefaultMove();
        } catch (ExecutionException e) {
            e.printStackTrace();
            move = MCTS.getDefaultMove();
        }
        return move;
    }

    void setField(String field) {
        field = field.replace("{", "").replace("}", "");
        String[] indexes = field.split(";");
        this.board[Integer.valueOf(indexes[0])][Integer.valueOf(indexes[1])] = 1;
    }


    private HashSet<Move> getMoveSet(int boardSize) {
        HashSet<Move> moveSet = new HashSet<>();
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (this.board[i % boardSize][j % boardSize] == 0 &&
                        this.board[(i + 1) % boardSize][j % boardSize] == 0) {
                    Move move1 = new Move(i % boardSize, j % boardSize,
                            (i + 1) % boardSize, j % boardSize);
                    moveSet.add(move1);
                }
                if (this.board[i % boardSize][j % boardSize] == 0 &&
                        this.board[i % boardSize][(j + 1) % boardSize] == 0) {
                    Move move2 = new Move(i % boardSize, j % boardSize,
                            i % boardSize, (j + 1) % boardSize);
                    moveSet.add(move2);
                }
            }
        }
        return moveSet;
    }

}
