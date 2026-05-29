package dk.sdu.mmmi.cbse.main;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {

        launch(Main.class);
    }

    @Override
    public void start(Stage window) throws Exception {

        // Game constructor queries ServiceLocator to locate plugins inside "mods-mvn"
        Game game = new Game();

        // Starts the GUI and boots game plugins
        game.start(window);

        // Starts 60fps AnimationTimer game loop
        game.render();        

    }

}
