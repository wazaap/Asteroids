/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResults;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.ui.Picture;

/**
 *
 * @author Thomas
 */
public class GameState extends AbstractAppState {

    private static final String MAPPING_SHOOT = "shoot";
    private static final Trigger TRIGGER_SHOOT = new MouseButtonTrigger(MouseInput.BUTTON_LEFT);
    private SimpleApplication app;
    private Camera cam;
    private Node rootNode;
    private Node asteroidNode;
    private Node lightNode;
    private AssetManager assetManager;
    private Ray ray = new Ray();

    @Override
    public void update(float tpf) {
    }
    private ActionListener analogListener = new ActionListener() {
        public void onAction(String name, boolean isPressed, float tpf) {
            if (name.equals(MAPPING_SHOOT) && isPressed) {
                CollisionResults results = new CollisionResults();
                ray.setOrigin(cam.getLocation());
                ray.setDirection(cam.getDirection());
                asteroidNode.collideWith(ray, results);

                if (results.size() > 0) {
                    System.out.println("You HIT!!");
                    AsteroidControl asteroid = getAsteroidControl(results.getClosestCollision().getGeometry());
                    asteroid.setXSpin(0);
                    asteroid.setYSpin(0);
                    asteroid.setZSpin(0);
                } else {
                    System.out.println("You missed!!");
                }
            }
        }
    };

    private AsteroidControl getAsteroidControl(Geometry g) {
        if (!g.getParent().getName().equalsIgnoreCase("asteroidnode")) {
            Node n = g.getParent();
            while (!n.getParent().getName().equalsIgnoreCase("asteroidnode")) {
                n = n.getParent();
            }
            return n.getControl(AsteroidControl.class);
        }
        return g.getControl(AsteroidControl.class);
    }

    @Override
    public void cleanup() {
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        asteroidNode = new Node("asteroidnode");
        lightNode = new Node("lightnode");
        this.app = (SimpleApplication) app;
        this.cam = this.app.getCamera();
        this.rootNode = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        makeAsteroids(30);
        createGui();
        rootNode.addLight(addLight());
        rootNode.attachChild(asteroidNode);
        rootNode.attachChild(lightNode);
        app.getInputManager().addMapping(MAPPING_SHOOT, TRIGGER_SHOOT);
        app.getInputManager().addListener(analogListener, new String[]{MAPPING_SHOOT});
    }

    private void createGui() {
        Picture pic = new Picture("HUD Picture");
        float y = app.getContext().getSettings().getHeight();
        float x = app.getContext().getSettings().getWidth();
        pic.setImage(assetManager, "Interface/crosshair.png", true);
        pic.setWidth(200);
        pic.setHeight(200);
        pic.setPosition(x / 2 - 100, y / 2 - 100);
        app.getGuiNode().attachChild(pic);
    }

    private void makeAsteroids(int number) {
        for (int i = 0; i < number; i++) {
            //Generating random location
            Vector3f loc = new Vector3f(
                    FastMath.nextRandomInt(-200, 200),
                    FastMath.nextRandomInt(-200, 200),
                    FastMath.nextRandomInt(-200, 200));
            Spatial asteroid = assetManager.loadModel("/Models/Asteroid/Asteroid.j3o");
            asteroid.setLocalTranslation(loc);
            asteroid.addControl(new AsteroidControl());
            //Setting random size of asteroid
            int size = FastMath.nextRandomInt(0, 10);
            asteroid.setLocalScale(size);
            asteroid.setUserData("size", size);
            //Generating random spin
            asteroid.setUserData("xspin", FastMath.nextRandomFloat() / 20 - FastMath.nextRandomFloat() / 10);
            asteroid.setUserData("yspin", FastMath.nextRandomFloat() / 20 - FastMath.nextRandomFloat() / 10);
            asteroid.setUserData("zspin", FastMath.nextRandomFloat() / 20 - FastMath.nextRandomFloat() / 10);
//            
            //Stationary asteroids for testing
//            asteroid.setUserData("direction", new Vector3f(0, 0, 0));

            //Generating random direction of asteroid
            Vector3f direction = new Vector3f(
                    FastMath.nextRandomFloat() / 2 - FastMath.nextRandomFloat(),
                    FastMath.nextRandomFloat() / 2 - FastMath.nextRandomFloat(),
                    FastMath.nextRandomFloat() / 2 - FastMath.nextRandomFloat());
            asteroid.setUserData("direction", direction);

            asteroidNode.attachChild(asteroid);
        }
    }

    private DirectionalLight addLight() {
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)));
        sun.setColor(ColorRGBA.White.mult(0.5f));
        return sun;
    }
}
