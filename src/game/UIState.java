/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;

/**
 *
 * @author Thomas
 */
public class UIState extends AbstractAppState {

    private Node guiNode;
    private Factory factory;
    private Application app;

    public UIState(Node guiNode, Factory factory) {
        this.guiNode = guiNode;
        this.factory = factory;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = app;
        createCrosshair();

    }

    @Override
    public void update(float tpf) {
        super.update(tpf); //To change body of generated methods, choose Tools | Templates.
    }

    private void createCrosshair() {
        float y = app.getContext().getSettings().getHeight();
        float x = app.getContext().getSettings().getWidth();
        Picture crosshair = factory.getCrosshair();
        crosshair.setPosition(x / 2 - 90, y / 2 - 90);
        guiNode.attachChild(crosshair);
    }

    @Override
    public void cleanup() {
        super.cleanup(); //To change body of generated methods, choose Tools | Templates.
    }
}