package edu.virginia.engine.event;

/**
 * Created by Alex on 2/16/17.
 */
public interface IEventDispatcher {

    void addEventListener(IEventListener listener, String eventType);
    void removeEventListener(IEventListener listener, String eventType);
    void dispatchEvent(Event event);
    boolean hasEventListener(IEventListener listener, String eventType);

}
