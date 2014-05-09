package game;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.SphereCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.CameraControl;
import com.jme3.scene.shape.Sphere;
import com.jme3.ui.Picture;
import com.jme3.util.SkyFactory;
import control.AsteroidControl;
import control.DebrisControl;
import control.MissileControl;
import control.PlayerControl;

public final class Factory {

    private AssetManager am;
    private BulletAppState bulletAppState;
    private AudioNode missileAudio;
    private AudioNode ambientAudio;
    private Material missileMaterial;
    private GameState state;
    
    public Factory(AssetManager am) {
        this.am = am;
        missileAudio = new AudioNode(am, "Sounds/Effects/blast_heavy.wav");
        ambientAudio = new AudioNode(am, "Sounds/Ambient/space_ambient.ogg", true);
        missileMaterial = new Material(am, "Common/MatDefs/Misc/Unshaded.j3md");
    }

    public void setState(GameState state) {
        this.state = state;
    }
    
    

    public DirectionalLight addSunLight() {
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)));
        sun.setColor(ColorRGBA.White.mult(0.5f));
        return sun;
    }
    
    public Spatial addBackground(){
        return SkyFactory.createSky(
                am, "Textures/SkyBox/BackgroundCube.dds", false);
    }

    public void ambientSounds(boolean start) {
        ambientAudio.setVolume(8);
        if (start) {
            ambientAudio.play();
        } else {
            ambientAudio.stop();
        }
    }

    public void setBulletAppState(BulletAppState bulletAppState) {
        this.bulletAppState = bulletAppState;
    }

    public Picture getCrosshair() {
        Picture crosshair = new Picture("HUD Picture");
        crosshair.setImage(am, "Interface/crosshair1.png", true);
        crosshair.setWidth(180);
        crosshair.setHeight(180);
        return crosshair;
    }

    public Spatial createAsteroid(int size, Vector3f loc) {
        Spatial asteroid = am.loadModel("/Models/Asteroid/Asteroid.j3o");
        asteroid.setLocalTranslation(loc);
        asteroid.addControl(new AsteroidControl(state));
        asteroid.setLocalScale(size);
        asteroid.setUserData("size", size);
        asteroid.setUserData("health", size * 10);
        SphereCollisionShape sphereShape = new SphereCollisionShape(size * 3);
        RigidBodyControl physControl = new RigidBodyControl(sphereShape, size);
        physControl.setDamping(0.3f, 0.3f);
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
        return asteroid;
    }

    public ParticleEmitter createExplosion(Vector3f location) {
        ParticleEmitter debrisEffect = new ParticleEmitter("Debris", ParticleMesh.Type.Triangle, 10);
        Material debrisMat = new Material(am, "Common/MatDefs/Misc/Particle.j3md");
        debrisMat.setTexture("Texture", am.loadTexture("Textures/Effects/Debris.png"));
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
        debrisEffect.emitAllParticles();
        return debrisEffect;
    }

    public Spatial createMissile(Vector3f location, Vector3f direction) {
        Sphere s = new Sphere(16, 16, 2f);
        Spatial missile = new Geometry("missile", s);
        missileMaterial.setColor("Color", ColorRGBA.Cyan);
        missile.setMaterial(missileMaterial);
        missile.setUserData("direction", direction);
        missile.setLocalTranslation(location.add(direction.mult(3)));
        SphereCollisionShape sphereShape = new SphereCollisionShape(1.5f);
        RigidBodyControl physControl = new RigidBodyControl(sphereShape, 1.0f);
        physControl.applyImpulse(direction.mult(400), direction);
        missile.addControl(physControl);
        missile.addControl(new MissileControl(state));
        missileAudio.playInstance();
        bulletAppState.getPhysicsSpace().add(missile);
        return missile;
    }

    public CameraNode createPlayer(CameraNode camNode) {
        PlayerControl pc = new PlayerControl(state);
        BoxCollisionShape cBox = new BoxCollisionShape(new Vector3f(2, 2, 2));
        RigidBodyControl physControl = new RigidBodyControl(cBox, 5);
        physControl.setDamping(0.5f, 0.5f);
        camNode.addControl(pc);
        camNode.addControl(physControl);
        camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
        bulletAppState.getPhysicsSpace().add(camNode);
        return camNode;
    }
}
