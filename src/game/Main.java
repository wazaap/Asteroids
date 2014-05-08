package game;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;

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
        stateManager.attach(new UIState(guiNode, factory));
        stateManager.attach(state);
    }

    @Override
    public void simpleUpdate(float tpf) {
    }

    @Override
    public void simpleRender(RenderManager rm) {
    }
}
