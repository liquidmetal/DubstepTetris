package in.liquidmetal.dubsteptetris.ui;

import in.liquidmetal.dubsteptetris.TexturedAlignedRect;

/**
 * Created by utkarsh on 6/6/13.
 */
public class GLButton extends TexturedAlignedRect {
    private Clickable myListener;

    public GLButton(Clickable newListener) {
        myListener = newListener;
    }

    public void TestClick(int x, int y) {
        // Test bounds
        if(isPointInside(x, y))
            myListener.onClick();
    }

    public boolean isPointInside(int x, int y) {
        return true;
    }
}
