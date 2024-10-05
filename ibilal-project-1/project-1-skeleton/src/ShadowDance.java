import bagel.*;
import java.io.FileReader;
import java.io.BufferedReader;

/**
 * Skeleton Code for SWEN20003 Project 1, Semester 2, 2023
 * Please enter your name below
 * @IbrahimBilal
 */
public class ShadowDance extends AbstractGame  {
    private final static int WINDOW_WIDTH = 1024;
    private final static int WINDOW_HEIGHT = 768;
    private final static String GAME_TITLE = "SHADOW DANCE";
    private final Image BACKGROUND_IMAGE = new Image("res/background.png");
    private final Font FONT = new Font("res/FSO8BITR.ttf", 64);
    private final Font SUBTITLE_FONT = new Font("res/FSO8BITR.ttf", 24);
    private final Font SCORE_FONT = new Font("res/FSO8BITR.ttf", 30);
    private final Track TRACK = new Track("res/track1.wav");

    // keep track of frames in a game
    private int frameCount = 0;
    private final Lane leftLane = new Lane("res/laneLeft.png");
    private final Lane rightLane = new Lane("res/laneRight.png");
    private final Lane upLane = new Lane("res/laneUp.png");
    private final Lane downLane = new Lane("res/laneDown.png");
    private final Score gameScore =
            new Score(readCSV(leftLane, rightLane, upLane, downLane));

    // different states of the game
    public enum gameStates {
        TITLE,
        START,
        PAUSE,
        WIN,
        LOSE
    }
    private gameStates gameState = gameStates.TITLE;


    public ShadowDance(){
        super(WINDOW_WIDTH, WINDOW_HEIGHT, GAME_TITLE);
    }


    /**
     * Method used to read file and create objects (you can change
     * this method as you wish).
     * reads level data and creates corresponding lane and note objects
     * with data to implement in game
     */
    private int readCSV(Lane leftLane, Lane rightLane, Lane upLane, Lane downLane) {
        final int ENTITY_TYPE = 0;
        final int ENTITY_DES = 1;
        final int X_OR_FRAME = 2;
        int trackLen = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("res/level1-60.csv"))) {
            String command;

            while ((command = br.readLine()) != null) {
                String[] noteStats = command.split(",");

                if (noteStats[ENTITY_TYPE].equals("Lane")) {
                    switch (noteStats[ENTITY_DES]) {
                        case "Left":
                            leftLane.setX(Double.parseDouble(noteStats[X_OR_FRAME]));
                            break;
                        case "Right":
                            rightLane.setX(Double.parseDouble(noteStats[X_OR_FRAME]));
                            break;
                        case "Up":
                            upLane.setX(Double.parseDouble(noteStats[X_OR_FRAME]));
                            break;
                        case "Down":
                            downLane.setX(Double.parseDouble(noteStats[X_OR_FRAME]));
                            break;
                    }
                } else {
                    trackLen++;
                    switch (noteStats[ENTITY_TYPE]) {
                        case "Left":
                            switch (noteStats[ENTITY_DES]) {
                                case "Normal":
                                    TapNote newTap = new TapNote("res/noteLeft.png", Keys.LEFT,
                                            leftLane.getX(), Double.parseDouble(noteStats[X_OR_FRAME]));
                                    leftLane.addNote(newTap);
                                    break;
                                case "Hold":
                                    HoldNote newHold = new HoldNote("res/holdNoteLeft.png", Keys.LEFT,
                                            leftLane.getX(), Double.parseDouble(noteStats[X_OR_FRAME]));
                                    leftLane.addNote(newHold);
                                    break;
                            }
                            break;
                        case "Right":
                            switch (noteStats[ENTITY_DES]) {
                                case "Normal":
                                    TapNote newTap = new TapNote("res/noteRight.png", Keys.RIGHT,
                                            rightLane.getX(), Double.parseDouble(noteStats[X_OR_FRAME]));
                                    rightLane.addNote(newTap);
                                    break;
                                case "Hold":
                                    HoldNote newHold = new HoldNote("res/holdNoteRight.png", Keys.RIGHT,
                                            rightLane.getX(), Double.parseDouble(noteStats[X_OR_FRAME]));
                                    rightLane.addNote(newHold);
                                    break;
                            }
                            break;
                        case "Up":
                            switch (noteStats[ENTITY_DES]) {
                                case "Normal":
                                    TapNote newTap = new TapNote("res/noteUp.png", Keys.UP,
                                            upLane.getX(), Double.parseDouble(noteStats[X_OR_FRAME]));
                                    upLane.addNote(newTap);
                                    break;
                                case "Hold":
                                    HoldNote newHold = new HoldNote("res/holdNoteUp.png", Keys.UP,
                                            upLane.getX(), Double.parseDouble(noteStats[X_OR_FRAME]));
                                    upLane.addNote(newHold);
                                    break;
                            }
                            break;
                        case "Down":
                            switch (noteStats[ENTITY_DES]) {
                                case "Normal":
                                    TapNote newTap = new TapNote("res/noteDown.png", Keys.DOWN,
                                            downLane.getX(), Double.parseDouble(noteStats[X_OR_FRAME]));
                                    downLane.addNote(newTap);
                                    break;
                                case "Hold":
                                    HoldNote newHold = new HoldNote("res/holdNoteDown.png", Keys.DOWN,
                                            downLane.getX(), Double.parseDouble(noteStats[X_OR_FRAME]));
                                    downLane.addNote(newHold);
                                    break;
                            }
                            break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return trackLen;
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowDance game = new ShadowDance();
        game.run();
    }

    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {

        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);

        switch (gameState) {
            case TITLE:
                FONT.drawString("SHADOW DANCE", 220, 250);
                SUBTITLE_FONT.drawString("PRESS SPACE TO START", 332, 440);
                SUBTITLE_FONT.drawString("USE ARROW KEYS TO PLAY", 320, 466);

                if (input.wasPressed(Keys.SPACE)) {
                    gameState = gameStates.START;
                    TRACK.start();
                }
                break;
            case START:
                SCORE_FONT.drawString(gameScore.toString(), 35, 35);

                leftLane.update(input, frameCount, gameScore);
                rightLane.update(input, frameCount, gameScore);
                upLane.update(input, frameCount, gameScore);
                downLane.update(input, frameCount, gameScore);
                // increment frame count while game is played
                frameCount++;

                if (input.wasPressed(Keys.SPACE)) {
                    gameState = gameStates.PAUSE;
                    TRACK.pause();
                }

                if (gameScore.isTrackFin() &&
                        gameScore.getScore() >= Score.TARGET_SCORE) {
                    gameState = gameStates.WIN;
                } else if (gameScore.isTrackFin() &&
                        gameScore.getScore() < Score.TARGET_SCORE) {
                    gameState = gameStates.LOSE;
                }
                break;
            case PAUSE:
                FONT.drawString("PAUSE", 386, WINDOW_HEIGHT / 2.0);
                if (input.wasPressed(Keys.SPACE)) {
                    gameState = gameStates.START;
                    TRACK.run();
                }
                break;
            case WIN:
                FONT.drawString("CLEAR!", 364, WINDOW_HEIGHT / 2.0);
                break;
            case LOSE:
                FONT.drawString("TRY AGAIN", 298, WINDOW_HEIGHT / 2.0);
                break;
        }
    }
}
