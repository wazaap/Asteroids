package game;

import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.util.SkyFactory;

/**
 * test
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    private static Box mesh = new Box(Vector3f.ZERO, 1, 1, 1);

    public static void main(String[] args) {
        AppSettings settings = new AppSettings(true);
        settings.setTitle("Asteroids 3D");
        Main app = new Main();
        app.setSettings(settings); // apply settings to app
        app.start(); // use settings and run
    }

    @Override
    public void simpleInitApp() {
        flyCam.setMoveSpeed(100f);
        rootNode.attachChild(SkyFactory.createSky(
                assetManager, "Textures/SkyBox/BackgroundCube.dds", false));
        AsteroidsState state = new AsteroidsState();
        stateManager.attach(state);
    }

    @Override
    public void simpleUpdate(float tpf) {
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
