package game;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;

public class UIState extends AbstractAppState {

    private Node guiNode;
    private Factory factory;
    private Application app;
    private CameraNode camNode;
    private int speed;
    private BitmapFont guiFont;
    private BitmapText speedText;
    private BitmapText hitText;
    private BitmapText playerHealthText;
    private BitmapText playerHitText;

    public UIState(Node guiNode, Factory factory) {
        this.guiNode = guiNode;
        this.factory = factory;
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = app;

        guiFont = app.getAssetManager().loadFont("Interface/Fonts/Default.fnt");
        camNode = app.getStateManager().getState(GameState.class).getCameraNode();
        speedText = new BitmapText(guiFont, false);
        hitText = new BitmapText(guiFont, false);
        playerHealthText = new BitmapText(guiFont, false);
        playerHitText = new BitmapText(guiFont, false);

        createCrosshair();
        createText();
    }

    @Override
    public void update(float tpf) {
        super.update(tpf); //To change body of generated methods, choose Tools | Templates.
        Vector3f vspeed = camNode.getControl(RigidBodyControl.class).getLinearVelocity();
        speed = (int) Math.round(vspeed.length());
        speedText.setText("Speed: " + speed);
        hitText.setText("Asteroids destroyed: " + app.getStateManager().getState(GameState.class).getAsteroidsDestroyed());
        playerHealthText.setText("Your health is: " + app.getStateManager().getState(GameState.class).getPlayerHealth());
        if (app.getStateManager().getState(GameState.class).isPlayerHit()) {
            playerHitText.setText("You've been HIT!!");
        } else {
            playerHitText.setText("");
        }
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
        int screenHeight = app.getCamera().getHeight();
        int screenWidth = app.getCamera().getWidth();
        speedText.setSize(30);
        speedText.setColor(ColorRGBA.Blue);
        speedText.setLocalTranslation(0, screenHeight, 0);
        guiNode.attachChild(speedText);
        hitText.setSize(30);
        hitText.setColor(ColorRGBA.Green);
        hitText.setLocalTranslation(screenWidth - 335, screenHeight, 0);
        guiNode.attachChild(hitText);
        playerHealthText.setSize(30);
        playerHealthText.setColor(ColorRGBA.Green);
        playerHealthText.setLocalTranslation(screenWidth - 252, screenHeight - 30, 0);
        guiNode.attachChild(playerHealthText);
        playerHitText.setSize(30);
        playerHitText.setColor(ColorRGBA.Red);
        playerHitText.setLocalTranslation(screenWidth / 2 - 100, screenHeight / 2 - 100, 100);
        guiNode.attachChild(playerHitText);
    }
}
