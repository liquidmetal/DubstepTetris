package in.liquidmetal.dubsteptetris;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.opengl.GLES20;
import android.opengl.GLUtils;
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
            "uniform float alpha_multiplier;" +
            "void main() {" +
            //"    gl_FragColor = vec4(v_texCoord, 0.0, 1.0);"+  // debug: display UVs
            "    vec4 sampleColor = texture2D(u_texture, v_texCoord);" +
            "    sampleColor.a = sampleColor.a*alpha_multiplier;" +
            "    gl_FragColor = vec4(sampleColor.r, sampleColor.g, sampleColor.b, sampleColor.a);" +
            "}";

    private static FloatBuffer sVertexBuffer = getVertexArray();

    private static int sProgramHandle = -1;
    private static int sTexCoordHandle = -1;
    private static int sPositionHandle = -1;
    private static int sAlphaMultiplierHandle = -1;
    private static int sMVPMatrixHandle = -1;
    private static int sTextureUniformHandle = -1;

    private int mTextureDataHandle = -1;
    private int mTextureWidth = -1;
    private int mTextureHeight = -1;
    private FloatBuffer mTexBuffer;
    private float alphaMultiplier = 1.0f;

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

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        sPositionHandle = GLES20.glGetAttribLocation(sProgramHandle, "a_position");
        sTexCoordHandle = GLES20.glGetAttribLocation(sProgramHandle, "a_texCoord");

        sAlphaMultiplierHandle = GLES20.glGetUniformLocation(sProgramHandle, "alpha_multiplier");
        sMVPMatrixHandle = GLES20.glGetUniformLocation(sProgramHandle, "u_mvpMatrix");
        sTextureUniformHandle = GLES20.glGetUniformLocation(sProgramHandle, "u_texture");
    }

    public void setTexture(ByteBuffer buf, int width, int height, int format) {
        mTextureDataHandle = Util.createImageTexture(buf, width, height, format);
        mTextureWidth = width;
        mTextureHeight = height;
    }

    public void setTexture(Bitmap bitmap, int iTextureWidth, int iTextureHeight) {
        int[] handles = new int[1];
        GLES20.glGenTextures(1, handles, 0);

        int iTextureWidthNew = (int)Math.pow(2, Math.ceil(Math.log(iTextureWidth)/Math.log(2)));
        int iTextureHeightNew = (int)Math.pow(2, Math.ceil(Math.log(iTextureHeight)/Math.log(2)));



        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, handles[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);

        if(iTextureHeight != iTextureHeightNew || iTextureWidth != iTextureWidthNew) {
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, iTextureWidthNew, iTextureWidthNew, false);
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, scaled, 0);
            scaled.recycle();

            setTexture(handles[0], iTextureWidthNew, iTextureHeightNew);
        } else {
            GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

            setTexture(handles[0], iTextureWidth, iTextureHeight);
        }
    }

    public void setTexture(int handle, int width, int height) {
        mTextureDataHandle = handle;
        mTextureWidth = width;
        mTextureHeight = height;
    }

    public void setAlphaMultiplier(float multiplier) {
        alphaMultiplier = multiplier;
    }

    public float getAlphaMultiplier() {
        return alphaMultiplier;
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

        GLES20.glEnableVertexAttribArray(sPositionHandle);
        GLES20.glVertexAttribPointer(sPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, VERTEX_STRIDE, sVertexBuffer);

        GLES20.glEnableVertexAttribArray(sTexCoordHandle);
        GLES20.glVertexAttribPointer(sTexCoordHandle, TEX_COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, TEX_VERTEX_STRIDE, mTexBuffer);

        float[] mvp = sTempMVP;
        Matrix.multiplyMM(mvp, 0, GameSurfaceRenderer.mProjectionMatrix, 0, mModelView, 0);
        GLES20.glUniformMatrix4fv(sMVPMatrixHandle, 1, false, mvp, 0);

        // Pass the alpha multiplier to the fragment shader
        GLES20.glUniform1f(sAlphaMultiplierHandle, alphaMultiplier);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle);
        GLES20.glUniform1i(sTextureUniformHandle, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, VERTEX_COUNT);

        // Reset everything
        GLES20.glDisableVertexAttribArray(sPositionHandle);
        GLES20.glDisableVertexAttribArray(sTexCoordHandle);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        GLES20.glUseProgram(0);
    }
}
