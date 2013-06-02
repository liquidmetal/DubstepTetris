package in.liquidmetal.dubsteptetris;

import java.util.LinkedList;

/**
 * Created by utkarsh on 2/6/13.
 */
public class DestroyAnimator extends Animator {
    private LinkedList<TexturedAlignedRect> objsToDestroy = new LinkedList<TexturedAlignedRect>();
    GameSurfaceRenderer renderer;

    public DestroyAnimator(double duration, GameSurfaceRenderer renderer) {
        super(duration);

        this.renderer = renderer;
    }

    public void addObject(TexturedAlignedRect obj) {
        objsToDestroy.add(obj);
    }

    public double update() {
        double value = super.update();

        if(value>=1.0) {
            for(TexturedAlignedRect r:objsToDestroy) {
                renderer.deleteTempObject(r);
            }
        }

        return value;
    }
}
