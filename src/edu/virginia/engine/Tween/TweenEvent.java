package edu.virginia.engine.Tween;

import edu.virginia.engine.event.Event;
import edu.virginia.engine.event.IEventDispatcher;

/**
 * Created by Alex on 3/16/17.
 */
public class TweenEvent extends Event {

    public static String TWEEN = "tween";

    public TweenEvent(IEventDispatcher source) {
        super(TWEEN, source);
    }

    public TweenEvent(String eventType, IEventDispatcher source) {super(eventType, source);}

    public void getTween() { //fix this line

    }



}
