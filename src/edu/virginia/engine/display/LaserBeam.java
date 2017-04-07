package edu.virginia.engine.display;

/**
 * Created by jaz on 4/7/17.
 */
public class LaserBeam extends Sprite {

    public double direction;

    public LaserBeam(String id, double direction) {
        super(id,"laserbeam.png");
        this.direction = direction;
        this.setRotation(direction);
    }
    @Override
    public void alignCenterVertical(DisplayObject parent) {
        int y = parent.getScaledHeight()/2 - this.getScaledHeight()/2 + parent.getyAbsolutePosition();
        this.setyPosition(y);
    }
    @Override
    public void alignCenterHorizontal(DisplayObject parent) {
        int x = parent.getScaledWidth()/2 - this.getScaledWidth()/2 + parent.getxAbsolutePosition();
        this.setxPosition(x);
    }
}
