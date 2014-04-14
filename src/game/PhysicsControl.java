/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;

/**
 *
 * @author Thomas
 */
public class PhysicsControl extends RigidBodyControl
    implements PhysicsCollisionListener {

    public void collision(PhysicsCollisionEvent event) {
        System.out.println("Collision!!");
    }
    
}
