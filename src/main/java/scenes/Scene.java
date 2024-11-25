package scenes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import engine.ecs.Component;
import engine.ecs.GameObject;
import engine.ecs.serialization.GameManagerSerializer;
import engine.graphics.Camera;
import engine.graphics.renderer.Renderer;
import engine.ecs.serialization.ComponentSerializer;
import engine.ecs.serialization.GameObjectSerializer;
import game.GameManager;
import imgui.ImGui;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected Camera camera;
    private boolean isRunning = false;
    protected List<GameObject> gameObjects = new ArrayList<>();

    protected GameObject activeGameObject = null;
    protected boolean levelLoaded = false;

    public Scene() {

    }

    public void init() {

    }

    public void start() {
        for (GameObject go : gameObjects) {
            go.start();
            this.renderer.add(go);
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject go) {
        if (!isRunning) {
            gameObjects.add(go);
        } else {
            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
        }
    }

    public abstract void update(float dt);

    public Camera camera() {
        return this.camera;
    }

    public void sceneImgui() {
        if (activeGameObject != null) {
            ImGui.begin("Inspector");
            activeGameObject.imgui();
            ImGui.end();
        }

        imgui();
    }

    public void imgui() {

    }

    public void saveExit() {
        Gson gson = new GsonBuilder()
                            .setPrettyPrinting()
                            .registerTypeAdapter(Component.class, new ComponentSerializer())
                            .registerTypeAdapter(GameObject.class, new GameObjectSerializer())
                            .registerTypeAdapter(GameManager.class, new GameManagerSerializer())
                            .create();

        try {
            FileWriter writer = new FileWriter("level.txt");
            writer.write(gson.toJson(this.gameObjects));
            writer.close();

             writer = new FileWriter("data.txt");
             writer.write(gson.toJson(GameManager.get()));
             writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        Gson gson = new GsonBuilder()
                            .setPrettyPrinting()
                            .registerTypeAdapter(Component.class, new ComponentSerializer())
                            .registerTypeAdapter(GameObject.class, new GameObjectSerializer())
                            .registerTypeAdapter(GameManager.class, new GameManagerSerializer())
                            .create();
        String levelFile = "";

        try {
            levelFile = new String(Files.readAllBytes(Paths.get("level.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!levelFile.equals("")) {
            int maxGoId = -1;
            int maxCompId = -1;
            GameObject[] objs = gson.fromJson(levelFile, GameObject[].class);
            for (GameObject go : objs) {
                addGameObjectToScene(go);

                for (Component c : go.getAllComponents()) {
                    if (c.getUid() > maxCompId) {
                        maxCompId = c.getUid();
                    }
                }
                if (go.getUid() > maxGoId) {
                    maxGoId = go.getUid();
                }
            }

            // Update the ID_COUNTER values for GameObject and Component
            maxGoId++;
            maxCompId++;
            GameObject.init(maxGoId);
            Component.init(maxCompId);
        }


        String dataFile = "";
        try {
            dataFile = new String(Files.readAllBytes(Paths.get("data.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!dataFile.equals("")) {
            System.out.println(dataFile);

        } else {
            GameManager.get().init();
        }

        if (!levelFile.equals("") && !dataFile.equals("")) {
            this.levelLoaded = true;
        }
    }
}
