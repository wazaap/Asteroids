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
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

/**
 *
 * @author Thomas
 */
public class GameState extends AbstractAppState {

    private SimpleApplication app;
    private Camera cam;
    private Node rootNode;
    private AssetManager assetManager;
    private Ray ray = new Ray();
    private static Box mesh = new Box(Vector3f.ZERO, 1, 1, 1);

    @Override
    public void update(float tpf) {
        CollisionResults results = new CollisionResults();
        ray.setOrigin(cam.getLocation());
        ray.setDirection(cam.getDirection());
        rootNode.collideWith(ray, results);
        if (results.size() > 0) {
        }
    }

    @Override
    public void cleanup() {
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        this.cam = this.app.getCamera();
        this.rootNode = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        makeAsteroids(30);
        addLight();

    }

    private void makeAsteroids(int number) {
        for (int i = 0; i < number; i++) {
            //Generating random location
            Vector3f loc = new Vector3f(
                    FastMath.nextRandomInt(-200, 200),
                    FastMath.nextRandomInt(-200, 200),
                    FastMath.nextRandomInt(-200, 200));
            Spatial asteroid = asteroid("Asteroid" + i, loc);
            
            //Setting random size of asteroid
            int size = FastMath.nextRandomInt(0, 7);
            asteroid.setLocalScale(size);
            asteroid.setUserData("size", size);
            
            //Generating random spin
            asteroid.setUserData("xspin", FastMath.nextRandomFloat() / 20 - FastMath.nextRandomFloat() / 10);
            asteroid.setUserData("yspin", FastMath.nextRandomFloat() / 20 - FastMath.nextRandomFloat() / 10);
            asteroid.setUserData("zspin", FastMath.nextRandomFloat() / 20 - FastMath.nextRandomFloat() / 10);
            
            //Stationary asteroids for testing
            asteroid.setUserData("direction", new Vector3f(0, 0, 0));
            
            //Generating random direction of asteroid
//            Vector3f direction = new Vector3f(
//                    FastMath.nextRandomFloat() / 2 - FastMath.nextRandomFloat(),
//                    FastMath.nextRandomFloat() / 2 - FastMath.nextRandomFloat(),
//                    FastMath.nextRandomFloat() / 2 - FastMath.nextRandomFloat());
//                        asteroid.setUserData("direction", direction);

            asteroid.addControl(new AsteroidControl());
            rootNode.attachChild(asteroid);
        }
    }

    public Spatial asteroid(String name, Vector3f loc) {
        Spatial asteroid = assetManager.loadModel("/Models/Asteroid/Asteroid.j3o");
        asteroid.setLocalTranslation(loc);
        return asteroid;
    }

    private void addLight() {
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)));
        sun.setColor(ColorRGBA.White.mult(0.5f));
        rootNode.addLight(sun);
    }
}
