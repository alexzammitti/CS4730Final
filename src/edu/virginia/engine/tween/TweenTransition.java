package edu.virginia.engine.tween;

/**
 * Created by jaz on 3/17/17.
 */
public class TweenTransition {
    public enum TransitionType {
        LINEAR, EXPONENTIAL
    }

    TransitionType type;

    public TweenTransition(TransitionType t) {
        this.type = t;
    }

    public double applyTransition(double percentDone) {
        switch(this.type) {
            case EXPONENTIAL:
                return -(Math.pow(2,-10*percentDone))+1;
            case LINEAR:
                return percentDone;
        }
        return -1.0;
    }
    //math part of tweening
}
