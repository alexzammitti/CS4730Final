package edu.virginia.engine.display;

import edu.virginia.engine.controller.GamePad;
import edu.virginia.engine.event.Event;

import java.util.ArrayList;

/**
 * Created by jaz on 4/11/17.
 */
public class Player extends PhysicsSprite {

    protected boolean alive = true;
    protected boolean courseCompleted = false;
    public DisplayObject platformPlayerIsOn;
    public Sprite item;
    public Sprite cursor;
    public int playerNumber;

    public boolean isAlive() {
        return alive;
    }
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    public boolean isCourseCompleted() {
        return courseCompleted;
    }
    public void setCourseCompleted(boolean courseCompleted) {
        this.courseCompleted = courseCompleted;
    }



    public Player(String id, String imageFileName, String cursorFileName, int number) {
        super(id,imageFileName);
        this.fileName = imageFileName;
        cursor = new Sprite(id + "cursor",cursorFileName);
        this.playerNumber = playerNumber;
    }

    public void update(ArrayList<Integer> pressedKeys, ArrayList<GamePad> gamePads) {
        super.update(pressedKeys,gamePads);
        if(this.isAlive()) {
            this.setxPosition(this.getxPosition()+xVelocity);
            this.setyPosition(this.getyPosition()+yVelocity);
            if(airborne) {
                this.setyVelocity(this.getyVelocity()+yAcceleration);
            }
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
