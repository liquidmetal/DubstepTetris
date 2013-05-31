package in.liquidmetal.dubsteptetris;

import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by utkarsh on 26/5/13.
 */
public class BaseRect {
    protected float[] mModelView;

    private static final float COORDS[] = {
            -0.5f, -0.5f,   // Bottom left
             0.5f, -0.5f,   // Bottom right
            -0.5f,  0.5f,   // Top left
             0.5f,  0.5f    // Top right
    };

    private static final float TEX_COORDS[] = {
            0.0f,   1.0f,
            1.0f,   1.0f,
            0.0f,   0.0f,
            1.0f,   0.0f
    };

    private static final float OUTLINE_COORDS[] = {
           -0.5f, -0.5f,
            0.5f, -0.5f,
            0.5f,  0.5f,
           -0.5f,  0.5f
    };

    private static FloatBuffer sVertexArray = BaseRect.createVertexArray(COORDS);
    private static FloatBuffer sTexArray = BaseRect.createVertexArray(TEX_COORDS);
    private static FloatBuffer sOutlineArray = BaseRect.createVertexArray(OUTLINE_COORDS);

    protected static final int COORDS_PER_VERTEX = 2;     // x and y
    protected static final int TEX_COORDS_PER_VERTEX = 2; // u and v
    protected static final int VERTEX_STRIDE = COORDS_PER_VERTEX * 4;
    protected static final int TEX_VERTEX_STRIDE = TEX_COORDS_PER_VERTEX * 4;
    protected static final int VERTEX_COUNT = COORDS.length / COORDS_PER_VERTEX;

    protected BaseRect() {
        mModelView = new float[16];
        Matrix.setIdentityM(mModelView, 0);
    }

    private static FloatBuffer createVertexArray(float[] array) {
        FloatBuffer fb = ByteBuffer.allocateDirect(array.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        fb.put(array).position(0);
        return fb;
    }

    public static FloatBuffer getVertexArray() {
        return sVertexArray;
    }

    public static FloatBuffer getTexArray() {
        return sTexArray;
    }

    public static FloatBuffer getOutlineArray() {
        return sOutlineArray;
    }

    public float getXPosition() {
        return mModelView[12];
    }

    public float getYPosition() {
        return mModelView[13];
    }

    public void setPosition(float x, float y) {
        mModelView[12] = x;
        mModelView[13] = y;
    }

    public void setXPosition(float x) {
        mModelView[12] = x;
    }

    public void setYPosition(float y) {
        mModelView[13] = y;
    }

    public float getXScale() {
        return mModelView[0];
    }

    public float getYScale() {
        return mModelView[5];
    }

    public void setScale(float xs, float ys) {
        mModelView[0] = xs;
        mModelView[5] = ys;
    }

    @Override public String toString() {
        return "[BaseRect x=" + getXPosition() + " y=" + getYPosition() + " xs=" + getXScale() + " ys=" + getYScale() + "]";
    }
}
