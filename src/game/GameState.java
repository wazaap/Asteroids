package game;

import control.DebrisControl;
import control.PhysicsControl;
import control.MissileControl;
import control.AsteroidControl;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl.ControlDirection;
import com.jme3.scene.shape.Sphere;
import com.jme3.ui.Picture;
import control.PlayerControl;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;

public class GameState extends AbstractAppState {

    private static final String MAPPING_SHOOT_MISSILE = "shootmissile";
    private static final Trigger TRIGGER_SHOOT_MISSILE = new MouseButtonTrigger(MouseInput.BUTTON_LEFT);
    private static final String MAPPING_THRUST = "thrust";
    private static final Trigger TRIGGER_THRUST = new KeyTrigger(KeyInput.KEY_W);
    private static final String MAPPING_BREAK = "break";
    private static final Trigger TRIGGER_BREAK = new KeyTrigger(KeyInput.KEY_S);
    private static final String MAPPING_UP = "up";
    private static final Trigger TRIGGER_UP = new MouseAxisTrigger(MouseInput.AXIS_X, true);
    private SimpleApplication app;
    private Camera cam;
    private BulletAppState bulletAppState;
    private Node rootNode;
    private Node asteroidNode;
    private Node lightNode;
    private Node missileNode;
    private Node debrisNode;
    private Node playerNode;
    private CameraNode camNode;
    private AssetManager assetManager;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        //Initialize stuff
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        this.cam = this.app.getCamera();
        this.rootNode = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();


        //Create nodes and attatch them
        playerNode = new Node("playernode");
        asteroidNode = new Node("asteroidnode");
        lightNode = new Node("lightnode");
        missileNode = new Node("missilenode");
        debrisNode = new Node("debrisnode");
        camNode = new CameraNode("camNode", cam);

        rootNode.addLight(addLight());
        rootNode.attachChild(asteroidNode);
        rootNode.attachChild(lightNode);
        rootNode.attachChild(missileNode);
        rootNode.attachChild(debrisNode);
        rootNode.attachChild(playerNode);

        //Add statemanagers & Physics
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        bulletAppState.getPhysicsSpace().setGravity(new Vector3f(0, 0, 0));
        bulletAppState.getPhysicsSpace().addCollisionListener(new PhysicsControl());
        stateManager.attach(this);

        //Create elements
        makeAsteroids(30);
        createGui();
        createPlayer();

