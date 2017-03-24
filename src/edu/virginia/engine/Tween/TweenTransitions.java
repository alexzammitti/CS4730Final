package edu.virginia.engine.Tween;

/**
 * Created by Alex on 3/17/17.
 */
public class TweenTransitions {

    public double percentDone;
    protected String type = "";

    public TweenTransitions(String t) {
        this.type = t;
    }

    public double applyTransition(double percent) {
        if(type.equals("linear")) {
           return percent;
        } else if(type.equals("exponential")) {
            return (percent*percent);
        } else if(type.equals("square")) {
            return Math.sqrt(percent);
        }
        else {return 0;}
    }

    //public double linear(double percent) {

    //}


}
