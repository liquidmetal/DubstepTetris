package in.liquidmetal.dubsteptetris;

/**
 * Created by utkarsh on 4/6/13.
 */
public class OpacityAnimator extends Animator {
    private TexturedAlignedRect mBaseRect;
    private float originalOpacity;
    private float destOpacity;

    public OpacityAnimator(double duration, TexturedAlignedRect obj, float destOp) {
        super(duration);
        mBaseRect = obj;

        originalOpacity = obj.getAlphaMultiplier();
        destOpacity = destOp;
    }

    public OpacityAnimator(double duration, TexturedAlignedRect obj, float destOp, float startOp) {
        super(duration);
        mBaseRect = obj;

        originalOpacity = startOp;
        destOpacity = destOp;
    }

    public double update() {
        double value = super.update();

        if(value>0)
            mBaseRect.setAlphaMultiplier(originalOpacity + (float)value*(destOpacity-originalOpacity));

        return value;
    }
}
