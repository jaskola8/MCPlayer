import java.util.Collections;
import java.util.Comparator;

class UCT {
    private static double uctValue(int totalVisit, double nodeWinScore, int nodeVisit) {
        if (nodeVisit == 0) {
            return Integer.MAX_VALUE;
        }
        return (nodeWinScore / (double) nodeVisit)
                + 1.41 + Math.sqrt(Math.log(totalVisit) / (double) nodeVisit);
    }

    static State findBestStateWithUTC(State state) {
        int parentVisit = state.getVisitCounter();
        return Collections.max(
                state.getChildrenStates().values(),
                Comparator.comparing(c -> uctValue(parentVisit,
                        c.getScore(), c.getVisitCounter())));
    }
}
