package edu.virginia.engine.Tween;

import java.util.ArrayList;

/**
 * Created by Alex on 3/17/17.
 */
public class TweenJuggler {
    private static TweenJuggler ourInstance = new TweenJuggler();

    public static TweenJuggler getInstance() {
        return ourInstance;
    }

    private ArrayList<Tween> myTweens = new ArrayList<>();

    public TweenJuggler() {
    }

    public void add(Tween t) {
        this.myTweens.add(t);
    }

    public boolean remove(Tween t) {
        return this.myTweens.remove(t);
    }

    public void update() {
        for (Tween t: myTweens) {
            t.update();
        }
    }
}
