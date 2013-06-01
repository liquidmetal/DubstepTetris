package in.liquidmetal.dubsteptetris;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.ConditionVariable;
import android.view.MotionEvent;

/**
 * Created by utkarsh on 26/5/13.
 */
public class GameSurfaceView extends GLSurfaceView {
    private GameSurfaceRenderer mRenderer;
    private GameState mGameState;
    private final ConditionVariable syncObj = new ConditionVariable();

    // Constructor for the game view.
    public GameSurfaceView(Context context, GameState gameState) {
        super(context);

        // Request for OpenGL ES 2.0
        setEGLContextClientVersion(2);

        mGameState = gameState;
        mGameState.initializeBoard();

        mRenderer = new GameSurfaceRenderer(mGameState, this);
        mGameState.setRenderer(mRenderer);
        setRenderer(mRenderer);
    }

    @Override
    public void onPause() {
        syncObj.close();
        queueEvent(new Runnable() {
            @Override public void run() {
                mRenderer.onViewPause(syncObj);
            }
        });

        // TODO What's the use of this syncObj?
        syncObj.block();
    }

    @Override
    // This is where we forward touch events to the renderer thread. We could
    // call methods on the other thread directly, but since those variables are
    // owned by a separate thread, we need to use QueueEvent
    public boolean onTouchEvent(final MotionEvent e) {
        queueEvent(new Runnable() {
            @Override public void run() {
            mRenderer.touchEvent(e);
            }
        });
        return true;
    }
}
