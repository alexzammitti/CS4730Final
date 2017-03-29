package edu.virginia.engine.event;

import edu.virginia.engine.display.DisplayObject;

/**
 * Created by Alex on 3/2/17.
 */
public class EventManager implements IEventListener {

    public EventManager() {}

    public void handleEvent(Event event) {
        switch (event.getEventType()) {
            case Event.COLLISION:

                break;
            case Event.COIN_PICKED_UP:

                break;
            case Event.TWEEN_COMPLETE_EVENT:

                break;
        }

    }
}
