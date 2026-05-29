package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import java.io.InputStream;
import dk.sdu.mmmi.cbse.common.util.ServiceLocator;

class Game {

    private final GameData gameData = new GameData();
    private final World world = new World();
    private final Map<Entity, Polygon> polygons = new ConcurrentHashMap<>();
    private final Pane gameWindow = new Pane();
    private final Text gameOverText = new Text();

    // ServiceLocator pulls fields dynamically
   public Game(){
       // Left empty. Initialization happens in start()
   }

    public void start(Stage window) throws Exception {
        Text scoreText = new Text(10, 25, "Destroyed asteroids: 0");
        gameWindow.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        gameWindow.getChildren().add(scoreText);

        // Game over text appearance
        gameOverText.setX(gameData.getDisplayWidth() / 2.0 - 100);
        gameOverText.setY(gameData.getDisplayHeight() / 2.0);
        gameOverText.setFill(Color.DARKRED);
        gameOverText.setStroke(Color.BLACK);
        gameOverText.setStrokeWidth(2);


        // Loading score text font
        try {
            InputStream scoreFontStream = getClass().getResourceAsStream("/fonts/RetroGaming.ttf");
            if (scoreFontStream != null) {
                // Load scoreText font at size 16px
                Font scoreFont = Font.loadFont(scoreFontStream, 16);
                scoreText.setFont(scoreFont);
            } else {
                System.err.println("[FONT WARNING]: Could not find designated font for score. Defaulting to fallback.");
                scoreText.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            }
        } catch (Exception e) {
            System.err.println("[FONT ERROR]: Failed to load designated scoreText font.");
            e.printStackTrace();
        }

        // Loading game over font
        try {
            InputStream gameOverFontStream = getClass().getResourceAsStream("/fonts/OptimusPrinceps.ttf");
            if (gameOverFontStream != null) {
                // Load scoreText font at size 42px
                Font gameOverFont = Font.loadFont(gameOverFontStream, 42);
                gameOverText.setFont(gameOverFont);
            } else {
                System.err.println("[FONT WARNING]: Could not find designated font for game over text. Defaulting to fallback.");
                gameOverText.setStyle("-fx-font-size: 36px; -fx-font-weight: bold;");
            }
        } catch (Exception e) {
            System.err.println("[FONT ERROR]: Failed to load designated gameOverText font.");
            e.printStackTrace();
        }

        // Add the game over text to window canvas
        gameWindow.getChildren().add(gameOverText);

        Scene scene = new Scene(gameWindow);
        scene.setOnKeyPressed(event -> {
            // Changed LEFT arrow to 'A'
            if (event.getCode().equals(KeyCode.A)) {
                gameData.getKeys().setKey(GameKeys.LEFT, true);
            }
            // Changed RIGHT arrow to 'D'
            if (event.getCode().equals(KeyCode.D)) {
                gameData.getKeys().setKey(GameKeys.RIGHT, true);
            }
            // Changed UP arrow to 'W'
            if (event.getCode().equals(KeyCode.W)) {
                gameData.getKeys().setKey(GameKeys.UP, true);
            }
            // Additional BACK key to 'S'
            if (event.getCode().equals(KeyCode.S)) {
                gameData.getKeys().setKey(GameKeys.BACK, true);
            }
            // Kept SPACE for shooting
            if (event.getCode().equals(KeyCode.SPACE)) {
                gameData.getKeys().setKey(GameKeys.SPACE, true);
            }
        });
        scene.setOnKeyReleased(event -> {
            // For making the game know when you let go of keys
            if (event.getCode().equals(KeyCode.A)) {
                gameData.getKeys().setKey(GameKeys.LEFT, false);
            }
            if (event.getCode().equals(KeyCode.D)) {
                gameData.getKeys().setKey(GameKeys.RIGHT, false);
            }
            if (event.getCode().equals(KeyCode.W)) {
                gameData.getKeys().setKey(GameKeys.UP, false);
            }
            if (event.getCode().equals(KeyCode.S)) {
                gameData.getKeys().setKey(GameKeys.BACK, false);
            }
            if (event.getCode().equals(KeyCode.SPACE)) {
                gameData.getKeys().setKey(GameKeys.SPACE, false);
            }
        });

        // Register any loaded plugin components (like Player or Enemy)
        for (IGamePluginService iGamePlugin : getGamePluginServices()) {
            iGamePlugin.start(gameData, world);
        }
        for (Entity entity : world.getEntities()) {
            Polygon polygon = new Polygon(entity.getPolygonCoordinates());
            polygons.put(entity, polygon);
            gameWindow.getChildren().add(polygon);
        }
        window.setScene(scene);
        window.setTitle("ASTEROIDS");
        window.show();
    }

