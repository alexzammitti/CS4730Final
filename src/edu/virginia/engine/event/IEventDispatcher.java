package edu.virginia.engine.event;

/**
 * Created by jaz on 2/17/17.
 */
public interface IEventDispatcher {
    void addEventListener(IEventListener listener, String eventType);
    void removeEventDispatcher(IEventListener listener, String eventType);
    void dispatchEvent(Event event);
    boolean hasEventListener(IEventListener listener, String eventType);
}
