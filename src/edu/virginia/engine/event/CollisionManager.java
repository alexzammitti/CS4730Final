package edu.virginia.engine.event;

/**
 * Created by Alex on 3/2/17.
 */
public class CollisionManager implements IEventListener {

    public String myEventTrigger = "nope";

    public CollisionManager() {}

    public String getMyEventTrigger() {
        return myEventTrigger;
    }

    public void handleEvent(Event event) {
        if(event.getEventType().equals(CollisionEvent.COLLISION)) {
            System.out.println("Collision!");
            myEventTrigger = "Collision!";
        }
    }
}
