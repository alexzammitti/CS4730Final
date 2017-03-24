package edu.virginia.engine.event;

import edu.virginia.engine.display.DisplayObject;
import edu.virginia.engine.display.Sprite;

/**
 * Created by Alex on 2/16/17.
 */
public class PickedUpEvent extends Event {

    public static String COIN_PICKED_UP = "coinPickedUp";

    public PickedUpEvent(IEventDispatcher source) {
        super(COIN_PICKED_UP, source);
    }

}
