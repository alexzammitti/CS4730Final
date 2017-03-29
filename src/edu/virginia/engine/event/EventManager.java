package edu.virginia.engine.event;

/**
 * Created by Alex on 3/2/17.
 */
public class EventManager implements IEventListener {

    public String myEventTrigger = "nope";

    public EventManager() {}

    public String getMyEventTrigger() {
        return myEventTrigger;
    }

    public void handleEvent(Event event) {
        switch (event.getEventType()) {
            case Event.COLLISION:
                System.out.println("Collision!");
                myEventTrigger = "Collision!";
                break;
            case Event.COIN_PICKED_UP:

                break;
            case Event.TWEEN_COMPLETE_EVENT:

                break;
        }

    }
}
