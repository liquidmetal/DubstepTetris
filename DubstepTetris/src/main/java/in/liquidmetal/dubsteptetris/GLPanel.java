package in.liquidmetal.dubsteptetris;

import java.util.LinkedList;

/**
 * Created by utkarsh on 8/6/13.
 */
public class GLPanel extends AlignedRect {
    private LinkedList<GLButton> listButtons = new LinkedList<GLButton>();
    private LinkedList<GLText> listText = new LinkedList<GLText>();

    public GLPanel() {

    }

    public void addNewButton(String text, long textureHandle, Clickable listener) {
        listButtons.add(new GLButton(listener));
    }

    public void addNewText(String text) {
        listText.add(new GLText(48, 128, text, 48, 0, 0));
    }

    public boolean tapped(int x, int y) {
        for(GLButton btn:listButtons) {
            btn.TestClick(x, y);
        }
    }

    public void draw() {
        for(GLButton button:listButtons) {
            button.draw();
        }

        for(GLText text:listText) {
            text.draw();
        }
    }
}
