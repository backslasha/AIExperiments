import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TicTacToe {
    static final int TICK = 666;
    static final int CROSS = 233;
    static final int EMPTY = 0;

    static final String TICK_NAME = "萌妹稽";
    static final String CROSS_NAME = "皮皮稽";

    private boolean tick = false;
    private int[][] mPieces = new int[3][3];
    private boolean gameOver = false;
    private AI ai;
    private OnGameOverListener onGameOverListener;

    public void setOnGameOverListener(OnGameOverListener onGameOverListener) {
        this.onGameOverListener = onGameOverListener;
    }

    interface OnGameOverListener {
        void onGameOver(int pieceType);
    }

    public boolean playerDrop(int i, int j) {
        boolean success = drop(i, j);
        if (ai != null && !gameOver) {
            aiDrop();
        }
        return success;
    }

    public boolean aiDrop() {
        Point point = ai.decide(mPieces);
        return drop(point.x, point.y);
    }

    private boolean drop(int i, int j) {
        if (mPieces[i][j] != EMPTY || gameOver) {
            return false;
        }
        // 修改棋盘数据，真正的下棋动作
        mPieces[i][j] = tick ? TICK : CROSS;

        // 若 tick 方三子连续
        if (3 == Evaluator.maxSeries(TICK, mPieces)) {
            gameOver = true;
            if (onGameOverListener != null) {
                onGameOverListener.onGameOver(TICK);
            }
            System.out.println(TICK_NAME + "胜!");
        }// 若 wrong 方三子连续
        else if (3 == Evaluator.maxSeries(CROSS, mPieces)) {
            gameOver = true;
            if (onGameOverListener != null) {
                onGameOverListener.onGameOver(CROSS);
            }
            System.out.println(CROSS_NAME + "胜!");
        } // 若棋盘已满
        else if (9 == Evaluator.countPieces(mPieces)) {
            gameOver = true;
            if (onGameOverListener != null) {
                onGameOverListener.onGameOver(EMPTY);
            }
            System.out.println("平局!");
        } // 否则游戏继续，若有 AI，则 AI 下棋
        else {
            tick = !tick;
        }

        return true;
    }

    int[][] getPieces() {
        return mPieces;
    }

    void setAI(AI ai) {
        this.ai = ai;
    }

    void reset() {
        gameOver = false;
        tick = false;
    }

    void setAIFirst(boolean AIFirst) {
        tick = true;
    }
}
