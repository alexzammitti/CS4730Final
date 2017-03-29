package edu.virginia.engine.tween;

/**
 * Created by jaz on 3/17/17.
 */
public class TweenParam {

    double startVal, endVal, time;
    TweenableParam param;

    public TweenParam(TweenableParam paramToTween, double startVal, double endVal, double time) {
        this.startVal = startVal;
        this.endVal = endVal;
        this.time = time;
        this.param = paramToTween;
    }
    TweenableParam getParam(){
        return param;
    }
    double getStartVal() {
        return startVal;
    }
    double getEndVal() {
        return endVal;
    }
    double getTweenTime() {
        return time;
    }
}
