import javax.swing.*;
import java.awt.event.ActionEvent;

public class Entry {

    public static void main(String[] args) {

        JFrame jFrame = new JFrame();
        TicTacToe ticTacToe = new TicTacToe();
        ticTacToe.setAI(new AI());
        TicTacToeBoard ticTacToeBoard = new TicTacToeBoard(ticTacToe);
        ticTacToe.setOnGameOverListener(pieceType -> {
            ticTacToeBoard.repaint();
            if (pieceType == TicTacToe.TICK) {
                JOptionPane.showMessageDialog(
                        jFrame,
                        TicTacToe.TICK_NAME + "：\n你这索嗨赢一局算我输!ↀωↀ",
                        TicTacToe.TICK_NAME + "胜利!",
                        JOptionPane.INFORMATION_MESSAGE);
            } else if (pieceType == TicTacToe.CROSS) {
                JOptionPane.showMessageDialog(
                        jFrame,
                        TicTacToe.CROSS_NAME + "：\n！！!(ಡωಡ) ",
                        TicTacToe.CROSS_NAME + "胜利!",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(
                        jFrame,
                        TicTacToe.TICK_NAME + "：ↀωↀ" + "\n" + TicTacToe.CROSS_NAME + "：∑(°□°)!!",
                        "平局!",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        jFrame.setLocation(640, 160);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setContentPane(ticTacToeBoard);
        jFrame.setResizable(false);
        jFrame.setTitle("TicTacToe");
        jFrame.setJMenuBar(getJMenuBar(ticTacToe, ticTacToeBoard, jFrame));
        jFrame.setVisible(true);
        jFrame.pack();

    }

    private static JMenuBar getJMenuBar(TicTacToe ticTacToe, TicTacToeBoard ticTacToeBoard, JFrame jFrame) {
        JMenuBar jMenuBar = new JMenuBar();
        JMenu jMenu = new JMenu("顺序");
        JMenuItem menuFirst = new JMenuItem();
        JMenuItem menuSecond = new JMenuItem();
        menuFirst.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuFirst.setText("电脑先手 √");
                menuSecond.setText("电脑后手");
            }
        });
        menuSecond.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuFirst.setText("电脑先手");
                menuSecond.setText("电脑后手 √");
            }
        });
        jMenu.add(menuFirst);
        jMenu.add(menuSecond);
        jMenuBar.add(jMenu);


        jMenu = new JMenu("选项");
        JMenuItem menuReOpen = new JMenuItem();
        menuReOpen.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int[] ints : ticTacToe.getPieces()) {
                    for (int i = 0; i < ints.length; i++) {
                        ints[i] = 0;
                    }
                }
                ticTacToe.reset();
                ticTacToeBoard.repaint();

                if (menuFirst.getText().contains("√")) {
                    ticTacToe.setAIFirst(true);
                    ticTacToe.aiDrop();
                }

            }
        });
        jMenu.add(menuReOpen);
        jMenuBar.add(jMenu);


        jMenu = new JMenu("模式");
        JMenuItem menuAI = new JMenuItem();
        JMenuItem menuNoAI = new JMenuItem();

        menuAI.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ticTacToe.setAI(new AI());
                menuAI.setText("人机 √");
                menuNoAI.setText("人人");
                menuFirst.setEnabled(true);
                menuSecond.setEnabled(true);
            }
        });
        menuNoAI.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ticTacToe.setAI(null);
                menuAI.setText("人机");
                menuNoAI.setText("人人 √");
                menuFirst.setEnabled(false);
                menuSecond.setEnabled(false);
            }
        });

        jMenu.add(menuAI);
        jMenu.add(menuNoAI);
        jMenuBar.add(jMenu);


        menuAI.setText("人机 √");
        menuNoAI.setText("人人");
        menuFirst.setText("电脑先手");
        menuSecond.setText("电脑后手 √");
        menuReOpen.setText("重开");

        return jMenuBar;
    }
}
