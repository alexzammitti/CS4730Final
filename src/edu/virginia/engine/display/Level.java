package edu.virginia.engine.display;

import java.util.ArrayList;

/**
 * Created by jaz on 4/19/17.
 */
public class Level extends Sprite {

    private Sprite background;

    public Level(String id, Sprite background) {
        super(id);
        this.background = background;
    }

    public Sprite getBackground() {
        return background;
    }

    public void setBackground(Sprite background) {
        this.background = background;
    }

    public void setPositionAndScaling() {
        this.background.setPosition(0,0);
        switch (this.getId()){
            case "level1":
                this.background.setScale(3.8,3.6);
                break;
            case "level2":
                this.background.setScale(2,2);
                break;
            case "level3":
                this.background.setScale(2,2);
        }
    }
}
