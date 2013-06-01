package in.liquidmetal.dubsteptetris;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.ConditionVariable;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by utkarsh on 26/5/13.
 */
public class GameSurfaceRenderer implements GLSurfaceView.Renderer, GestureDetector.OnGestureListener {

    // Member variables used to keep track of the viewport
    static float mProjectionMatrix[] = new float[16];       // TODO Why was it 'final' in the original code?
    private int mViewportWidth, mViewportHeight;
    private int mViewportXoff, mViewportYoff;

    private GestureDetectorCompat mDetector;

    private GameSurfaceView mSurfaceView;
    private GameState mGameState;
    private AlignedRect rect;
    private float oldX;

    private GLText text;

    public GameSurfaceRenderer(GameState gameState, GameSurfaceView gameSurfaceView) {
        mGameState = gameState;
        mSurfaceView = gameSurfaceView;

        mDetector = new GestureDetectorCompat(gameSurfaceView.getContext(), this);



    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        AlignedRect.createProgram();
        GLText.createProgram();

        text = new GLText("ABCD", 300, 0, 0);
        text.setPosition(300, 300);
        text.setScale(512, 512);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glDisable(GLES20.GL_CULL_FACE);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        float arenaRatio = GameState.ARENA_HEIGHT/ GameState.ARENA_WIDTH;
        int x, y, viewWidth, viewHeight;

        // Limited by width?
        if(height > (int)(width*arenaRatio)) {
            viewWidth = width;
            viewHeight = (int)(width*arenaRatio);
            x = 0;
            y = (height - viewHeight) / 2;
        } else {
            viewHeight = height;
            viewWidth = (int)(height / arenaRatio);
            x = (width - viewWidth) / 2;
            y = 0;
        }

        // Setup the OpenGL viewport based on these calculations
        GLES20.glViewport(x, y, viewWidth, viewHeight);

        mViewportHeight = viewHeight;
        mViewportWidth = viewWidth;
        mViewportXoff = x;
        mViewportYoff = y;

        // Now, setup an orthographic projection
        Matrix.orthoM(mProjectionMatrix, 0, 0, GameState.ARENA_WIDTH, 0, GameState.ARENA_HEIGHT, -1.0f, 1.0f);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        //mGameState.calculateNextFrame();

        mGameState.clearScreen();

        //mGameState.drawBoard();
        //mGameState.drawNextPiece();
        //mGameState.updateRenderTime();
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        text.draw();
        GLES20.glDisable(GLES20.GL_BLEND);
    }

    public void touchEvent(MotionEvent e) {
        boolean used = mDetector.onTouchEvent(e);
        if(used)
            return;

        switch(e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mGameState.signalMotion((int)e.getX(), (int)e.getY());
                break;

            case MotionEvent.ACTION_DOWN:
                mGameState.signalDown((int)e.getX(), (int)e.getY());
                break;
        }

    }

    public void onViewPause(ConditionVariable syncObj) {
        // Save game state

        syncObj.open();
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        mGameState.handleRotation((int)motionEvent.getX());
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float distanceX, float distanceY) {
        //mGameState.signalTranslate((int)(motionEvent2.getX() - motionEvent.getX()));
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
        return false;
    }
}
