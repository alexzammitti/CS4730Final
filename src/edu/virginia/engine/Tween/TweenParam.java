package edu.virginia.engine.Tween;

/**
 * Created by Alex on 3/17/17.
 */
public class TweenParam {

    protected TweenableParams specificParam;
    protected double startVal = 0;
    protected double endVal = 0;
    protected double time = 0;

    public TweenParam(TweenableParams par, double start, double end, double timing) {
        this.specificParam = par;
        this.startVal = start;
        this.endVal = end;
        this.time = timing;
    }

    public TweenableParams getSpecificParam() {
        return specificParam;
    }

    public double getStartVal() {
        return startVal;
    }

    public double getEndVal() {
        return endVal;
    }

    public double getTime() {
        return time;
    }
}
