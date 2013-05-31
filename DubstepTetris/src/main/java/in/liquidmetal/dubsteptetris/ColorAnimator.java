package in.liquidmetal.dubsteptetris;

/**
 * Created by utkarsh on 29/5/13.
 */
public class ColorAnimator extends Animator {
    private float[] startColor;
    private float[] endColor;
    private float[] currentColor;

    // 0 -> initial state
    // 1 -> Forward animation
    // 2 -> forward animation ended
    // 3 -> backward animation
    int state = 0;

    public ColorAnimator(double duration, float[] startColor, float[] endColor) {
        super(duration);

        this.startColor = startColor.clone();
        this.endColor = endColor.clone();
        this.currentColor = startColor.clone();
        state = 0;
    }

    public void start() {
        super.start();
        state = 1;
    }

    public double update() {
        double value = super.update();

        for(int i=0;i<3;i++) {
            if(state==1)
                currentColor[i] = startColor[i] * (1.0f-(float)value) + endColor[i] * (float)value;
            else if(state==3)
                currentColor[i] = startColor[i] * (float)value + endColor[i] * (1.0f-(float)value);
        }

        if(value==1) {
            if(state==1)
                state = 2;
            else if(state==3)
                state = 0;
        }
        return value;
    }

    public float[] getCurrentColor() {
        return currentColor.clone();
    }

    // You can only reverse once the whole animation has completed
    public void reverse() {
        if(state==2) {
            state = 3;
            super.start();
        }
    }
}
