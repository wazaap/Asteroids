/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;

/**
 *
 * @author Thomas
 */
public class AsteroidControl extends AbstractControl {

    public AsteroidControl() {
    }

    @Override
    protected void controlUpdate(float tpf) {
        Vector3f location = new Vector3f(spatial.getWorldTranslation());
        if (location.getX() > 200) {
            location.setX(-200);
            spatial.getControl(RigidBodyControl.class).setPhysicsLocation(location);
        }
        if (location.getX() < -200) {
            location.setX(200);
                        spatial.getControl(RigidBodyControl.class).setPhysicsLocation(location);


        }
        if (location.getY() > 200) {
            location.setY(-200);
            spatial.getControl(RigidBodyControl.class).setPhysicsLocation(location);

        }
        if (location.getY() < -200) {
            location.setY(200);
            spatial.getControl(RigidBodyControl.class).setPhysicsLocation(location);

        }
        if (location.getZ() > 200) {
            location.setZ(-200);
            spatial.getControl(RigidBodyControl.class).setPhysicsLocation(location);

        }
        if (location.getZ() < -200) {
            location.setZ(200);
            spatial.getControl(RigidBodyControl.class).setPhysicsLocation(location);

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
}