    public void render() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                draw();
                gameData.getKeys().update();
            }

        }.start();
    }

    private void update() {
        for (IEntityProcessingService entityProcessorService : getEntityProcessingServices()) {
            entityProcessorService.process(gameData, world);
        }
        for (IPostEntityProcessingService postEntityProcessorService : getPostEntityProcessingServices()) {
            postEntityProcessorService.process(gameData, world);
        }
    }

    private void draw() {
        for (Entity polygonEntity : polygons.keySet()) {
            if (!world.getEntities().contains(polygonEntity)) {

                // Catching player ship death
                if (polygonEntity.getClass().getSimpleName().equalsIgnoreCase("Player")){
                    System.out.println("\n [GAME NOTICE]: Player entity was removed from the world!");
                    gameOverText.setText("YOU DIED");
                }

                Polygon removedPolygon = polygons.get(polygonEntity);
                polygons.remove(polygonEntity);
                gameWindow.getChildren().remove(removedPolygon);
            }
        }

        for (Entity entity : world.getEntities()) {
            Polygon polygon = polygons.get(entity);

            if (polygon == null) {
                // Implementing try-catch due to disappearing player ship
                try {
                    polygon = new Polygon(entity.getPolygonCoordinates());
                    polygons.put(entity, polygon);
                    gameWindow.getChildren().add(polygon);
                } catch (Exception e) {
                        System.err.println("--- DIAGNOSTIC ERROR: Could not create Polygon for " + entity.getClass().getSimpleName() + " ---");
                        e.printStackTrace();
                        continue;
                    }
            }

            try {
                // Try-catch to apply an entity's color
                // Reading color string for entities and paints the polygon fill specified color
                polygon.setFill(javafx.scene.paint.Color.valueOf(entity.getColor()));
                // Adding border color to polygons
                polygon.setStroke(javafx.scene.paint.Color.DARKGREY);
            } catch (Exception e) {
                System.err.println("--- DIAGNOSTIC ERROR: Color failed for " + entity.getClass().getSimpleName() + " with value [" + entity.getColor() + "] ---");
                e.printStackTrace();
                // Fallback color to avoid crashing
                polygon.setFill(javafx.scene.paint.Color.BLACK);
            }

            // Reading color string for entities and paints the polygon fill specified color
            //polygon.setFill(javafx.scene.paint.Color.valueOf(entity.getColor()));

            // Adding border color to polygons
            //polygon.setStroke(javafx.scene.paint.Color.DARKGREY);

            polygon.setTranslateX(entity.getX());
            polygon.setTranslateY(entity.getY());
            polygon.setRotate(entity.getRotation());
        }
    }

    public List<IGamePluginService> getGamePluginServices() {
       // Automatically looks inside "mods-mvn" folder for game setups
       return ServiceLocator.INSTANCE.locateAll(IGamePluginService.class);
    }

    public List<IEntityProcessingService> getEntityProcessingServices() {
        // Automatically looks inside "mods-mvn" folder for movement/enemy movement
       return ServiceLocator.INSTANCE.locateAll(IEntityProcessingService.class);
    }

    public List<IPostEntityProcessingService> getPostEntityProcessingServices() {
        // Automatically looks inside "mods-mvn" folder for clean-up/collision
       return ServiceLocator.INSTANCE.locateAll(IPostEntityProcessingService.class);
    }

}
