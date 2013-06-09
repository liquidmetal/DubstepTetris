package in.liquidmetal.dubsteptetris.anim;

import in.liquidmetal.dubsteptetris.BaseRect;
import in.liquidmetal.dubsteptetris.anim.Animator;

/**
 * Created by utkarsh on 4/6/13.
 */
public class ScaleAnimator extends Animator {

    private BaseRect mBaseRect;
    private float originalScaleX, originalScaleY;
    private float destScaleX, destScaleY;

    public ScaleAnimator(double duration, BaseRect rect, float sx, float sy) {
        super(duration);

        mBaseRect = rect;
        originalScaleX = rect.getXScale();
        originalScaleY = rect.getYScale();

        destScaleX = sx;
        destScaleY = sy;
    }

    public double update() {
        double value = super.update();

        mBaseRect.setScale((float)value*(destScaleX-originalScaleX) + originalScaleX, (float)value*(destScaleY-originalScaleY) + originalScaleY);

        return value;
    }
}
