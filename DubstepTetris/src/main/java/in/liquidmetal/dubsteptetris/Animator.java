package in.liquidmetal.dubsteptetris;

import java.util.Date;

/**
 * Created by utkarsh on 29/5/13.
 * A base class that keeps track of how much a particular animation has been updated.
 * You can reset the animation or get a ping about what percentage of the animation
 * has been completed
 */
public class Animator {
    private double duration;
    private long startTimestamp = -1;
    private long endTimestamp;
    private boolean canRemove = false;

    public Animator(double duration) {
        this.duration = duration;
    }

    public void start() {
        if(startTimestamp!=-1)
            return;

        startTimestamp = (new Date()).getTime();
    }

    public double update() {
        if(startTimestamp==-1)
            return 0;

        long diff = (new Date()).getTime() - startTimestamp;

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
