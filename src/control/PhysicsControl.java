/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.control.RigidBodyControl;

/**
 *
 * @author Thomas
 */
public class PhysicsControl extends RigidBodyControl
        implements PhysicsCollisionListener {

    public PhysicsControl(){
    }
    
    public void collision(PhysicsCollisionEvent event) {
        if (event.getNodeB() != null) {
            if (event.getNodeB().getControl(MissileControl.class) != null) {
                System.out.println("Collision!!" + event.getNodeA().getName() + " --- " + event.getNodeB().getName());
                event.getNodeB().getControl(MissileControl.class).hitAsteroid();
                //TODO: Add effect and remove life from asteroid.
            }
        }
    }
}
