
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TicTacToeBoard extends JPanel {

    private int N;
    private int pieceSize = 120;
    private ImageIcon bingoIcon, wrongIcon;
    private int[][] mPieces;

    TicTacToeBoard(TicTacToe ticTacToe) {
        setPreferredSize(new Dimension(360, 360));
        bingoIcon = new ImageIcon(getClass().getResource("/images/bingo.png"));
        wrongIcon = new ImageIcon(getClass().getResource("/images/wrong.png"));
        mPieces = ticTacToe.getPieces();
        N = mPieces.length + 1;
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                int i = y / pieceSize;
                int j = x / pieceSize;
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (ticTacToe.playerDrop(i, j)) {
                        repaint();
                    }
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    int score1 = Evaluator.countLines(TicTacToe.TICK, ticTacToe.getPieces());
                    int score2 = Evaluator.countLines(TicTacToe.CROSS, ticTacToe.getPieces());
                    int value = score1 - score2;
                    System.out.println("score1 - score2 = " + score1 + " - " + score2 + " = " + value);
                }
            }

        };
        addMouseListener(mouseAdapter);
    }

    @Override
    public void paint(Graphics g) {
        paintLines(g);
        paintIcons(g);
    }

    private void paintIcons(Graphics g) {
        for (int i = 0; i < mPieces.length; i++) {
            for (int j = 0; j < mPieces.length; j++) {
                if (mPieces[i][j] == TicTacToe.TICK)
                    bingoIcon.paintIcon(this, g, j * pieceSize + 10, i * pieceSize + 10);
                else if (mPieces[i][j] == TicTacToe.CROSS)
                    wrongIcon.paintIcon(this, g, j * pieceSize + 10, i * pieceSize + 10);
            }
        }
    }

    private void paintLines(Graphics g) {
        int startX, startY;
        startX = startY = 0;

        for (int i = 0; i < N; i++) {
            g.drawLine(startX, startY, startX + (pieceSize * 3), startY);
            startY += pieceSize;
        }

        startX = startY = 0;
        for (int j = 0; j < N; j++) {
            g.drawLine(startX, startY, startX, startY + (pieceSize * 3));
            startX += pieceSize;
        }
    }
}
