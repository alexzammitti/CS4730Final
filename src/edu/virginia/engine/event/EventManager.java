package edu.virginia.engine.event;

import edu.virginia.engine.display.DisplayObject;
import edu.virginia.engine.display.PhysicsSprite;
import edu.virginia.engine.display.Player;
import edu.virginia.engine.tween.*;

import java.awt.*;

/**
 * Created by Alex on 3/2/17.
 */
public class EventManager implements IEventListener {

    private Tween deathTweenY, deathTweenAlpha, deathTweenRotate;

    public void handleEvent(Event event) {
        switch (event.getEventType()) {
            case Event.COLLISION:
                break;
            case Event.COIN_PICKED_UP:
                break;
            case Event.TWEEN_COMPLETE_EVENT:
                break;
            case Event.UNSAFE_COLLISION:
                event.player.setAlive(false);
                //event.player.dispatchEvent(new Event(Event.DEATH,event.player));
                break;
            case Event.SAFE_COLLISION:
                handlePlatformCollision(event.player,event.object);
                break;
            case Event.DEATH:
                deathAnimation(event);
                break;
            case Event.GOAL:
                event.player.setCourseCompleted(true);
                event.player.incrementScore(1);
                break;
        }

    }

    public void handlePlatformCollision(Player character, DisplayObject platform) {
        Rectangle intersection = character.getHitbox().intersection(platform.getHitbox());
        if( intersection.width > intersection.height && character.getBottom() > platform.getTop()
                || intersection.width > intersection.height && character.getTop() < platform.getBottom()){
                if(character.getyPosition() < platform.getTop()) {                                          //top of platform
                character.setyPosition(platform.getHitbox().y-(character.getScaledHeight()+1));
                character.setAirborne(false);
                character.setOnPlatform(true);
                character.platformPlayerIsOn = platform;
                character.setyVelocity(0);
            }
            else if(character.getyPosition() > platform.getTop()) {                                         //bottom of platform
                character.setyPosition(platform.getHitbox().y+platform.getHitbox().height);
                character.setAirborne(true);
                character.setOnPlatform(false);
                character.setyVelocity(0);
            }
        } else {
            if(character.getRight() > platform.getLeft() && intersection.x == platform.getLeft()) {
                character.setxPosition(platform.getLeft()-character.getScaledWidth()-1);
                character.setxVelocity((int)(character.getxVelocity()*-1.2));
            }
            else if(character.getLeft() < platform.getRight()) {
                character.setxPosition(platform.getRight()+1);
                character.setxVelocity((int)(character.getxVelocity()*-1.2));
            }
        }
    }

    public void deathAnimation(Event event) {
        deathTweenY = new Tween(event.player, new TweenTransition(TweenTransition.TransitionType.LINEAR));
        deathTweenAlpha = new Tween(event.player, new TweenTransition(TweenTransition.TransitionType.LINEAR));
        deathTweenRotate = new Tween(event.player, new TweenTransition(TweenTransition.TransitionType.LINEAR));
        deathTweenY.animate(TweenableParam.Y,event.player.getyPosition(),event.player.getyPosition()-100,100);
        deathTweenAlpha.animate(TweenableParam.ALPHA,1,0,100);
        deathTweenRotate.animate(TweenableParam.ROTATION,0,5,100);
        event.player.setPivotCenter();
        TweenJuggler.getInstance().add(deathTweenAlpha);
        TweenJuggler.getInstance().add(deathTweenY);
        TweenJuggler.getInstance().add(deathTweenRotate);
    }
}
