package edu.virginia.engine.event;

import java.util.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jaz on 2/17/17.
 */
public class EventDispatcher implements IEventDispatcher {

    private HashMap<String,ArrayList<IEventListener>> listenerMap = new HashMap<>(0);

    public void addEventListener(IEventListener listener, String eventType) {
        if(listenerMap.containsKey(eventType)){
            if (listenerMap.get(eventType).size() <= 0) {
                ArrayList<IEventListener> listeners = new ArrayList<>(0);
                listeners.add(listener);
                listenerMap.put(eventType, listeners);
            } else {
                listenerMap.get(eventType).add(listener);
            }
        } else {
            ArrayList<IEventListener> list = new ArrayList<>(0);
            list.add(listener);
            listenerMap.put(eventType,list);
        }
    }
    public void removeEventDispatcher(IEventListener listener, String eventType){
        if(!(listenerMap.get(eventType).size() <= 0)) {
            listenerMap.get(eventType).remove(listener);
        }
    }
    public void dispatchEvent(Event event){
        //System.out.println("Listener map size: " + listenerMap.size());
        if(listenerMap.size() > 0) {
            ArrayList<IEventListener> list = listenerMap.get(event.getEventType());
            int listSize = list.size();
            for (int i = 0; i < listSize; i++) {
                list.get(i).handleEvent(event);
            }
        }
    }
    public boolean hasEventListener(IEventListener listener, String eventType){
        return listenerMap.get(eventType).contains(listener);
    }
}
