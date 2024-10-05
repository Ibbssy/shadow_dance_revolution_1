import bagel.Input;
import bagel.Keys;
import bagel.Window;

public class HoldNote extends Note {
    public final static double HOLD_START = 24;
    public final static double HOLD_BUFF = 82;
    // hold notes contribute 0.5 note into track length for two parts
    public final static double HOLD_LEN = 0.5;
    private boolean bottomHit = false;
    private String bottomNoteScore;
    private double bottomHitFrame;
    private boolean topHit = false;
    private String topNoteScore;
    private double topHitFrame;

    public HoldNote(String noteImage, Keys type, double x, double y, double frameNum) {
        super(noteImage, type, x, y, frameNum);
    }

    public HoldNote(String noteImage, Keys type, double x, double frameNum) {
        super(noteImage, type, x, frameNum);
        super.setY(HOLD_START);
    }

    @Override
    public void update(Input input, Lane currLane, int frameCount, Score gameScore, int playNote) {
        if (!topHit && frameCount >= super.getFrameNum()) {
            super.getNoteImage().draw(super.getX(), super.getY());
            super.addY(NOTE_SPEED);
        }

        if (!bottomHit && currLane.getCurrNote() == playNote &&
                input.wasPressed(super.getType())) {
            double dist = getDist(Lane.LANE_END, super.getY() + HOLD_BUFF);
            if (dist <= scoreCriteria.PERFECT.dist) {
                bottomNoteScore = "PERFECT";
                getBottomHit(gameScore, frameCount, scoreCriteria.PERFECT.points);
            } else if (dist <= scoreCriteria.GOOD.dist &&
                    dist > scoreCriteria.PERFECT.dist) {
                bottomNoteScore = "GOOD";
                getBottomHit(gameScore, frameCount, scoreCriteria.GOOD.points);
            } else if (dist <= scoreCriteria.BAD.dist &&
                    dist > scoreCriteria.GOOD.dist) {
                bottomNoteScore = "BAD";
                getBottomHit(gameScore, frameCount, scoreCriteria.BAD.points);
            } else if (dist <= scoreCriteria.MISS.dist &&
                    dist > scoreCriteria.BAD.dist) {
                bottomNoteScore = "MISS";
                getBottomHit(gameScore, frameCount, scoreCriteria.MISS.points);
            }
        }

        if (!bottomHit && super.getY() + HOLD_BUFF > Window.getHeight()) {
            bottomNoteScore = "MISS";
            getBottomHit(gameScore, frameCount, scoreCriteria.MISS.points);
        }

        if (bottomHit) {
            scoreMessage(frameCount);
            inputDelay(currLane, frameCount);
        }

        if (bottomHit && !topHit && currLane.getCurrNote() == playNote &&
                input.wasReleased(super.getType())) {
            double dist = getDist(Lane.LANE_END, super.getY() - HOLD_BUFF);
            if (dist <= scoreCriteria.PERFECT.dist) {
                topNoteScore = "PERFECT";
                getTopHit(gameScore, frameCount, scoreCriteria.PERFECT.points);
            } else if (dist <= scoreCriteria.GOOD.dist &&
                    dist > scoreCriteria.PERFECT.dist) {
                topNoteScore = "GOOD";
                getTopHit(gameScore, frameCount, scoreCriteria.GOOD.points);
            } else if (dist <= scoreCriteria.BAD.dist &&
                    dist > scoreCriteria.GOOD.dist) {
                topNoteScore = "BAD";
                getTopHit(gameScore, frameCount, scoreCriteria.BAD.points);
            } else if (dist <= scoreCriteria.MISS.dist &&
                    dist > scoreCriteria.BAD.dist) {
                topNoteScore = "MISS";
                getTopHit(gameScore, frameCount, scoreCriteria.MISS.points);
            }
        }

        if (!topHit && super.getY() - HOLD_BUFF > Window.getHeight()) {
            topNoteScore = "MISS";
            getTopHit(gameScore, frameCount, scoreCriteria.MISS.points);
        }

        if (topHit) {
            scoreMessage(frameCount);
            inputDelay(currLane, frameCount);
        }
    }

    @Override
    public void scoreMessage(double frameCount) {
        if (bottomHit && frameCount < bottomHitFrame + super.MESSAGE_TIME) {
            super.MESSAGE_FONT.drawString(bottomNoteScore, Window.getWidth() / 2.0
                    - bottomNoteScore.length() * 16, Window.getHeight() / 2.0);
        } else if (topHit && frameCount < topHitFrame + super.MESSAGE_TIME) {
            super.MESSAGE_FONT.drawString(topNoteScore, Window.getWidth() / 2.0
                    - topNoteScore.length() * 16, Window.getHeight() / 2.0);
        }
    }

    @Override
    public void inputDelay(Lane currLane, double frameCount) {
        if (bottomHit && frameCount == bottomHitFrame + NO_DELAY) {
            currLane.advanceCurrNote(HOLD_LEN);
        } else if (topHit && frameCount == topHitFrame + INPUT_DELAY) {
            currLane.advanceCurrNote(HOLD_LEN);
        }
    }

    public void getTopHit(Score gameScore, int frameCount, int points) {
        topHitFrame = frameCount;
        gameScore.addScore(points, HOLD_LEN);
        topHit = true;
    }

    public void getBottomHit(Score gameScore, int frameCount, int points) {
        bottomHitFrame = frameCount;
        gameScore.addScore(points, HOLD_LEN);
        bottomHit = true;
    }
}
