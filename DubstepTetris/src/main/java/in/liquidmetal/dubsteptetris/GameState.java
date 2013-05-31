package in.liquidmetal.dubsteptetris;

import java.util.Date;

/**
 * Created by utkarsh on 26/5/13.
 */
public class GameState {
    static final float ARENA_WIDTH = 720.0f;
    static final float ARENA_HEIGHT = 1280.0f;

    static final float BOARD_LEFT = 100;
    static final float BOARD_WIDTH = 500;
    static final float BOARD_HEIGHT = 1000;
    static final float BOARD_BOTTOM = 100;

    static final int TETRAMINO_EMPTY = -1;
    static final int TETRAMINO_I = 0;
    static final int TETRAMINO_L = 1;
    static final int TETRAMINO_J = 2;
    static final int TETRAMINO_O = 3;
    static final int TETRAMINO_T = 4;
    static final int TETRAMINO_S = 5;
    static final int TETRAMINO_Z = 6;
    static final int TETRAMINO_TOTAL = 7;

    static final int numCellsX = 10;
    static final int numCellsY = 20;

    static final int hiddenRowFirst = 21;
    static final int hiddenRowSecond = 20;

    private long lastRenderTime;
    private long lastDropTime;

    private long dropRate = 500;          // Number of miliseconds to pass before dropping one level
    private int currentTetramino;
    private int nextTetramino;
    private int currentTetraminoY;

    private int[][] board_representation = new int[numCellsY+2][numCellsX];
    private AlignedRect[][] board = new AlignedRect[numCellsY][numCellsX];
    static float[][] tetraminoColors = new float[TETRAMINO_TOTAL][3];



    public void initializeBoard() {
        initializeColors();
        float cellSize = BOARD_HEIGHT / numCellsY;
        for(int y=0;y<numCellsY;y++)
            for(int x=0;x<numCellsX;x++) {
                board[y][x] = new AlignedRect();
                board[y][x].setColor(0,0,0);
                board[y][x].setPosition(BOARD_LEFT + x*cellSize - cellSize/2, BOARD_BOTTOM+y*cellSize - cellSize/2);
                board[y][x].setScale(cellSize,cellSize);
                board_representation[y][x] = TETRAMINO_EMPTY;
            }

        for(int y=numCellsY;y<numCellsY+2;y++)
            for(int x=0;x<numCellsX;x++) {
                board_representation[y][x] = TETRAMINO_EMPTY;
            }

        currentTetramino = 3;
        currentTetraminoY = 22;
        nextTetramino = (int)Math.floor(Math.random()*7);

        createNewTetramino();
    }

    private void spawnTetramino() {
        switch(currentTetramino) {
            case TETRAMINO_I:
                board_representation[hiddenRowSecond][3] = TETRAMINO_I + TETRAMINO_TOTAL;
                board_representation[hiddenRowSecond][4] = TETRAMINO_I + TETRAMINO_TOTAL;
                board_representation[hiddenRowSecond][5] = TETRAMINO_I + TETRAMINO_TOTAL;
                board_representation[hiddenRowSecond][6] = TETRAMINO_I + TETRAMINO_TOTAL;
                break;

            case TETRAMINO_J:
                board_representation[hiddenRowSecond][3] = TETRAMINO_J + TETRAMINO_TOTAL;
                board_representation[hiddenRowSecond][4] = TETRAMINO_J + TETRAMINO_TOTAL;
                board_representation[hiddenRowSecond][5] = TETRAMINO_J + TETRAMINO_TOTAL;
                board_representation[hiddenRowFirst][3] = TETRAMINO_J + TETRAMINO_TOTAL;
                break;

            case TETRAMINO_L:
                board_representation[hiddenRowSecond][3] = TETRAMINO_L + TETRAMINO_TOTAL;
                board_representation[hiddenRowSecond][4] = TETRAMINO_L + TETRAMINO_TOTAL;
                board_representation[hiddenRowSecond][5] = TETRAMINO_L + TETRAMINO_TOTAL;
                board_representation[hiddenRowFirst][5] = TETRAMINO_L + TETRAMINO_TOTAL;
                break;

            case TETRAMINO_O:
                board_representation[hiddenRowFirst][5] = TETRAMINO_O + TETRAMINO_TOTAL;
                board_representation[hiddenRowFirst][6] = TETRAMINO_O + TETRAMINO_TOTAL;
                board_representation[hiddenRowSecond][5] = TETRAMINO_O + TETRAMINO_TOTAL;
                board_representation[hiddenRowSecond][6] = TETRAMINO_O + TETRAMINO_TOTAL;
                break;

            case TETRAMINO_S:
                board_representation[hiddenRowFirst][5] = TETRAMINO_S + TETRAMINO_TOTAL;
                board_representation[hiddenRowFirst][4] = TETRAMINO_S + TETRAMINO_TOTAL;
                board_representation[hiddenRowSecond][4] = TETRAMINO_S + TETRAMINO_TOTAL;
                board_representation[hiddenRowSecond][3] = TETRAMINO_S + TETRAMINO_TOTAL;
                break;

            case TETRAMINO_Z:
                board_representation[hiddenRowFirst][3] = TETRAMINO_Z + TETRAMINO_TOTAL;
                board_representation[hiddenRowFirst][4] = TETRAMINO_Z + TETRAMINO_TOTAL;
                board_representation[hiddenRowSecond][4] = TETRAMINO_Z + TETRAMINO_TOTAL;
                board_representation[hiddenRowSecond][5] = TETRAMINO_Z + TETRAMINO_TOTAL;
                break;

            case TETRAMINO_T:
                board_representation[hiddenRowFirst][4] = TETRAMINO_T + TETRAMINO_TOTAL;
                board_representation[hiddenRowSecond][3] = TETRAMINO_T + TETRAMINO_TOTAL;
                board_representation[hiddenRowSecond][4] = TETRAMINO_T + TETRAMINO_TOTAL;
                board_representation[hiddenRowSecond][5] = TETRAMINO_T + TETRAMINO_TOTAL;
                break;
        }
    }

