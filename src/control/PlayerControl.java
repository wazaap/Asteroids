package control;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import game.Factory;

public class PlayerControl extends AbstractControl{

    private Factory factory;

    public PlayerControl(Factory factory) {
        this.factory = factory;
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }    
}
