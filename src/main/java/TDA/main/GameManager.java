package TDA.main;

import TDA.entities.player.controller.collisions.GameCollisionManager;
import TDA.main.configs.GameConfigs;
import TDA.main.controls.GameControls;
import TDA.main.world.World;
import TDA.scene.Scene;
import TDA.scene.prefabs.HomeScene;
import woareXengine.io.window.DisplayMode;
import woareXengine.io.window.Window;
import woareXengine.mainEngine.Engine;

public class GameManager {
    private static Engine engine;
    private static GameConfigs configs;

    public static GameControls gameControls;

    public static Scene currentScene;

    public static World world;

    public static void init(GameConfigs gameConfigs) {
        configs = gameConfigs;
        engine = Engine.init(gameConfigs);

        gameControls = new GameControls();

        currentScene = HomeScene.init();

        world = new World();
    }

    public static void update() {
        if (gameControls.windowControls.isEscapeKeyPressed()) {
            Engine.instance().requestClose();
        }
        if (gameControls.windowControls.isDisplayModeSwitchPressed()) {
            Window window = Engine.window();
            switch (window.getDisplayMode()) {
                case FULLSCREEN -> window.changeDisplayMode(DisplayMode.WINDOWED);
                case WINDOWED -> window.changeDisplayMode(DisplayMode.FULLSCREEN);
            }
        }
        if (gameControls.developerControls.isDevModeToggled()) {
            Engine.instance().debugging = !Engine.instance().debugging;
        }

        currentScene.update();
        GameCollisionManager.get().update();
        currentScene.render();

        engine.update();
    }


    public static boolean readyToClose() {
        return engine.isCloseRequested();
    }

    public static void cleanUp() {
        currentScene.cleanUp();
        engine.closeEngine();
    }
}