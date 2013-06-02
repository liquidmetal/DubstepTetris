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
    private int textColor;

    private int iTextureWidth;
    private int iTextureHeight;

    public GLText(int sizeX, int sizeY, String text, int fontSize, int shadowRadius, int shadowOffset) {
        //iTextureWidth = sizeX;
        //iTextureHeight = sizeY;
        iTextureWidth = (int)Math.pow(2, Math.ceil(Math.log(sizeX)/Math.log(2)));
        iTextureHeight = (int)Math.pow(2, Math.ceil(Math.log(sizeY)/Math.log(2)));
        textColor = 0xffffff;

        setScale(sizeX, sizeY);
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

    public void setColor(int color) {
        textColor = color;
        updateTexture();
    }

    public Bitmap createTextureBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(iTextureWidth, iTextureHeight, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(0x00000000);

        Canvas canvas = new Canvas(bitmap);

        Paint textPaint = new Paint();
        Typeface typeFace = Typeface.defaultFromStyle(Typeface.BOLD);
        textPaint.setTypeface(typeFace);
        textPaint.setTextSize(fontSize);
        textPaint.setAntiAlias(true);

        int startx = 0;
        int starty = 0;
        int lineHeight = 0;
        textPaint.setColor(0xff000000 | textColor);
        textPaint.setShadowLayer(shadowRadius, shadowOffset, shadowOffset, 0xff000000);



        Rect boundsRect = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), boundsRect);
        canvas.drawText(text, 0-boundsRect.left, 0-boundsRect.top, textPaint);
        boundsRect.bottom += shadowRadius + shadowOffset;
        boundsRect.right += shadowRadius + shadowOffset;

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

        setTexture(handles[0], iTextureWidth, iTextureHeight);

        bitmap.recycle();
    }

    public void generateTexture() {

    }
}
