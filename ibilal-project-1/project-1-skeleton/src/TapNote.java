import bagel.Input;
import bagel.Keys;
import bagel.Window;

public class TapNote extends Note {
    // where tap note starts
    public final static double TAP_START = 100;
    // tap notes contribute 1 note into track length
    public final static double TAP_LEN = 1;
    // data on tap hit
    private boolean hit = false;
    private String noteScore;
    private double hitFrame;

    public TapNote(String noteImage, Keys type, double x, double y, double frameNum) {
        super(noteImage, type, x, y, frameNum);
    }

    public TapNote(String noteImage, Keys type, double x, double frameNum) {
        super(noteImage, type, x, frameNum);
        super.setY(TAP_START);
    }

    @Override
    public void update(Input input, Lane currLane, int frameCount, Score gameScore, int playNote) {
        if (!hit && frameCount >= super.getFrameNum()) {
            super.getNoteImage().draw(super.getX(), super.getY());
            super.addY(NOTE_SPEED);
        }

        if (!hit && currLane.getCurrNote() == playNote &&
                input.wasPressed(super.getType())) {
            double dist = getDist(Lane.LANE_END, super.getY());
            if (dist <= scoreCriteria.PERFECT.dist) {
                noteScore = "PERFECT";
                getHit(gameScore, frameCount, scoreCriteria.PERFECT.points);
            } else if (dist <= scoreCriteria.GOOD.dist &&
                    dist > scoreCriteria.PERFECT.dist) {
                noteScore = "GOOD";
                getHit(gameScore, frameCount, scoreCriteria.GOOD.points);
            } else if (dist <= scoreCriteria.BAD.dist &&
                    dist > scoreCriteria.GOOD.dist) {
                noteScore = "BAD";
                getHit(gameScore, frameCount, scoreCriteria.BAD.points);
            } else if (dist <= scoreCriteria.MISS.dist &&
                    dist > scoreCriteria.BAD.dist) {
                noteScore = "MISS";
                getHit(gameScore, frameCount, scoreCriteria.MISS.points);
            }
        }

        if (!hit && super.getY() > Window.getHeight()) {
            noteScore = "MISS";
            getHit(gameScore, frameCount, scoreCriteria.MISS.points);
        }

        if (hit) {
            scoreMessage(frameCount);
            inputDelay(currLane, frameCount);
        }
    }

    @Override
    public void scoreMessage(double frameCount) {
        if (frameCount < hitFrame + super.MESSAGE_TIME) {
            super.MESSAGE_FONT.drawString(noteScore, Window.getWidth() / 2.0
                    - noteScore.length() * 16, Window.getHeight() / 2.0);
        }
    }

    @Override
    public void inputDelay(Lane currLane, double frameCount) {
        if (frameCount == hitFrame + INPUT_DELAY) {
            currLane.advanceCurrNote(TAP_LEN);
        }
    }

    public void getHit(Score gameScore, int frameCount, int points) {
        hitFrame = frameCount;
        gameScore.addScore(points, TAP_LEN);
        hit = true;
    }
}
