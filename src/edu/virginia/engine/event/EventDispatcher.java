package edu.virginia.engine.event;

import java.util.*;

/**
 * Created by Alex on 2/16/17.
 */
public class EventDispatcher implements IEventDispatcher {

    public HashMap<String, IEventListener> myMap = new HashMap<>();


    public void addEventListener(IEventListener listener, String eventType) {
        myMap.put(eventType, listener);
    }

    public void removeEventListener(IEventListener listener, String eventType) {
        myMap.remove(eventType, listener);
    }

    public void dispatchEvent(Event event) {
        String key = event.getEventType();
        IEventListener buzzer = myMap.get(key);
        buzzer.handleEvent(event);
    }

    public boolean hasEventListener(IEventListener listener, String eventType) {
        if(myMap.containsKey(eventType)) {
            if(myMap.get(eventType) == listener) {
                return true;
            }
            else {
                return false;
            }
        } else {
            return false;
        }
    }

}
