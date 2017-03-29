package edu.virginia.engine.tween;

import java.util.ArrayList;

/**
 * Created by jaz on 3/17/17.
 */
public class TweenJuggler {
    ArrayList<Tween> tweens = new ArrayList<>(0);
    private static TweenJuggler ourInstance = new TweenJuggler();
    public static TweenJuggler getInstance() {return ourInstance;}
    public TweenJuggler() {

    }
    public void add(Tween tween) {
        if(!tweens.contains(tween)){
            tweens.add(tween);
        }
    }
    public void nextFrame() {
        for (Tween tween:tweens) {
            tween.update();
        }
    }
}
