package in.liquidmetal.dubsteptetris;

import java.util.LinkedList;

/**
 * Created by utkarsh on 2/6/13.
 */
public class AlphaAnimator extends Animator {
    private float originalAlpha, destinationAlpha;

    private TexturedAlignedRect alphaObject = null;

    public AlphaAnimator(double duration, TexturedAlignedRect alphaObject, float destAlpha) {
        super(duration);
        this.alphaObject = alphaObject;

        this.originalAlpha = alphaObject.getAlphaMultiplier();
        this.destinationAlpha = destAlpha;
    }

    public double update() {
        double value = super.update();

        alphaObject.setAlphaMultiplier(originalAlpha + (destinationAlpha - originalAlpha) * (float)value);

        return value;
    }
}
