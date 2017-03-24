package edu.virginia.engine.Tween;

import edu.virginia.engine.display.DisplayObject;

import java.util.ArrayList;
import java.util.Timer;

/**
 * Created by Alex on 3/17/17.
 */
public class Tween {

    public boolean complete = false;
    public DisplayObject myDO;
    public TweenTransitions transition;
    public ArrayList<TweenParam> paramajama = new ArrayList<>();
    public TweenParam par;
    public double rate = 0;
    protected double currVal = 0;
    protected Timer clock;
    protected double percent = 0;
    protected int currTime = 0;


    public Tween (DisplayObject obj) {
        this.myDO = obj;
    }

    public Tween (DisplayObject obj, TweenTransitions transit) {
        this.myDO = obj;
        this.transition = transit;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setValue(TweenableParams param, double value) {

    }

    public void animate(TweenableParams fieldToAnimate, double startVal, double endVal, int time) {
        rate = (endVal - startVal) / time;
        par = new TweenParam(fieldToAnimate, startVal, endVal, time);
        currVal = startVal;
    }

    public void update() {
        switch(par.getSpecificParam()) {
            case X:
                if(!isComplete()) {
                    currTime++;
                    percent = currTime / par.getTime();
                    if(percent > 1) {
                        this.complete = true;
                    } else {
                        currVal = transition.applyTransition(percent) * (par.getEndVal() - par.getStartVal());
                        myDO.setxPosition((int) (currVal + par.getStartVal()));
                    }
                }
                break;
            case Y:
                if(!isComplete()) {
                    currTime++;
                    percent = currTime / par.getTime();
                    if(percent > 1) {
                        this.complete = true;
                    } else {
                        currVal = transition.applyTransition(percent) * (par.getEndVal() - par.getStartVal());
                        myDO.setyPosition((int) (currVal + par.getStartVal()));
                    }
                }
                break;
            case ALPHA:
                if(!isComplete()) {
                    currTime++;
                    percent = currTime / par.getTime();
                    if(percent > 1) {
                        this.complete = true;
                    } else {
                        currVal = transition.applyTransition(percent) * (par.getEndVal() - par.getStartVal());
                        myDO.setAlpha((float) (currVal + par.getStartVal()));
                    }
                }
                break;
            case ROT:
                if(!isComplete()) {
                    currTime++;
                    percent = currTime / par.getTime();
                    if(percent > 1) {
                        this.complete = true;
                    } else {
                        currVal = transition.applyTransition(percent) * (par.getEndVal() - par.getStartVal());
                        myDO.setRotation(currVal + par.getStartVal());
                    }
                }
                break;
            case SCALE_X:
                if(!isComplete()) {
                    currTime++;
                    percent = currTime / par.getTime();
                    if(percent > 1) {
                        this.complete = true;
                    } else {
                        currVal = transition.applyTransition(percent) * (par.getEndVal() - par.getStartVal());
                        myDO.setxScale(currVal + par.getStartVal());
                    }
                }
                break;
            case SCALE_Y:
                if(!isComplete()) {
                    currTime++;
                    percent = currTime / par.getTime();
                    if(percent > 1) {
                        this.complete = true;
                    } else {
                        currVal = transition.applyTransition(percent) * (par.getEndVal() - par.getStartVal());
                        myDO.setyScale(currVal + par.getStartVal());
                    }
                }
                break;
        }
    }

}
