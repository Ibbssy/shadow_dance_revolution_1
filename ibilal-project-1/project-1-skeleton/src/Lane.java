import bagel.Image;
import bagel.Input;

/**
 * Class representing Lanes
 */
public class Lane {
    private final Image laneImage;
    // where notes start
    public final static double LANE_START = 384;
    // the note shape at the end of lane
    public final static double LANE_END = 657;
    private double x;
    private double y;
    private final static int MAX_TAP_NOTES = 100;
    private int tapNoteCount;
    private final static int MAX_HOLD_NOTES = 20;
    private int holdNoteCount;
    private final Note[] notes = new Note[MAX_TAP_NOTES + MAX_HOLD_NOTES];
    // current note the lane is on
    private double currNote = 0;

    public Lane(String laneImage, double x, double y) {
        this.laneImage = new Image(laneImage);
        this.x = x;
        this.y = y;
        this.tapNoteCount = 0;
        this.holdNoteCount = 0;
    }

    public Lane(String laneImage) {
        this.laneImage = new Image(laneImage);
        this.x = 0;
        this.y = LANE_START;
        this.tapNoteCount = 0;
        this.holdNoteCount = 0;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public int getCurrNote() {
        // return as index representation
        return (int) Math.floor(currNote);
    }

    // advances current note depending on note length
    public void advanceCurrNote(double advance) {
        // compares within index representation
        if ((int) Math.floor(currNote) <= tapNoteCount + holdNoteCount) {
            currNote += advance;
        } else {
            System.out.println("Error: lane has reached end");
        }
    }

    // adds note to lane, checking for type and limit
    public void addNote(Note note) {
        if (note instanceof TapNote && tapNoteCount < MAX_TAP_NOTES) {
            notes[tapNoteCount + holdNoteCount] = note;
            tapNoteCount++;
        } else if (note instanceof HoldNote && holdNoteCount < MAX_HOLD_NOTES) {
            notes[tapNoteCount + holdNoteCount] = note;
            holdNoteCount++;
        } else {
            System.out.println("Error: lane has already reached type-node limit");
        }
    }

    // everything lane does in an update
    public void update(Input input, int frameCount, Score gameScore) {
        laneImage.draw(x, y);

        for (int playNote = 0; playNote < tapNoteCount + holdNoteCount; playNote++) {
            notes[playNote].update(input, this, frameCount, gameScore, playNote);
        }
    }
}
