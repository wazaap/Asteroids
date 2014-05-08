package game;

import control.PhysicsControl;
import control.AsteroidControl;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class GameState extends AbstractAppState {

    private SimpleApplication app;
    private BulletAppState bulletAppState;
    private Node rootNode;
    private Node asteroidNode;
    private Node lightNode;
    private Node missileNode;
    private Node debrisNode;
    private Node playerNode;
    private CameraNode camNode;
    private Factory factory;
    private ControlFactory controlFactory;

    public GameState(Node rootNode, Factory assetManager, ControlFactory controlFactory) {
        this.rootNode = rootNode;
        this.controlFactory = controlFactory;
        this.factory = assetManager;
        //Physics
        bulletAppState = new BulletAppState();
        //Nodes
        playerNode = new Node("playernode");
        asteroidNode = new Node("asteroidnode");
        lightNode = new Node("lightnode");
        missileNode = new Node("missilenode");
        debrisNode = new Node("debrisnode");

        
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        //Initialize stuff
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        camNode = new CameraNode("camNode", this.app.getCamera());

        //Setup ControlFactory
        controlFactory.setCamNode(camNode);
        controlFactory.setGameState(this);
        controlFactory.setInputManager(app.getInputManager());
        controlFactory.activateControls();

        //Setup factory
        factory.setBulletAppState(bulletAppState);
        factory.ambientSounds(true);

        //Setup Nodes
        rootNode.addLight(factory.addSunLight());
        rootNode.attachChild(asteroidNode);
        rootNode.attachChild(lightNode);
        rootNode.attachChild(missileNode);
        rootNode.attachChild(debrisNode);
        rootNode.attachChild(playerNode);
        rootNode.attachChild(factory.addBackground());
        playerNode.attachChild(factory.createPlayer(camNode));

        //Add statemanagers & Physics
        stateManager.attach(bulletAppState);
        bulletAppState.getPhysicsSpace().setGravity(new Vector3f(0, 0, 0));
        bulletAppState.getPhysicsSpace().addCollisionListener(new PhysicsControl());
        stateManager.attach(this);

        //Create elements
        makeAsteroids(30);
    }

    @Override
    public void update(float tpf) {
    }

    @Override
    public void cleanup() {
    }

    //Getters and setters
    public AsteroidControl getAsteroidControl(Geometry g) {
        if (!g.getParent().getName().equalsIgnoreCase("asteroidnode")) {
            Node n = g.getParent();
            while (!n.getParent().getName().equalsIgnoreCase("asteroidnode")) {
                n = n.getParent();
            }
            return n.getControl(AsteroidControl.class);
        }
        return g.getControl(AsteroidControl.class);
    }

    //Create several asteroids
    private void makeAsteroids(int number) {
        for (int i = 0; i < number; i++) {
            //Setting random size of asteroid
            int size = FastMath.nextRandomInt(0, 10);
            Vector3f loc = new Vector3f(
                    FastMath.nextRandomInt(-200, 200),
                    FastMath.nextRandomInt(-200, 200),
                    FastMath.nextRandomInt(-200, 200));
            makeAsteroid(size, loc);
        }
    }

    public void makeAsteroid(int size, Vector3f loc) {
        asteroidNode.attachChild(factory.createAsteroid(size, loc));
    }

    public BulletAppState getBulletAppState() {
        return bulletAppState;
    }

    public void addSpatialToPhys(Spatial s) {
        bulletAppState.getPhysicsSpace().add(s);
    }

    public void createExplosion(Vector3f location) {
        debrisNode.attachChild(factory.createExplosion(location));
    }

    public CameraNode getCameraNode() {
        return camNode;
    }

    public void fireMissile() {
        missileNode.attachChild(factory.createMissile(camNode.getCamera().getLocation(), camNode.getCamera().getDirection()));
    }
}
