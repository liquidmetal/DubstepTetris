package in.liquidmetal.dubsteptetris;

import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by utkarsh on 1/6/13.
 */
public class TexturedAlignedRect extends BaseRect {
    private static final String VERTEX_SHADER_CODE =
            "uniform mat4 u_mvpMatrix;" +
            "attribute vec4 a_position;" +
            "attribute vec2 a_texCoord;" +
            "varying vec2 v_texCoord;" +
            "void main() {" +
            "    gl_Position = u_mvpMatrix * a_position;" +
            "    v_texCoord = a_texCoord;" +
            "}";

    private static final String FRAGMENT_SHADER_CODE =
            "precision mediump float;" +
            "uniform sampler2D u_texture;" +
            "varying vec2 v_texCoord;" +
            "void main() {" +
            //"    gl_FragColor = vec4(v_texCoord, 0.0, 1.0);"+  // debug: display UVs
            "    gl_FragColor = texture2D(u_texture, v_texCoord);" +
            "}";

    private static FloatBuffer sVertexBuffer = getVertexArray();

    private static int sProgramHandle = -1;
    private static int sTexCoordHandle = -1;
    private static int sPositionHandle = -1;
    private static int sMVPMatrixHandle = -1;
    private static int sTextureUniformHandle = -1;

    private int mTextureDataHandle = -1;
    private int mTextureWidth = -1;
    private int mTextureHeight = -1;
    private FloatBuffer mTexBuffer;

    private static float[] sTempMVP = new float[16];

    public TexturedAlignedRect() {
        FloatBuffer defaultCoords = getTexArray();

        FloatBuffer fb = ByteBuffer.allocateDirect(VERTEX_COUNT * TEX_VERTEX_STRIDE).order(ByteOrder.nativeOrder()).asFloatBuffer();
        fb.put(defaultCoords);
        defaultCoords.position(0);
        fb.position(0);
        mTexBuffer = fb;
    }

    public static void createProgram() {
        sProgramHandle = Util.createProgram(VERTEX_SHADER_CODE, FRAGMENT_SHADER_CODE);

        sPositionHandle = GLES20.glGetAttribLocation(sProgramHandle, "a_position");
        sTexCoordHandle = GLES20.glGetAttribLocation(sProgramHandle, "a_texCoord");
        sMVPMatrixHandle = GLES20.glGetUniformLocation(sProgramHandle, "u_mvpMatrix");
        sTextureUniformHandle = GLES20.glGetUniformLocation(sProgramHandle, "u_texture");
    }

    public void setTexture(ByteBuffer buf, int width, int height, int format) {
        mTextureDataHandle = Util.createImageTexture(buf, width, height, format);
        mTextureWidth = width;
        mTextureHeight = height;
    }

    public void setTexture(int handle, int width, int height) {
        mTextureDataHandle = handle;
        mTextureWidth = width;
        mTextureHeight = height;
    }

    public void setTexCoords(Rect coords) {
        float left = (float)coords.left / mTextureWidth;
        float right = (float)coords.right / mTextureWidth;
        float top = (float)coords.top / mTextureHeight;
        float bottom = (float)coords.bottom / mTextureHeight;

        FloatBuffer fb = mTexBuffer;
        fb.put(left);
        fb.put(bottom);

        fb.put(right);
        fb.put(bottom);

        fb.put(left);
        fb.put(right);

        fb.put(top);
        fb.put(right);

        fb.position(0);
    }

    public void draw() {
        GLES20.glUseProgram(sProgramHandle);
        GLES20.glUniform1i(sTextureUniformHandle, mTextureDataHandle);

        GLES20.glEnableVertexAttribArray(sPositionHandle);
        GLES20.glVertexAttribPointer(sPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE, sVertexBuffer);

        GLES20.glEnableVertexAttribArray(sTexCoordHandle);
        GLES20.glVertexAttribPointer(sTexCoordHandle, TEX_COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, TEX_VERTEX_STRIDE, mTexBuffer);

        float[] mvp = sTempMVP;
        Matrix.multiplyMM(mvp, 0, GameSurfaceRenderer.mProjectionMatrix, 0, mModelView, 0);

        GLES20.glUniformMatrix4fv(sMVPMatrixHandle, 1, false, mvp, 0);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, VERTEX_COUNT);
        GLES20.glDisableVertexAttribArray(sPositionHandle);
        GLES20.glUseProgram(0);
    }
}
