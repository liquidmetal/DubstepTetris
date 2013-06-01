package in.liquidmetal.dubsteptetris;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

/**
 * Created by utkarsh on 30/5/13.
 * A class to encapsulate all functionality needed for rendering text
 * onto an OpenGL surface
 */
public class GLText extends TexturedAlignedRect {
    private String text;
    private int fontSize, shadowRadius, shadowOffset;
    private int textureID = -1;

    private final static int TEXTURE_WIDTH = 512;
    private final static int TEXTURE_HEIGHT = 512;

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

    public GLText(String text, int fontSize, int shadowRadius, int shadowOffset) {
        setText(text, fontSize, shadowRadius, shadowOffset);
    }

    public void setText(String text, int fontSize, int shadowRadius, int shadowOffset) {
        this.fontSize = fontSize;
        this.shadowRadius = shadowRadius;
        this.shadowOffset = shadowOffset;

        setText(text);
    }

    public void setText(String text) {
        this.text = text;
        updateTexture();
    }

    public Bitmap createTextureBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(TEXTURE_WIDTH, TEXTURE_HEIGHT, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        //bitmap.eraseColor(0x00000000);
        bitmap.eraseColor(0x00000000);

        Paint textPaint = new Paint();
        Typeface typeFace = Typeface.defaultFromStyle(Typeface.BOLD);
        textPaint.setTypeface(typeFace);
        textPaint.setTextSize(fontSize);
        textPaint.setAntiAlias(true);

        int startx = 0;
        int starty = 0;
        int lineHeight = 0;
        textPaint.setColor(0xffff0000);
        textPaint.setShadowLayer(shadowRadius, shadowOffset, shadowOffset, 0xffff0000);
        canvas.drawText(text, 50, 500, textPaint);

        Rect boundsRect = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), boundsRect);
        boundsRect.bottom += shadowRadius + shadowOffset;
        boundsRect.right += shadowRadius + shadowOffset;

        if(boundsRect.width() > TEXTURE_WIDTH || boundsRect.height() > TEXTURE_HEIGHT) {
            Log.w("info",  "HEY: The text " + text + " is too long for the rectangle " + boundsRect);
        }


        boundsRect.offsetTo(startx, starty);
        lineHeight = Math.max(lineHeight, boundsRect.height() + 1);
        startx += boundsRect.width() + 1;

        return bitmap;
    }

    public void updateTexture() {
        // Used to redraw the texture
        Bitmap bitmap = createTextureBitmap();

        int[] handles = new int[1];
        GLES20.glGenTextures(1, handles, 0);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, handles[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

        setTexture(handles[0], TEXTURE_WIDTH, TEXTURE_HEIGHT);

        bitmap.recycle();
    }

    public void generateTexture() {

    }
}
