/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import game.GameState;

/**
 *
 * @author Thomas
 */
public class MissileControl extends AbstractControl {

    private GameState game;
    private float time;

    public MissileControl(GameState game) {
        this.game = game;
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
        //do dameage
        game.createExplosion(spatial.getWorldTranslation());
        remove();
    }

    private void remove() {
        spatial.removeFromParent();
        spatial.removeControl(spatial.getControl(RigidBodyControl.class));
        spatial.removeControl(this);
    }
}
