package edu.virginia.engine.display;

import edu.virginia.engine.controller.GamePad;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by jaz on 4/24/17.
 */
public class SlidingPlatform extends Sprite {

    private int xStartPosition = 0;
    private int xMiddlePosition = 100;
    private int xEndPosition = 200;
    private int speed = 1;
    private int travelDistance = 200;
    private boolean direction = false;
    private Sprite platform = null;

    public int getTravelDistance() {
        return travelDistance;
    }
    public void setTravelDistance(int travelDistance) {
        this.travelDistance = travelDistance;
    }
    public Sprite getPlatform() {
        return platform;
    }

    public SlidingPlatform(String id, String imageFileName) {
        super(id);
        this.platform = new Sprite(id + "platform",imageFileName);
        this.platform.setScale(0.8,0.8);
        //this.setImage("slidingplatformtrack.png");
    }

    public void setxStartPosition(int middle) {
        this.xMiddlePosition = middle;
        this.xStartPosition = middle -travelDistance - this.platform.getScaledWidth()/2;
        this.xEndPosition = middle + this.travelDistance - this.platform.getScaledWidth()/2;
    }

    @Override
    public void draw(Graphics g) {
        this.platform.draw(g);
    }

    @Override
    public void update(ArrayList<Integer> pressedKeys, ArrayList<GamePad> gamePads){
        super.update(pressedKeys,gamePads);
        this.setxStartPosition(xMiddlePosition);
        if(direction) {
            if(this.platform.getxPosition() >= xEndPosition) {
                this.platform.setxPosition(xEndPosition);
                direction = !direction;
            } else if( this.platform.getxPosition() < xEndPosition) {
                this.platform.setxPosition(this.platform.getxPosition() + speed);
            }
        } else {
            if(this.platform.getxPosition() <= xStartPosition) {
                this.platform.setxPosition(xStartPosition);
                direction = !direction;
            } else if(this.platform.getxPosition() > xStartPosition) {
                this.platform.setxPosition(this.platform.getxPosition() - speed);
            }
        }
    }

}
