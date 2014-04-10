/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
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
        spatial.rotate(getXSpin(), getYSpin(), getZSpin());
        spatial.move(getDirection());
    }

    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException(
                "Not supported yet.");
    }

    public int getAsteroidSize() {
        return spatial.getUserData("size");
    }

    public float getXSpin() {
        return spatial.getUserData("xspin");
    }

    public float getYSpin() {
        return spatial.getUserData("yspin");
    }

    public float getZSpin() {
        return spatial.getUserData("zspin");
    }
    
    public Vector3f getDirection(){
        return spatial.getUserData("direction");
    }
}
