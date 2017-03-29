package edu.virginia.engine.event;

import edu.virginia.engine.display.DisplayObject;

/**
 * Created by jaz on 2/17/17.
 */
public class Event {
    public final static String TWEEN_COMPLETE_EVENT = "tween_complete_event";
    public final static String COLLISION = "collision";
    public final static String COIN_PICKED_UP = "coin picked up";

    public String eventType;
    public EventDispatcher source;

    public Event(String type, DisplayObject src) {
        this.eventType = type;
        this.source = src;
    }
    public Event(String type) {
        this.eventType = type;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public EventDispatcher getSource() {
        return source;
    }

    public void setSource(EventDispatcher source) {
        this.source = source;
    }
}
