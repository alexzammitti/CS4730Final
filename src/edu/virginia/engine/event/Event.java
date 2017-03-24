package edu.virginia.engine.event;

import edu.virginia.engine.display.DisplayObject;

/**
 * Created by Alex on 2/16/17.
 */
public class Event {

    public String eventType;
    public IEventDispatcher source;

    public Event(String type, IEventDispatcher src) {
        this.eventType = type;
        System.out.println("IEventDispatcher event");
        this.source = src;
    }

    public String getEventType() {
        return eventType;
    }

    public IEventDispatcher getSource() {
        return source;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setSource(IEventDispatcher source) {
        this.source = source;
    }
}
