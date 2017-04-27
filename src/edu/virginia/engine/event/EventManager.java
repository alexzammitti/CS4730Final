package edu.virginia.engine.event;

import edu.virginia.engine.display.DisplayObject;
import edu.virginia.engine.display.PhysicsSprite;
import edu.virginia.engine.display.Player;
import edu.virginia.engine.tween.*;
import edu.virginia.engine.util.SoundEffect;

import java.awt.*;

/**
 * Created by Alex on 3/2/17.
 */
public class EventManager implements IEventListener {

    private SoundEffect deathSound = new SoundEffect("death.wav");
    private SoundEffect thudSound = new SoundEffect("thud.wav");
    private SoundEffect goalSound = new SoundEffect("goal.wav");


    public void handleEvent(Event event) {
        switch (event.getEventType()) {
            case Event.COLLISION:
                break;
            case Event.COIN_PICKED_UP:
                break;
            case Event.TWEEN_COMPLETE_EVENT:
                break;
            case Event.UNSAFE_COLLISION:
                if(event.player.isAlive()) deathSound.play(false);
                deathAnimation(event);
                event.player.setAlive(false);
                break;
            case Event.SAFE_COLLISION:
                if(event.player.isAirborne()) thudSound.play(false);
                handlePlatformCollision(event.player,event.object);
                break;
            case Event.DEATH:
                deathAnimation(event);
                break;
            case Event.GOAL:
                if(!event.player.isCourseCompleted()) goalSound.play(false);
                event.player.setCourseCompleted(true);
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
                    character.hoverClock.resetGameClock();
            }
            else if(character.getyPosition() > platform.getTop()) { //bottom of platform
                    if(character.platformPlayerIsOn != null) {
                        if(character.isAlive()) deathSound.play(false);
                        character.setyScale(0.5*character.getyScale());
                        character.setAlive(false);
                    }
                    if(platform.getFileName().contains("sliding"))   {
                    character.setyPosition(platform.getHitbox().y+platform.getHitbox().height + 10);
                } else {
                    character.setyPosition(platform.getHitbox().y + platform.getHitbox().height);
                }
                character.setAirborne(true);
                character.setOnPlatform(false);
                character.platformPlayerIsOn = null;
                character.setyVelocity(0);
            }
        } else {
            if(character.getRight() > platform.getLeft() && intersection.x == platform.getLeft()) {
                if(platform.getFileName().contains("sliding")) {
                    character.setxPosition(platform.getLeft()-character.getScaledWidth()-10);
                } else {
                    character.setxPosition(platform.getLeft() - character.getScaledWidth() - 1);
                }
                character.setxVelocity((int)(character.getxVelocity()*-1.2));
            }
            else if(character.getLeft() < platform.getRight()) {
                if(platform.getFileName().contains("sliding")) {
                    character.setxPosition(platform.getRight()+10);
                } else {
                    character.setxPosition(platform.getRight() + 1);
                }
                character.setxVelocity((int)(character.getxVelocity()*-1.2));
            }
        }
    }

    public void deathAnimation(Event event) {
        if(event.player.isAlive()) {
            Tween deathTweenY, deathTweenAlpha, deathTweenRotate;
            deathTweenY = new Tween(event.player, new TweenTransition(TweenTransition.TransitionType.LINEAR));
            deathTweenAlpha = new Tween(event.player, new TweenTransition(TweenTransition.TransitionType.LINEAR));
            deathTweenRotate = new Tween(event.player, new TweenTransition(TweenTransition.TransitionType.LINEAR));
            deathTweenY.animate(TweenableParam.Y, event.player.getyPosition(), event.player.getyPosition() - 100, 100);
            deathTweenAlpha.animate(TweenableParam.ALPHA, 1, 0, 100);
            deathTweenRotate.animate(TweenableParam.ROTATION, 0, 5, 100);
            event.player.setPivotCenter();
            TweenJuggler.getInstance().add(deathTweenAlpha);
            TweenJuggler.getInstance().add(deathTweenY);
            TweenJuggler.getInstance().add(deathTweenRotate);
        }
    }
}
