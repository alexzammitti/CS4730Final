package edu.virginia.engine.display;

import java.util.ArrayList;

/**
 * Created by Alex on 3/2/17.
 */
public class PhysicsSprite extends AnimatedSprite {

    protected int xVelocity = 0;
    protected int yVelocity = 0;
    protected int xAcceleration = 0;
    protected int yAcceleration = 1;
    protected boolean airborne = false;
    protected boolean right = false;
    protected boolean left = false;
    protected boolean jump = false;
    protected boolean falling = false;

    public boolean isFalling() {
        return falling;
    }

    public void setFalling(boolean falling) {
        this.falling = falling;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isJump() {
        return jump;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public int getxVelocity() {
        return xVelocity;
    }

    public void setxVelocity(int xVelocity) {
        this.xVelocity = xVelocity;
    }

    public int getyVelocity() {
        return yVelocity;
    }

    public void setyVelocity(int yVelocity) {
        this.yVelocity = yVelocity;
    }

    public int getxAcceleration() {
        return xAcceleration;
    }

    public void setxAcceleration(int xAcceleration) {
        this.xAcceleration = xAcceleration;
    }

    public int getyAcceleration() {
        return yAcceleration;
    }

    public void setyAcceleration(int yAcceleration) {
        this.yAcceleration = yAcceleration;
    }

    public boolean isAirborne() {
        return airborne;
    }

    public void setAirborne(boolean airborne) {
        this.airborne = airborne;
    }

    public PhysicsSprite(String id) { super(id); }

    public PhysicsSprite(String id, String filename) {
        super(id, filename);
    }

    public void update(ArrayList<String> pressedKeys) {
        super.update(pressedKeys);
        if(right) {
            xVelocity = 7;
        }
        if(left) {
            xVelocity = -7;
        }
        if(!left && !right) {
            xVelocity = 0;
        }

        this.xVelocity = this.xVelocity + this.xAcceleration;
        this.xPosition = this.xPosition + this.xVelocity;


        if(airborne) {
            if(jump) {
                this.yVelocity = -20;
                this.falling = false;
            }
            this.yVelocity = this.yVelocity + this.yAcceleration;
            this.yPosition = this.yPosition + this.yVelocity;
            if(this.yVelocity < 0) {
                this.falling = true;
            }
        } else {
            this.yVelocity = 0;
            this.falling = false;
        }



    }
}
