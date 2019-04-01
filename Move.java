import java.util.ArrayList;
import java.util.Arrays;

public class Move {

    private static int boardSize;
    private int[][] fields;

    Move(int x1, int y1, int x2, int y2) {
        this.fields = new int[][]{{x1, y1}, {x2, y2}};
    }

    Move(String move) {
        move = move.replace("{", "");
        move = move.replace("}", "");
        String[] moves = move.split(",");
        String[] move1 = moves[0].split(";");
        String[] move2 = moves[1].split(";");
        this.fields = new int[][]{{Integer.valueOf(move1[0]), Integer.valueOf(move1[1])},
                {Integer.valueOf(move2[0]), Integer.valueOf(move2[1])}};
    }

    static void setBoardSize(int boardSize) {
        Move.boardSize = boardSize;
    }

    @Override
    public int hashCode() {
        return (Arrays.hashCode(fields[0]) * Arrays.hashCode(fields[1])) * 31 + this.fields[0][0] + this.fields[1][0];
    }

    ArrayList<Integer> getOverlappingMoves() {
        if (this.isHorizontal()) return getOverlappingMovesForHorizontal();
        else return getOverlappingMovesForVertical();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Move)) return false;
        Move move = (Move) o;
        return Arrays.equals(move.fields[1], this.fields[0]) && Arrays.equals(move.fields[0], this.fields[1]) ||
                Arrays.equals(move.fields[1], this.fields[1]) && Arrays.equals(move.fields[0], this.fields[0]);
    }

    @Override
    public String toString() {
        return String.format("{%d;%d},{%d;%d}",
                fields[0][0], fields[0][1], fields[1][0], fields[1][1]);

    }

    private boolean isHorizontal() {
        return this.fields[0][1] == this.fields[1][1];
    }

    private ArrayList<Integer> getOverlappingMovesForHorizontal() {
        ArrayList<Integer> moves = new ArrayList<>();
        int[] f1 = this.fields[0];
        int[] f2 = this.fields[1];
        if ((f1[0] + 1) % boardSize != f2[0]) {
            int[] temp = f1;
            f1 = f2;
            f2 = temp;
        }
        int nextPos = f1[1] + 1;
        moves.add(new Move(f1[0], f1[1], f1[0], nextPos % boardSize).hashCode());
        nextPos = f1[1] - 1;
        if (nextPos < 0) nextPos = boardSize - 1;
        moves.add(new Move(f1[0], f1[1], f1[0], nextPos).hashCode());
        nextPos = f1[0] - 1;
        if (nextPos < 0) nextPos = boardSize - 1;
        moves.add(new Move(f1[0], f1[1], nextPos, f1[1]).hashCode());

        nextPos = f2[1] + 1;
        moves.add(new Move(f2[0], f2[1], f2[0], nextPos % boardSize).hashCode());
        nextPos = f2[1] - 1;
        if (nextPos < 0) nextPos = boardSize - 1;
        moves.add(new Move(f2[0], f2[1], f2[0], nextPos).hashCode());
        nextPos = f2[0] + 1;
        moves.add(new Move(f2[0], f2[1], nextPos % boardSize, f2[1]).hashCode());

        return moves;
    }

    private ArrayList<Integer> getOverlappingMovesForVertical() {
        ArrayList<Integer> moves = new ArrayList<>();
        int[] f1 = this.fields[0];
        int[] f2 = this.fields[1];
        if ((f2[1] + 1) % boardSize != f1[1]) {
            int[] temp = f1;
            f1 = f2;
            f2 = temp;
        }
        int nextPos = f1[1] + 1;
        moves.add(new Move(f1[0], f1[1], f1[0], nextPos % boardSize).hashCode());
        nextPos = f1[0] - 1;
        if (nextPos < 0) nextPos = boardSize - 1;
        moves.add(new Move(f1[0], f1[1], nextPos, f1[1]).hashCode());
        nextPos = f1[0] + 1;
        moves.add(new Move(f1[0], f1[1], nextPos % boardSize, f1[1]).hashCode());

        nextPos = f2[1] - 1;
        if (nextPos < 0) nextPos = boardSize - 1;
        moves.add(new Move(f2[0], f2[1], f2[0], nextPos % boardSize).hashCode());
        nextPos = f2[0] - 1;
        if (nextPos < 0) nextPos = boardSize - 1;
        moves.add(new Move(f2[0], f2[1], nextPos, f2[1]).hashCode());
        nextPos = f2[0] + 1;
        moves.add(new Move(f2[0], f2[1], nextPos % boardSize, f2[1]).hashCode());

        return moves;
    }

}
