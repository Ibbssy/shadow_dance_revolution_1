/**
 * Class for keeping track of game score and track length
 */
public class Score {
    // minimum score needed to win
    final public static int TARGET_SCORE = 150;
    private int score = 0;
    private final double trackLen;
    private double trackPoint;

    public Score(int trackLen) {
        this.trackLen = trackLen;
        this.trackPoint = 0;
    }

    public int getScore() {
        return score;
    }

    // add to game score, and advances track by note length
    public void addScore(int add, double advance) {
        score += add;
        trackPoint+= advance;
    }

    public boolean isTrackFin() {
        return trackPoint == trackLen;
    }

    @Override
    public String toString() {
        return "SCORE " + score;
    }
}
