package in.liquidmetal.dubsteptetris;

import android.view.View.OnClickListener;

class Clickable {

}

/**
 * Created by utkarsh on 6/6/13.
 */
public class GLButton extends TexturedAlignedRect {
    private OnClickListener myListener;

    public GLButton() {

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
