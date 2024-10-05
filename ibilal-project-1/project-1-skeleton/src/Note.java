import bagel.*;

public abstract class Note {
    private final Image noteImage;
    private final Keys type;
    // how fast notes go down the screen
    protected final static double NOTE_SPEED = 4;
    private double x;
    private double y;
    // when note appears on screen
    private final double frameNum;
    // input delay values for close proximity notes (double for 60Hz)
    public final static double INPUT_DELAY = 20;
    public final static double NO_DELAY = 0;

    protected enum scoreCriteria {
        PERFECT (15, 10),
        GOOD (50, 5),
        BAD (100, -1),
        MISS (200, -5);

        final int dist;
        final int points;
        scoreCriteria(int dist, int points) {
            this.dist = dist;
            this.points = points;
        }
    }
    protected final Font MESSAGE_FONT = new Font("res/FSO8BITR.ttf", 40);
    protected final static int MESSAGE_TIME = 30;

    public Note(String noteImage, Keys type, double x, double y, double frameNum) {
        this.noteImage = new Image(noteImage);
        this.type = type;
        this.x = x;
        this.y = y;
        this.frameNum = frameNum;
    }

    public Note(String noteImage, Keys type, double x, double frameNum) {
        this.noteImage = new Image(noteImage);
        this.type = type;
        this.x = x;
        this.y = Lane.LANE_START;
        this.frameNum = frameNum;
    }

    public Keys getType() {
        return type;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void addY(double add) {
        y += add;
    }

    public Image getNoteImage() {
        return noteImage;
    }

    public double getFrameNum() {
        return frameNum;
    }

    // everything note does in an update
    public abstract void update(Input input, Lane currLane, int frameCount, Score gameScore, int playNote);

    // absolute value of distance between two points
    public double getDist(double p1, double p2) {
        return Math.abs(p2 - p1);
    }

    public abstract void scoreMessage(double frameCount);

    public abstract void inputDelay(Lane currLane, double frameCount);
}
