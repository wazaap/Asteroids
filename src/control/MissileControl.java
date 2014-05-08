package control;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import game.Factory;

public class MissileControl extends AbstractControl {

    private Factory factory;
    private float time;

    public MissileControl(Factory factory) {
        this.factory = factory;
        time = 0;
    }

    @Override
    protected void controlUpdate(float tpf) {
        time += tpf;
        if (time > 2f) {
            remove();
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    public void setDirection(Vector3f direction) {
        spatial.setUserData("direction", direction);
    }

    public Vector3f getDirection() {
        return spatial.getUserData("direction");
    }

    public void hitAsteroid(AsteroidControl asteroid) {
        if (asteroid != null) {
            asteroid.addHealth(-30);
            factory.createExplosion(spatial.getWorldTranslation());
            remove();
        }
    }

    private void remove() {
        spatial.removeFromParent();
        spatial.removeControl(spatial.getControl(RigidBodyControl.class));
        spatial.removeControl(this);
    }
}
