import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

public class GoBangBoard extends JPanel {
    public static final int N = 15;
    public static final int BLACK_ONE = 9;
    public static final int WHITE_ONE = 1;
    public static final int EMPTY = 0;
    private ImageIcon blackPiece, whitePiece; //位图
    private int[][] mPieces;
    private int pieceSize = 30;
    private int piecesCount = 0;
    private boolean BlackSTurn = false;
    private Point lastDrop = new Point(-1, -1);
    private OnUserDropListener onUserDropListener;

    public interface OnUserDropListener {
        void OnUserDrop(int i, int j);
    }

    GoBangBoard() {
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                int i = x / pieceSize;
                int j = y / pieceSize;
                drop(i, j);
                if (onUserDropListener != null) {
                    onUserDropListener.OnUserDrop(i, j);
                }
            }
        };
        blackPiece = new ImageIcon(getClass().getResource("/images/blackPiece.jpg"));
        whitePiece = new ImageIcon(getClass().getResource("/images/whitePiece.jpg"));
        mPieces = new int[15][15];
        setPreferredSize(new Dimension(pieceSize * N, pieceSize * N));
        addMouseListener(mouseAdapter);
    }

    public void drop(int i, int j) {
        if (mPieces[i][j] != EMPTY) {
            return;
        }
        lastDrop.x = i;
        lastDrop.y = j;
        piecesCount++;
        mPieces[i][j] = BlackSTurn ? BLACK_ONE : WHITE_ONE;
        BlackSTurn = !BlackSTurn;
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
                if (mPieces[i][j] == BLACK_ONE) {
                    blackPiece.paintIcon(this, g, pieceSize * i, pieceSize * j);
                } else if (mPieces[i][j] == WHITE_ONE) {
                    whitePiece.paintIcon(this, g, pieceSize * i, pieceSize * j);
                }
            }
    }

    public int[][] getPieces() {
        return mPieces;
    }

    public void setOnUserDropListener(OnUserDropListener onUserDropListener) {
        this.onUserDropListener = onUserDropListener;
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
