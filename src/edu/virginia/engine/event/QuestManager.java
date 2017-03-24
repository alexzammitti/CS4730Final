package edu.virginia.engine.event;

import edu.virginia.engine.display.DisplayObject;
import edu.virginia.engine.util.SoundManager;

/**
 * Created by Alex on 2/16/17.
 */
public class QuestManager implements IEventListener {

    public String myEventTrigger = "nope";

    public QuestManager() {}

    public String getMyEventTrigger() {
        return myEventTrigger;
    }

    public void handleEvent(Event event) {
        if(event.getEventType().equals(PickedUpEvent.COIN_PICKED_UP)) {
            System.out.println("Quest Completed!");
            myEventTrigger = "Picked Up a Coin!";
        }
    }

}
