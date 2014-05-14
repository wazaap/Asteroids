package game;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;

public class Main extends SimpleApplication {

    public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        settings.setTitle("Asteroids 3D");
        Main app = new Main();
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        flyCam.setEnabled(false);
        Factory factory = new Factory(assetManager);
        ControlFactory controlFactory = new ControlFactory();
        GameState state = new GameState(rootNode, factory, controlFactory);
        setDisplayFps(false);
        setDisplayStatView(false);
        stateManager.attach(state);
        stateManager.attach(new UIState(guiNode, factory));
    }

    @Override
    public void simpleUpdate(float tpf) {
    }

    @Override
    public void simpleRender(RenderManager rm) {
    }
}
