import java.util.HashSet;
import java.util.concurrent.Callable;

public class MonteCarloTreeSearch implements Callable<Move> {
    private static final int TIME_CONST = 150;
    private static final int WIN_SCR = 10;
    private int opponent;
    private int playerNumber;
    private StateTree tree;

    MonteCarloTreeSearch(HashSet<Move> availableMoves) {
        this.playerNumber = 2;
        this.tree = new StateTree();
        this.tree.setRoot(new State());
        this.tree.getRoot().setPlayerNumber(this.playerNumber);
        this.tree.getRoot().createChildStates(availableMoves);
    }

    State getRoot(){
        return this.tree.getRoot();
    }

    void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
        this.opponent = 3 - playerNumber;
    }

    void makeMove(Move move) {
        this.tree.setRoot(this.tree.getRoot().getChildrenStates().get(move.hashCode()));
        if (this.tree.getRoot().getChildrenStates() == null) this.tree.getRoot().createChildStates();
        this.tree.getRoot().setParentState(null);
    }

    @Override
    public Move call() {
        State root = tree.getRoot();
        if(root.getChildrenStates().size() > 300) return getDefaultMove();
        while (!Thread.interrupted() && (System.nanoTime() - InputHandler.startTime) / 1000000 < TIME_CONST) {
            State bestState = selectBestNode(root);
            if(Thread.interrupted()) return this.getDefaultMove();
            if (bestState.getChildrenStates() == null)
                bestState.createChildStates();
            if(Thread.interrupted()) return this.getDefaultMove();
            State nodeToExplore = bestState;
            if (!nodeToExplore.getChildrenStates().isEmpty()) {
                nodeToExplore = bestState.getSingleChildState();
            }
            if(Thread.interrupted()) return this.getDefaultMove();
            if (nodeToExplore.getChildrenStates() == null)
                nodeToExplore.createChildStates();
            if(Thread.interrupted()) return this.getDefaultMove();
            int playResult = simulatePlay(nodeToExplore);
            if(Thread.interrupted()) return this.getDefaultMove();
            if (playResult != 0)
                backPropagation(nodeToExplore, playResult);
        }

        State winnerNode = root.getStateWithMaxScore();
        return winnerNode.getMove();
    }

    Move getDefaultMove() {
        return this.tree.getRoot().getStateWithMaxScore().getMove();
    }

    private State selectBestNode(State rootState) {
        State state = rootState;
        while (state.getChildrenStates() != null && !state.getChildrenStates().isEmpty()) {
            state = UCT.findBestStateWithUTC(state);
        }
        return state;
    }

    private void backPropagation(State stateToExplore, int playerNumber) {
        State tempState = stateToExplore;

        while (tempState != null) {
            tempState.incrementVisitCounter();
            if (tempState.getPlayerNumber() == playerNumber) {
                tempState.addScore(WIN_SCR);
            }
            tempState = tempState.getParentState();
        }
    }

    private int simulatePlay(State state) {
        State tempState = new State(state);

        int currentPlayer = tempState.getPlayerNumber();
        if (currentPlayer == this.opponent && tempState.getChildrenStates().isEmpty()) {
            tempState.getParentState().setScore(Integer.MIN_VALUE);
            return currentPlayer;
        }
        while (!tempState.getChildrenStates().isEmpty()) {
            tempState.pickAMove();
            currentPlayer = tempState.getPlayerNumber();
        }
        return currentPlayer;
    }

}