    public void createNewTetramino() {
        currentTetramino = nextTetramino;
        nextTetramino = (int)Math.floor(Math.random()*7);

        spawnTetramino();
    }

    public void drawBoard() {
        for(int y=0;y<numCellsY;y++)
            for(int x=0;x<numCellsX;x++)
                board[y][x].draw();
    }

    public void calculateNextFrame() {
        long timeNow = new Date().getTime();

        if(timeNow - lastDropTime > dropRate) {
            dropTetramino();
        }
    }

    private void dropTetramino() {
        boolean canDrop = true;
        for(int y=1;y<numCellsY+2;y++)
            for(int x=0;x<numCellsX;x++) {
                if(board_representation[y][x] >= TETRAMINO_TOTAL) {
                    if(board_representation[y-1][x] >= 0 && board_representation[y-1][x]<TETRAMINO_TOTAL)
                        canDrop = false;
                }
            }

        if(canDrop) {
            boolean didFirstRowChanges = false;
            for(int y=1;y<numCellsY+2;y++)
                for(int x=0;x<numCellsX;x++) {
                    if(board_representation[y][x] >= TETRAMINO_TOTAL) {
                        board_representation[y-1][x] = board_representation[y][x];
                        board_representation[y][x] = TETRAMINO_EMPTY;
                        if(y==1)
                            didFirstRowChanges = true;
                    }
                }

            if(didFirstRowChanges) canDrop = false;
        }

        if(!canDrop) {
            for(int y=0;y<numCellsY;y++)
                for(int x=0;x<numCellsX;x++)
                    if(board_representation[y][x]>=TETRAMINO_TOTAL) board_representation[y][x] -= TETRAMINO_TOTAL;

            createNewTetramino();
        }

        for(int y=0;y<numCellsY;y++)
            for(int x=0;x<numCellsX;x++) {
                if(board_representation[y][x] == TETRAMINO_EMPTY) {
                    board[y][x].setColor(0, 0, 0);
                } else {
                    int colorIndex = board_representation[y][x];
                    if(colorIndex>=TETRAMINO_TOTAL) colorIndex -= TETRAMINO_TOTAL;

                    float[] color = tetraminoColors[colorIndex];
                    board[y][x].setColor(color[0], color[1], color[2]);
                }
            }

        lastDropTime = new Date().getTime();
    }

