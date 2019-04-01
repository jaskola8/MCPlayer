import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Comparator;

class State {
    private State parentState;
    private double score;
    private Move move;
    private HashMap<Integer, State> childrenStates;
    private int playerNumber;
    private int visitCounter;

    State() {
    }

    State(State state) {
        this.score = state.getScore();
        this.parentState = state.getParentState();
        this.playerNumber = state.getPlayerNumber();
        this.visitCounter = state.getVisitCounter();
        this.move = state.getMove();
        this.childrenStates = new HashMap<>();
        this.childrenStates.putAll(state.getChildrenStates());
    }

    void pickAMove() {
        State nextState = this.childrenStates.values().iterator().next();
        this.setPlayerNumber(3 - this.playerNumber);
        this.move = nextState.getMove();
        this.childrenStates.remove(this.move.hashCode());
        for (int i : this.move.getOverlappingMoves()) this.childrenStates.remove(i);
    }

    void createChildStates() {
        this.childrenStates = new HashMap<>();
        for (State state : this.parentState.getChildrenStates().values()) {
            if (this.move != state.getMove() &&
                    !this.move.getOverlappingMoves().contains(state.getMove().hashCode())) {
                State newState = new State();
                newState.setPlayerNumber(3 - state.getPlayerNumber());
                newState.setMove(state.getMove());
                newState.setParentState(this);
                this.childrenStates.put(newState.move.hashCode(), newState);
            }
        }
    }

    void createChildStates(HashSet<Move> availableMoves) {
        this.childrenStates = new HashMap<>();
        for (Move move : availableMoves) {
            State newState = new State();
            newState.setParentState(this);
            newState.setPlayerNumber(3 - this.playerNumber);
            newState.setMove(move);
            newState.childrenStates = null;
            this.childrenStates.put(move.hashCode(), newState);
        }
    }

    State getStateWithMaxScore() {
        return Collections.max(
                this.getChildrenStates().values(),
                Comparator.comparing(State::getScore));
    }

    HashMap<Integer, State> getChildrenStates() {
        return this.childrenStates;
    }

    State getParentState() {
        return parentState;
    }

    void setParentState(State parentState) {
        this.parentState = parentState;
    }

    Move getMove() {
        return move;
    }

    private void setMove(Move move) {
        this.move = move;
    }

    int getPlayerNumber() {
        return playerNumber;
    }

    void setPlayerNumber(int n) {
        this.playerNumber = n;
    }

    int getVisitCounter() {
        return visitCounter;
    }

    void incrementVisitCounter() {
        this.visitCounter += 1;
    }

    double getScore() {
        return score;
    }

    void setScore(double score) {
        this.score = score;
    }

    void addScore(double winScore) {
        this.score += winScore;
    }

    State getSingleChildState() {
        return this.childrenStates.values().iterator().next();
    }

}
