/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import game.Factory;
import game.GameState;

/**
 *
 * @author Thomas
 */
public class AsteroidControl extends AbstractControl {

    private Factory factory;
    private int worldSize;

    public AsteroidControl(Factory factory) {
        this.factory = factory;
        this.worldSize = 400;
    }

    @Override
    protected void controlUpdate(float tpf) {
        Vector3f location = new Vector3f(spatial.getWorldTranslation());
        if (location.getX() > worldSize || location.getX() < worldSize
                || location.getY() > worldSize || location.getY() < worldSize) {
            moveAsteroid(location);
        }
    }

    public int getAsteroidSize() {
        return spatial.getUserData("size");
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void moveAsteroid(Vector3f oldLocation) {
        Vector3f newLocation = new Vector3f(oldLocation);
        if (newLocation.getX() > worldSize) {
            newLocation.setX(-worldSize);
            spatial.getControl(RigidBodyControl.class).setPhysicsLocation(newLocation);
        }
        if (newLocation.getX() < -worldSize) {
            newLocation.setX(200);
            spatial.getControl(RigidBodyControl.class).setPhysicsLocation(newLocation);
        }
        if (newLocation.getY() > worldSize) {
            newLocation.setY(-worldSize);
            spatial.getControl(RigidBodyControl.class).setPhysicsLocation(newLocation);
        }
        if (newLocation.getY() < -worldSize) {
            newLocation.setY(worldSize);
            spatial.getControl(RigidBodyControl.class).setPhysicsLocation(newLocation);
        }
        if (newLocation.getZ() > worldSize) {
            newLocation.setZ(-worldSize);
            spatial.getControl(RigidBodyControl.class).setPhysicsLocation(newLocation);
        }
        if (newLocation.getZ() < -worldSize) {
            newLocation.setZ(worldSize);
            spatial.getControl(RigidBodyControl.class).setPhysicsLocation(newLocation);
        }
    }

    public int getHealth() {
        return spatial.getUserData("health");
    }

    public void setHealth(int health) {
        spatial.setUserData("health", health);
    }

    public int getSize() {
        return spatial.getUserData("size");
    }

    public void addHealth(int damage) {
        setHealth(getHealth() + damage);
        if (getHealth() <= 0) {
            if (getSize() == 1) {
                remove();
            } else {
                int newAsteroids = FastMath.nextRandomInt(2, 5);
                int newSize = getSize() / 2;
                for (int i = 0 ; i < newAsteroids; i++){
                    factory.createAsteroid(newSize, spatial.getWorldTranslation().mult(FastMath.nextRandomFloat()));
                }
            }
        }
    }

    public void remove() {
        spatial.removeFromParent();
        spatial.removeControl(spatial.getControl(RigidBodyControl.class));
        spatial.removeControl(this);
    }
}
