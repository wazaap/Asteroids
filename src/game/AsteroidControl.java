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

    private Vector3f direction;
    private float xspin;
    private float yspin;
    private float zspin;

    public AsteroidControl(Vector3f direction, float xspin, float yspin, float zspin) {
        this.direction = direction;
        this.xspin = xspin;
        this.yspin = yspin;
        this.zspin = zspin;
    }





    @Override
    protected void controlUpdate(float tpf) {
        spatial.rotate(xspin, yspin, zspin);
        spatial.move(direction);
    }

    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException(
                "Not supported yet.");
    }
}
