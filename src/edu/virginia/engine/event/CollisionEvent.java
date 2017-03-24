package edu.virginia.engine.event;

/**
 * Created by Alex on 3/2/17.
 */
public class CollisionEvent extends Event {

    public static String COLLISION = "collided";

    public CollisionEvent(IEventDispatcher source) {
        super(COLLISION, source);
    }

}
