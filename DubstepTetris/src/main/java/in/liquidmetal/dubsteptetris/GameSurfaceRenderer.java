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

    public GameSurfaceRenderer(GameState gameState, GameSurfaceView gameSurfaceView) {
        mGameState = gameState;
        mSurfaceView = gameSurfaceView;

        mDetector = new GestureDetectorCompat(gameSurfaceView.getContext(), this);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        AlignedRect.createProgram();

        GLES20.glClearColor(0.0f, 0.25f, 0.0f, 1.0f);
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
        mGameState.calculateNextFrame();

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);


        mGameState.drawBoard();
        mGameState.updateRenderTime();
    }

    public void touchEvent(MotionEvent e) {
        mDetector.onTouchEvent(e);

        switch(e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mGameState.signalTranslate((int)e.getX());
                break;

            case MotionEvent.ACTION_DOWN:
                mGameState.signalDown(e.getX(), e.getY());
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
        return false;
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