        app.getInputManager().addMapping(MAPPING_SHOOT_MISSILE, TRIGGER_SHOOT_MISSILE);
        app.getInputManager().addListener(actionListener, new String[]{MAPPING_SHOOT_MISSILE});
        app.getInputManager().addMapping(MAPPING_THRUST, TRIGGER_THRUST);
        app.getInputManager().addListener(analogListener, new String[]{MAPPING_THRUST});
        app.getInputManager().addMapping(MAPPING_BREAK, TRIGGER_BREAK);
        app.getInputManager().addListener(analogListener, new String[]{MAPPING_BREAK});
        app.getInputManager().addMapping(MAPPING_UP, TRIGGER_UP);
        app.getInputManager().addListener(analogListener, new String[]{MAPPING_UP});


    }

    @Override
    public void update(float tpf) {
    }

    @Override
    public void cleanup() {
    }

    private void createGui() {
        Picture pic = new Picture("HUD Picture");
        float y = app.getContext().getSettings().getHeight();
        float x = app.getContext().getSettings().getWidth();
        pic.setImage(assetManager, "Interface/crosshair1.png", true);
        pic.setWidth(180);
        pic.setHeight(180);
        pic.setPosition(x / 2 - 90, y / 2 - 90);
        app.getGuiNode().attachChild(pic);
    }

    private DirectionalLight addLight() {
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)));
        sun.setColor(ColorRGBA.White.mult(0.5f));
        return sun;
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

    public Node getAsteroids() {
        return asteroidNode;
    }

    //Create spatials
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

    private Spatial createMissile() {
        Sphere s = new Sphere(16, 16, 2f);
        Spatial missile = new Geometry("missile", s);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.Cyan);
        missile.setMaterial(mat);
        missile.setUserData("direction", cam.getDirection());
        missile.setLocalTranslation(cam.getDirection().mult(5));

        SphereCollisionShape sphereShape = new SphereCollisionShape(1.5f);
        RigidBodyControl physControl = new RigidBodyControl(sphereShape, 1.0f);
        physControl.applyImpulse(cam.getDirection().mult(400), cam.getDirection());

        missile.addControl(physControl);
        bulletAppState.getPhysicsSpace().add(missile);
        missile.addControl(new MissileControl(this));
        return missile;
    }

    public void createExplosion(Vector3f location) {
        ParticleEmitter debrisEffect = new ParticleEmitter("Debris", ParticleMesh.Type.Triangle, 10);
        Material debrisMat = new Material(assetManager, "Common/MatDefs/Misc/Particle.j3md");
        debrisMat.setTexture("Texture", assetManager.loadTexture("Textures/Effects/Debris.png"));
        debrisEffect.setMaterial(debrisMat);
        debrisEffect.setImagesX(3);
        debrisEffect.setImagesY(3); // 3x3 texture animation
        debrisEffect.setRotateSpeed(4);
        debrisEffect.setSelectRandomImage(true);
        debrisEffect.getParticleInfluencer().setInitialVelocity(new Vector3f(4, 4, 4));
        debrisEffect.setStartColor(new ColorRGBA(1f, 1f, 1f, 1f));
        debrisEffect.setGravity(0f, 0f, 0f);
        debrisEffect.getParticleInfluencer().setVelocityVariation(1.60f);
        debrisEffect.setLocalScale(2.0f);
        debrisEffect.setLocalTranslation(location);
        debrisEffect.addControl(new DebrisControl());
        debrisNode.attachChild(debrisEffect);
        debrisEffect.emitAllParticles();
    }
    //Eventlisteners
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean isPressed, float tpf) {
            if (name.equals(MAPPING_SHOOT_MISSILE) && isPressed) {
                missileNode.attachChild(createMissile());
            }

        }
    };
    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float keyPressed, float tpf) {
            if (name.equals(MAPPING_THRUST)) {
                camNode.getControl(RigidBodyControl.class).applyForce(cam.getDirection().mult(300), cam.getDirection());
            }
            if (name.equals(MAPPING_BREAK)) {
                camNode.getControl(RigidBodyControl.class).applyForce(cam.getDirection().mult(-300), cam.getDirection());
            }
            if (name.equals(MAPPING_UP)) {
                System.out.println("UP!");
            }

        }
    };

    public void makeAsteroid(int size, Vector3f loc) {
        //Generating random location
        Spatial asteroid = assetManager.loadModel("/Models/Asteroid/Asteroid.j3o");
        asteroid.setLocalTranslation(loc);
        asteroid.addControl(new AsteroidControl(this));
        asteroid.setLocalScale(size);
        asteroid.setUserData("size", size);
        asteroid.setUserData("health", size * 10);
        SphereCollisionShape sphereShape = new SphereCollisionShape(size * 3);
        RigidBodyControl physControl = new RigidBodyControl(sphereShape, size);
        //Generating random speed
        int speed = FastMath.nextRandomInt(125, 175);
        //Generating random direction of asteroid
        Vector3f direction = new Vector3f(
                FastMath.nextRandomFloat() / 2 - FastMath.nextRandomFloat(),
                FastMath.nextRandomFloat() / 2 - FastMath.nextRandomFloat(),
                FastMath.nextRandomFloat() / 2 - FastMath.nextRandomFloat());
        physControl.applyImpulse(direction.mult(speed), direction);
        asteroid.addControl(physControl);
        bulletAppState.getPhysicsSpace().add(asteroid);
        asteroidNode.attachChild(asteroid);
    }

    private void createPlayer() {
        PlayerControl pc = new PlayerControl(this);
        BoxCollisionShape cBox = new BoxCollisionShape(new Vector3f(2, 2, 2));
        RigidBodyControl physControl = new RigidBodyControl(cBox, 10);
        camNode.addControl(physControl);
        camNode.addControl(pc);
        camNode.setControlDir(ControlDirection.SpatialToCamera);
        playerNode.attachChild(camNode);
        bulletAppState.getPhysicsSpace().add(camNode);
    }

    public Vector3f getCameraLocation() {
        return cam.getLocation();
    }

    public void moveAsteroidEffect(Vector3f newLocation, Vector3f oldLocation) {
    }
}
