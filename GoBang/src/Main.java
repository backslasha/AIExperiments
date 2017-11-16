import ai.AI;
import ai.Referee;
import widget.GoBangBoard;

import javax.swing.*;


public class Main {
    public static void main(String[] args) {
        GoBangBoard goBangBoard = new GoBangBoard();
        AI ai = new AI(goBangBoard);
        Referee referee = new Referee(goBangBoard);
        goBangBoard.setOnDropListener((i, j, color, dropByAI) -> {
            if (referee.gameOver(i, j)) {
                System.out.println((color == 1 ? "WHITE" : "BLACK") + " Won!");
                goBangBoard.setGameGover(true);
                return;
            }
            if (!dropByAI) {
                ai.drop();
            }
        });


//        goBangBoard.setOnRightClickListener((i, j) -> {
//            System.out.println("evaluate: "+ai.evaluateWhiteScores(new Point(i, j)));
//        });


        JFrame jFrame = new JFrame();
        jFrame.setLocation(360, 360);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setContentPane(goBangBoard);
        jFrame.setResizable(false);
        jFrame.pack();
        jFrame.setVisible(true);

    }

}

