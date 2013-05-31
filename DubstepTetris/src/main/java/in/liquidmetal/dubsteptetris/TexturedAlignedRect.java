package in.liquidmetal.dubsteptetris;

import android.opengl.GLES20;

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
                    "attribute vec2 a_texcoord;" +
                    "varying vec2 a_texCoord;" +
                    "void main() {" +
                    "    gl_Position = u_mvpMatrix * a_position;" +
                    "    v_texCoord = a_texCoord;" +
                    "}";

    private static final String FRAGMENT_SHADER_CODE =
            "precision mediump float;" +
                    "uniform sampler2D u_texture;" +
                    "varying vec2 v_texCoord;" +
                    "void main() {" +
                    "    glFragColor = texture2D(u_texture, v_texCoord);" +
                    "}";

    private static FloatBuffer sVertexBuffer = getVertexArray();

    private static int sProgramHandle = -1;
    private static int sTexCoordHandle = -1;
    private static int sPositionHandle = -1;
    private static int sMVPMatrixHandle = -1;

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
        mTexBuffer = fb;
    }

    public static void createProgram() {
        sProgramHandle = Util.createProgram(VERTEX_SHADER_CODE, FRAGMENT_SHADER_CODE);

        sPositionHandle = GLES20.glGetAttribLocation(sProgramHandle, "a_position");
        sTexCoordHandle = GLES20.glGetAttribLocation(sProgramHandle, "a_texCoord");
        sMVPMatrixHandle = GLES20.glGetUniformLocation(sProgramHandle, "u_mvpMatrix");
        int textureUniformHandle = GLES20.glGetUniformLocation(sProgramHandle, "u_texture");

        GLES20.glUseProgram(sProgramHandle);
        GLES20.glUniform1i(textureUniformHandle, 0);
        GLES20.glUseProgram(0);
    }

    public void setTexture(ByteBuffer buf, int width, int height, int format) {

    }
}
