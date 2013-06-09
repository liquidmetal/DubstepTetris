package in.liquidmetal.dubsteptetris.anim;

import java.util.Date;

/**
 * Created by utkarsh on 29/5/13.
 * A base class that keeps track of how much a particular animation has been updated.
 * You can reset the animation or get a ping about what percentage of the animation
 * has been completed
 */
public class Animator {
    private double duration;
    private long startDelayedTimestamp = -1;
    private long delay = -1;
    private long startTimestamp = -1;
    private long endTimestamp;
    private boolean canRemove = false;

    private Animator animatorToCheck = null;

    public Animator(double duration) {
        this.duration = duration;
    }

    public void startDelayed(int delay) {
        if(startDelayedTimestamp!=-1)
            return;

        startDelayedTimestamp = (new Date()).getTime();
        this.delay = delay;
    }

    public void start() {
        if(startTimestamp!=-1)
            return;

        startTimestamp = (new Date()).getTime();
    }

    public void startWhenEnds(Animator anim) {
        animatorToCheck = anim;
    }

    public double update() {
        if(animatorToCheck!=null && startTimestamp==-1 && animatorToCheck.canGarbageCollect()) {
            start();
            return 0;
        }

        long diffDelayed = (new Date()).getTime() - startDelayedTimestamp;
        if(startDelayedTimestamp!=-1) {
            if(diffDelayed>=delay)
                start();
        }


        long diff = (new Date()).getTime() - startTimestamp;
        if(startTimestamp==-1)
            return 0;



        if(diff >= duration) {
            canRemove = true;
            return 1.0;
        }

        return diff/duration;
    }

    public void reset() {
        startTimestamp = -1;
        canRemove = false;
    }

    public boolean canGarbageCollect() {
        return canRemove;
    }
}