    private void initializeColors() {
        // I
        tetraminoColors[TETRAMINO_I][0] = 0.0f;
        tetraminoColors[TETRAMINO_I][1] = 1.0f;
        tetraminoColors[TETRAMINO_I][2] = 1.0f;

        // L
        tetraminoColors[TETRAMINO_L][0] = 1.0f;
        tetraminoColors[TETRAMINO_L][1] = 0.5f;
        tetraminoColors[TETRAMINO_L][2] = 0.0f;

        // J
        tetraminoColors[TETRAMINO_J][0] = 0.0f;
        tetraminoColors[TETRAMINO_J][1] = 0.0f;
        tetraminoColors[TETRAMINO_J][2] = 1.0f;

        // O
        tetraminoColors[TETRAMINO_O][0] = 1.0f;
        tetraminoColors[TETRAMINO_O][1] = 1.0f;
        tetraminoColors[TETRAMINO_O][2] = 0.0f;

        // T
        tetraminoColors[TETRAMINO_T][0] = 0.0f;
        tetraminoColors[TETRAMINO_T][1] = 0.5f;
        tetraminoColors[TETRAMINO_T][2] = 0.5f;

        // S
        tetraminoColors[TETRAMINO_S][0] = 0.0f;
        tetraminoColors[TETRAMINO_S][1] = 1.0f;
        tetraminoColors[TETRAMINO_S][2] = 0.0f;

        // Z
        tetraminoColors[TETRAMINO_Z][0] = 1.0f;
        tetraminoColors[TETRAMINO_Z][1] = 0.0f;
        tetraminoColors[TETRAMINO_Z][2] = 0.0f;
    }

    public void updateRenderTime() {
        lastRenderTime = new Date().getTime();
    }

    public int getTetraminoX() {
        // Identifies the currently active tetramino's average
        // X position

        int avgX = 0;
        for(int y=0;y<numCellsY+2;y++)
            for(int x=0;x<numCellsX;x++) {
                if(board_representation[y][x] >= TETRAMINO_TOTAL) {
                    avgX += x;
                }
            }

        avgX /= 4.0f;
        return avgX;
    }

    public int translatePixelToCell(int x) {
        if(x<BOARD_LEFT)
            return 0;

        if(x>BOARD_LEFT+BOARD_WIDTH)
            return numCellsY-1;

        return (int)((x-BOARD_LEFT)/numCellsY);
    }

    public void signalDown(int posX, int posY) {

        
    }

    public void signalTranslate(int positionX) {
        int tetraminoX = getTetraminoX();
        int pixelX = translatePixelToCell(positionX);

        if(pixelX<tetraminoX) {
            //int numShifts = (int)(distanceX/-20f);

            // Move left

            //for(int i=0;i<numShifts;i++) {
                boolean canShiftLeft = true;
                for(int y=0;y<numCellsY+2;y++)
                    for(int x=0;x<numCellsX;x++) {
                        if(board_representation[y][x]>=TETRAMINO_TOTAL) {
                            if(x==0) {
                                canShiftLeft = false;
                                break;
                            }

                            int nextCell = board_representation[y][x-1];
                            if(nextCell>TETRAMINO_EMPTY && nextCell<TETRAMINO_TOTAL) {
                                canShiftLeft = false;
                                break;
                            }
                        }
                    }

                if(!canShiftLeft)
                    return;

                for(int y=0;y<numCellsY+2;y++) {
                    for(int x=1;x<numCellsX;x++) {
                        if(board_representation[y][x]>=TETRAMINO_TOTAL) {
                            board_representation[y][x-1] = board_representation[y][x];
                            board_representation[y][x] = TETRAMINO_EMPTY;
                        }
                    }
                }
            //}
        } else if(pixelX>tetraminoX) {
            //int numShifts = (int)(distanceX/20f);

            //for(int i=0;i<numShifts;i++) {

                boolean canShiftRight = true;
                for(int y=0;y<numCellsY+2;y++)
                    for(int x=0;x<numCellsX;x++) {
                        if(board_representation[y][x]>=TETRAMINO_TOTAL) {
                            if(x==numCellsX-1) {
                                canShiftRight = false;
                                break;
                            }

                            int nextCell = board_representation[y][x+1];
                            if(nextCell>TETRAMINO_EMPTY && nextCell<TETRAMINO_TOTAL) {
                                canShiftRight = false;
                                break;
                            }
                        }
                    }

                if(!canShiftRight)
                    return;

                for(int y=0;y<numCellsY+2;y++) {
                    for(int x=numCellsX-2;x>=0;x--) {
                        if(board_representation[y][x]>=TETRAMINO_TOTAL) {
                            board_representation[y][x+1] = board_representation[y][x];
                            board_representation[y][x] = TETRAMINO_EMPTY;
                        }
                    }
                }
            //}
        }
    }
}
