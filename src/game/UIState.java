package game;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;

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
        createText();
    }

    @Override
    public void update(float tpf) {
        super.update(tpf); //To change body of generated methods, choose Tools | Templates.
    }

    private void createCrosshair() {
        float y = app.getCamera().getHeight();
        float x = app.getCamera().getWidth();
        Picture crosshair = factory.getCrosshair();
        crosshair.setPosition(x / 2 - 90, y / 2 - 90);
        guiNode.attachChild(crosshair);
    }

    @Override
    public void cleanup() {
        super.cleanup(); //To change body of generated methods, choose Tools | Templates.
    }

    private void createText() {
        BitmapFont guiFont = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        BitmapText infoText = new BitmapText(guiFont,false);
        
        int screenHeight = app.getCamera().getHeight();
        infoText.setSize(30);
        infoText.setColor(ColorRGBA.Blue);
        infoText.setLocalTranslation(0, screenHeight, 0);
        infoText.setText("This is a test text!!!");
        guiNode.attachChild(infoText);    }
}
