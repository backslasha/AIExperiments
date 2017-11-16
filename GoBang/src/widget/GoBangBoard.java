package widget;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GoBangBoard extends JPanel {
    public static final int N = 15;
    public static final int BLACK = 9;
    public static final int WHITE = 1;
    public static final int EMPTY = 0;
    private ImageIcon blackPiece, whitePiece; //位图
    private int[][] mPieces;
    private int pieceSize = 30;
    private int piecesCount = 0;
    private boolean BlackSTurn = false;
    private Point lastDrop = new Point(-1, -1);
    private OnDropListener onDropListener;

    private OnHoverListener onHoverListener;

    public void setGameGover(boolean gameGover) {
        this.gameGover = gameGover;
    }

    public void setOnRightClickListener(OnHoverListener onHoverListener) {
        this.onHoverListener = onHoverListener;
    }

    private boolean gameGover = false;

    public interface OnDropListener {
        void onDrop(int i, int j, int color, boolean dropByAI);
    }

    public interface OnHoverListener {
        void onMouseHovering(int i, int j);
    }

    public GoBangBoard() {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                int i = y / pieceSize;
                int j = x / pieceSize;

                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (onHoverListener != null) {
                        onHoverListener.onMouseHovering(j, i);
                    }
                } else {
                    if (mPieces[i][j] != EMPTY) {
                        return;
                    }
                    if (!gameGover) {
                        drop(i, j, false);
                    }
                }

            }

        };
        blackPiece = new ImageIcon(getClass().getResource("/images/blackPiece.jpg"));
        whitePiece = new ImageIcon(getClass().getResource("/images/whitePiece.jpg"));
        mPieces = new int[15][15];
        setPreferredSize(new Dimension(pieceSize * N, pieceSize * N));
        addMouseListener(mouseAdapter);
    }

    public void drop(int i, int j, boolean dropByAI) {
        if (mPieces[i][j] != EMPTY) {
            return;
        }
        lastDrop.x = i;
        lastDrop.y = j;
        piecesCount++;
        mPieces[i][j] = BlackSTurn ? BLACK : WHITE;
        BlackSTurn = !BlackSTurn;
        if (onDropListener != null) {
            onDropListener.onDrop(i, j, BlackSTurn ? WHITE : BLACK, dropByAI);
        }
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        paintBorder(g);
        paintLines(g);
        paintPieces(g);
    }

    private void paintLines(Graphics g) {
        int startX, startY;
        startX = startY = pieceSize / 2;

        for (int i = 0; i < N; i++) {
            g.drawLine(startX, startY, startX + (pieceSize * 14), startY);
            startY += pieceSize;
        }

        startX = startY = pieceSize / 2;
        for (int j = 0; j < N; j++) {
            g.drawLine(startX, startY, startX, startY + (pieceSize * 14));
            startX += pieceSize;
        }
    }

    private void paintPieces(Graphics g) { //画棋子
        int i, j;
        for (i = 0; i < N; i++)
            for (j = 0; j < N; j++) {
                if (mPieces[i][j] == BLACK) {
                    blackPiece.paintIcon(this, g, pieceSize * j, pieceSize * i);
                } else if (mPieces[i][j] == WHITE) {
                    whitePiece.paintIcon(this, g, pieceSize * j, pieceSize * i);
                }
            }
    }

    public int[][] getPieces() {
        return mPieces;
    }

    public void setOnDropListener(OnDropListener onDropListener) {
        this.onDropListener = onDropListener;
    }

    public void updatePieces(int[][] pieces) {
        mPieces = pieces;
        repaint();
    }

    public int getPiecesCount() {
        return piecesCount;
    }

    public Point getLastDrop() {
        return lastDrop;
    }
}
