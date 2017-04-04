package edu.virginia.engine.display;

import edu.virginia.engine.event.Event;

import java.util.ArrayList;

/**
 * Created by jaz on 3/2/17.
 */
public class PhysicsSprite extends AnimatedSprite {

    public int mass;
    public int xAcceleration;
    public int yAcceleration;
    public int xVelocity;
    public int yVelocity;
    public int xForce;
    public int yForce;
    public boolean airborne = true;
    public boolean onPlatform = false;
    public boolean alive = true;

    public int getMass() {return mass;}
    public void setMass(int mass) {this.mass = mass;}
    public int getxAcceleration() {return xAcceleration;}
    public void setxAcceleration(int xAcceleration) {this.xAcceleration = xAcceleration;}
    public int getyAcceleration() {return yAcceleration;}
    public void setyAcceleration(int yAcceleration) {this.yAcceleration = yAcceleration;}
    public int getxVelocity() {return xVelocity;}
    public void setxVelocity(int xVelocity) {this.xVelocity = xVelocity;}
    public int getyVelocity() {return yVelocity;}
    public void setyVelocity(int yVelocity) {this.yVelocity = yVelocity;}
    public int getxForce() {return xForce;}
    public void setxForce(int xForce) {this.xForce = xForce;}
    public int getyForce() {return yForce;}
    public void setyForce(int yForce) {this.yForce = yForce;}

    public PhysicsSprite(String id, String imageFileName) {
        super(id, imageFileName);
        mass=1;
        xAcceleration=0;
        yAcceleration=0;
        xVelocity=0;
        yVelocity=0;
        xForce=0;
        yForce=0;
    }

    public void update(ArrayList<Integer> pressedKeys) {
        super.update(pressedKeys);
        if(alive) {
            this.setxPosition(this.getxPosition()+xVelocity);
            this.setyPosition(this.getyPosition()+yVelocity);
            if(airborne) {
                this.setyVelocity(this.getyVelocity()+yAcceleration);
            }
        } else {
            this.visible = false;
        }
    }

    @Override
    public boolean collidesWith(DisplayObject object) {
        if(object != null) {
            if (object.getHitbox().intersects(this.hitbox)) {
                if(object.dangerous) {
                    this.dispatchEvent(new Event(this,object,Event.UNSAFE_COLLISION));
                } else {
                    this.dispatchEvent(new Event(this,object,Event.SAFE_COLLISION));
                }
                return true;
            }
        }
        return false;
    }

}
