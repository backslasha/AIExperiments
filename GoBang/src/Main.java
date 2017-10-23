import javax.swing.*;


public class Main {
    public static void main(String[] args) {
        GoBangBoard goBangBoard = new GoBangBoard();
        AI ai = new AI(goBangBoard);
        Referee referee = new Referee(goBangBoard);
        goBangBoard.setOnUserDropListener((i,j)->{
            if (referee.gameOver(i, j)) {
                System.out.println("User Won!");
                return;
            }
            ai.drop();
            if (referee.gameOver(i, j)) {
                System.out.println("User Lost!");
            }
        });


        JFrame jFrame = new JFrame();
        jFrame.setLocation(360, 360);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setContentPane(goBangBoard);
        jFrame.setResizable(false);
        jFrame.pack();
        jFrame.setVisible(true);




    }
}

