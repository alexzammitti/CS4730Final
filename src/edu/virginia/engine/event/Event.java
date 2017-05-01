package edu.virginia.engine.event;

import edu.virginia.engine.display.DisplayObject;
import edu.virginia.engine.display.PhysicsSprite;
import edu.virginia.engine.display.Player;
import edu.virginia.engine.display.Sprite;

/**
 * Created by jaz on 2/17/17.
 */
public class Event {
    public final static String TWEEN_COMPLETE_EVENT = "tween_complete_event";
    public final static String COLLISION = "collision";
    public final static String SAFE_COLLISION = "safe collision";
    public final static String UNSAFE_COLLISION = "unsafe collision";
    public final static String DEATH = "death";
    public final static String GOAL = "goal";



    private String eventType;
    protected EventDispatcher source;

    // COLLISION FIELDS
    Player player;
    DisplayObject object;

    public Event(String type, DisplayObject src) {
        this.eventType = type;
        this.source = src;
    }
    public Event(String type) {
        this.eventType = type;
    }
    public Event(String type, Player player) {
        this.eventType = type;
        this.player = player;
    }
    public Event(Player player, DisplayObject object, String type) {
        this.eventType = type;
        this.player = player;
        this.object = object;
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
