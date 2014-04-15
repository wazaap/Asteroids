/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;

/**
 *
 * @author Thomas
 */
public class DebrisControl extends AbstractControl{

    private float time;
    
    public DebrisControl(){
        time = 0;
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        time += tpf;
        if(time > 3){
            remove();
        }
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }
    
    public void remove(){
        spatial.removeFromParent();
        spatial.removeControl(this);
    }
}
