/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;

/**
 *
 * @author Thomas
 */
public class ControlFactory {

    private CameraNode camNode;
    private GameState gameState;
    private InputManager inputManager;
    private static final String MAPPING_SHOOT_MISSILE = "shootmissile";
    private static final Trigger TRIGGER_SHOOT_MISSILE = new KeyTrigger(KeyInput.KEY_SPACE);
    private static final String MAPPING_THRUST = "thrust";
    private static final Trigger TRIGGER_THRUST = new KeyTrigger(KeyInput.KEY_Z);
    private static final String MAPPING_BREAK = "break";
    private static final Trigger TRIGGER_BREAK = new KeyTrigger(KeyInput.KEY_X);
    private static final String MAPPING_UP = "up";
    private static final Trigger TRIGGER_UP = new KeyTrigger(KeyInput.KEY_NUMPAD8);
    private static final String MAPPING_DOWN = "down";
    private static final Trigger TRIGGER_DOWN = new KeyTrigger(KeyInput.KEY_NUMPAD2);
    private static final String MAPPING_LEFT = "left";
    private static final Trigger TRIGGER_LEFT = new KeyTrigger(KeyInput.KEY_NUMPAD4);
    private static final String MAPPING_RIGHT = "right";
    private static final Trigger TRIGGER_RIGHT = new KeyTrigger(KeyInput.KEY_NUMPAD6);
    private static final String MAPPING_ROLL_LEFT = "roll_left";
    private static final Trigger TRIGGER_ROLL_LEFT = new KeyTrigger(KeyInput.KEY_NUMPAD7);
    private static final String MAPPING_ROLL_RIGHT = "roll_right";
    private static final Trigger TRIGGER_ROLL_RIGHT = new KeyTrigger(KeyInput.KEY_NUMPAD9);

    public ControlFactory() {
    }

    public void setCamNode(CameraNode camNode) {
        this.camNode = camNode;
    }
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
    public void setInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }
    public void activateControls() {
        inputManager.addMapping(MAPPING_SHOOT_MISSILE, TRIGGER_SHOOT_MISSILE);
        inputManager.addListener(actionListener, new String[]{MAPPING_SHOOT_MISSILE});
        inputManager.addMapping(MAPPING_THRUST, TRIGGER_THRUST);
        inputManager.addListener(analogListener, new String[]{MAPPING_THRUST});
        inputManager.addMapping(MAPPING_BREAK, TRIGGER_BREAK);
        inputManager.addListener(analogListener, new String[]{MAPPING_BREAK});
        inputManager.addMapping(MAPPING_UP, TRIGGER_UP);
        inputManager.addListener(analogListener, new String[]{MAPPING_UP});
        inputManager.addMapping(MAPPING_DOWN, TRIGGER_DOWN);
        inputManager.addListener(analogListener, new String[]{MAPPING_DOWN});
        inputManager.addMapping(MAPPING_LEFT, TRIGGER_LEFT);
        inputManager.addListener(analogListener, new String[]{MAPPING_LEFT});
        inputManager.addMapping(MAPPING_RIGHT, TRIGGER_RIGHT);
        inputManager.addListener(analogListener, new String[]{MAPPING_RIGHT});
        inputManager.addMapping(MAPPING_ROLL_LEFT, TRIGGER_ROLL_LEFT);
        inputManager.addListener(analogListener, new String[]{MAPPING_ROLL_LEFT});
        inputManager.addMapping(MAPPING_ROLL_RIGHT, TRIGGER_ROLL_RIGHT);
        inputManager.addListener(analogListener, new String[]{MAPPING_ROLL_RIGHT});
    }
    private ActionListener actionListener = new ActionListener() {
        public void onAction(String name, boolean isPressed, float tpf) {
            if (name.equals(MAPPING_SHOOT_MISSILE) && isPressed) {
                gameState.fireMissile();
            }
        }
    };
    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float keyPressed, float tpf) {
            Vector3f xRotation = camNode.getCamera().getRotation().getRotationColumn(0).normalize();
            Vector3f yRotation = camNode.getCamera().getRotation().getRotationColumn(1).normalize();
            Vector3f zRotation = camNode.getCamera().getRotation().getRotationColumn(2).normalize();
            //Forward and backwards
            if (name.equals(MAPPING_THRUST)) {
                camNode.getControl(RigidBodyControl.class).applyForce(camNode.getCamera().getDirection().mult(500), camNode.getCamera().getDirection());
            } else if (name.equals(MAPPING_BREAK)) {
                camNode.getControl(RigidBodyControl.class).applyForce(camNode.getCamera().getDirection().mult(-500), camNode.getCamera().getDirection());
            }
            //Up, down, left, right
            if (name.equals(MAPPING_UP)) {
                camNode.getControl(RigidBodyControl.class).setAngularVelocity(camNode.getControl(RigidBodyControl.class).getAngularVelocity().add(xRotation.mult(tpf)));
            } else if (name.equals(MAPPING_DOWN)) {
                camNode.getControl(RigidBodyControl.class).setAngularVelocity(camNode.getControl(RigidBodyControl.class).getAngularVelocity().add(xRotation.mult(-tpf)));
            } else if (name.equals(MAPPING_LEFT)) {
                camNode.getControl(RigidBodyControl.class).setAngularVelocity(camNode.getControl(RigidBodyControl.class).getAngularVelocity().add(yRotation.mult(tpf)));
            } else if (name.equals(MAPPING_RIGHT)) {
                camNode.getControl(RigidBodyControl.class).setAngularVelocity(camNode.getControl(RigidBodyControl.class).getAngularVelocity().add(yRotation.mult(-tpf)));
            }
            //Roll left and right
            if (name.equals(MAPPING_ROLL_LEFT)) {
                camNode.getControl(RigidBodyControl.class).setAngularVelocity(camNode.getControl(RigidBodyControl.class).getAngularVelocity().add(zRotation.mult(tpf)));
            } else if (name.equals(MAPPING_ROLL_RIGHT)) {
                camNode.getControl(RigidBodyControl.class).setAngularVelocity(camNode.getControl(RigidBodyControl.class).getAngularVelocity().add(zRotation.mult(-tpf)));
            }
        }
    };
}
