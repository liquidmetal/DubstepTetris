package in.liquidmetal.dubsteptetris;

/**
 * Created by utkarsh on 1/6/13.
 */
public class PositionAnimator extends Animator {
    private BaseRect objectToAnimate = null;
    private long deltaX = 0, deltaY = 0;
    private long currentDeltaX = 0, currentDeltaY = 0;
    private long originalX = 0, originalY = 0;
    private int state=0;

    public PositionAnimator(double duration, long deltaX, long deltaY) {
        super(duration);

        this.deltaX = deltaX;
        this.deltaY = deltaY;
        state = 0;
    }

    public void setObjectToAnimate(BaseRect rect) {
        objectToAnimate = rect;
        originalX = (long)objectToAnimate.getXPosition();
        originalY = (long)objectToAnimate.getYPosition();
    }

    public double update() {
        double value = super.update();

        currentDeltaX = (long)(deltaX*value);
        currentDeltaY = (long)(deltaY*value);

        if(objectToAnimate != null && state==0) {
            objectToAnimate.setPosition(originalX + currentDeltaX, originalY + currentDeltaY);
        }

        if(value>=1.0 && state==0) {
            state = 1;
        }

        return value;
    }

    public long[] getCurrentDelta() {
        long[] ret ={currentDeltaX, currentDeltaY};
        return ret;
    }
}
