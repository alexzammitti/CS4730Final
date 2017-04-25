package edu.virginia.engine.display;

import edu.virginia.engine.controller.GamePad;

import java.util.ArrayList;

/**
 * Created by jaz on 4/24/17.
 */
public class SlidingPlatform extends Sprite {

    private int xStartPosition = 0;
    private int xEndPosition = 200;
    private int speed = 1;
    private int travelDistance = 200;
    private boolean direction = false;
    private Sprite platform = null;

    public SlidingPlatform(String id, String imageFileName) {
        super(id);
        this.platform = new Sprite(id + "platform",imageFileName);
        this.setImage("platform-track.png");
    }

    public void setStartPosition(int x, int y) {
        this.xStartPosition = x;
        this.xEndPosition = x + this.travelDistance;
        this.setyPosition(y);
    }

    @Override
    public void update(ArrayList<Integer> pressedKeys, ArrayList<GamePad> gamePads){
        super.update(pressedKeys,gamePads);
        if(direction) {
            if(this.platform.getxPosition() > xEndPosition) {
                direction = !direction;
            } else if( this.platform.getxPosition() < xEndPosition) {
                this.platform.setxPosition(this.platform.getxPosition() + speed);
            }
        } else {
            if(this.platform.getxPosition() < xStartPosition) {
                direction = !direction;
            } else if(this.platform.getxPosition() > xStartPosition) {
                this.platform.setxPosition(this.platform.getxPosition() - speed);
            }
        }
    }

}
