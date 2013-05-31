package in.liquidmetal.dubsteptetris;

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.FloatBuffer;

/**
 * Created by utkarsh on 26/5/13.
 */
public class AlignedRect extends BaseRect {
    static final String VERTEX_SHADER_CODE =
            "uniform mat4 u_mvpMatrix; " +
            "attribute vec4 a_position; " +
            "void main() {" +
            "    gl_Position = u_mvpMatrix * a_position;" +
            "}";

    static final String FRAGMENT_SHADER_CODE =
            "precision mediump float;" +
            "uniform vec4 u_color;" +
            "void main() {" +
            "    gl_FragColor = u_color;"+
            "}";

    static FloatBuffer sVertexBuffer = getVertexArray();
    static int sProgramHandle = -1;
    static int sColorHandle = -1;
    static int sPositionHandle = -1;
    static int sMVPMatrixHandle = -1;

    float[] mColor = new float[4];

    static float[] sTempMVP = new float[16];

    public static void createProgram() {
        sProgramHandle = Util.createProgram(VERTEX_SHADER_CODE, FRAGMENT_SHADER_CODE);
        sPositionHandle = GLES20.glGetAttribLocation(sProgramHandle, "a_position");
        sColorHandle = GLES20.glGetUniformLocation(sProgramHandle, "u_color");
        sMVPMatrixHandle = GLES20.glGetUniformLocation(sProgramHandle, "u_mvpMatrix");
    }

    public void setColor(float r, float g, float b) {
        mColor[0] = r;
        mColor[1] = g;
        mColor[2] = b;
        mColor[3] = 1.0f;
    }

    public float[] getColor() {
        return mColor;
    }

    public void draw() {
        GLES20.glUseProgram(sProgramHandle);
        GLES20.glEnableVertexAttribArray(sPositionHandle);
        GLES20.glVertexAttribPointer(sPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE, sVertexBuffer);

        float[] mvp = sTempMVP;
        Matrix.multiplyMM(mvp, 0, GameSurfaceRenderer.mProjectionMatrix, 0, mModelView, 0);
        GLES20.glUniformMatrix4fv(sMVPMatrixHandle, 1, false, mvp, 0);

        GLES20.glUniform4fv(sColorHandle, 1, mColor, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, VERTEX_COUNT);

        GLES20.glDisableVertexAttribArray(sPositionHandle);
        GLES20.glUseProgram(0);
    }
}
