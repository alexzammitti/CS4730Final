package edu.virginia.engine.tween;

import edu.virginia.engine.display.DisplayObject;
import edu.virginia.engine.event.Event;

/**
 * Created by jaz on 3/17/17.
 */
public class TweenEvent extends Event {
    public final static String TWEEN_COMPLETE_EVENT = "tween_complete_event";

    TweenEvent(String eventType, Tween tween) {
        super(eventType);
        this.source = tween;
    }
    /*Tween getTween() {


    }

    */
}
