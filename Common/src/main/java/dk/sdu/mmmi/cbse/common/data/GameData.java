package dk.sdu.mmmi.cbse.common.data;

// Hold variables like screensize and keyboard state
public class GameData {

    private int displayWidth = 800;
    private int displayHeight = 800;

    // Creates the keyboard tracker when the game starts
    private final GameKeys keys = new GameKeys();

    public GameKeys getKeys() {
        return keys;
    }

    public void setDisplayWidth(int width) {
        this.displayWidth = width;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayHeight(int height) {
        this.displayHeight = height;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }
}
