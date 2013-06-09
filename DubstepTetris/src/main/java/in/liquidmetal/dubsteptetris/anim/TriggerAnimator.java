package in.liquidmetal.dubsteptetris.anim;

/**
 * Created by utkarsh on 9/6/13.
 * Triggers a function when this expires
 */
public class TriggerAnimator extends Animator {
    private Trigger myTrigger;

    public TriggerAnimator(double duration, Trigger myTrigger) {
        super(duration);
        this.myTrigger = myTrigger;
    }

    public double update() {
        double value = super.update();

        if(value>=1.0) {
            myTrigger.onFire();
        }

        return value;
    }
}
