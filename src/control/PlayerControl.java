package control;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.control.AbstractControl;
import game.GameState;

public class PlayerControl extends AbstractControl{

    private GameState state;

    public PlayerControl(GameState state) {
        this.state = state;
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }    
}
