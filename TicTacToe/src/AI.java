import java.awt.*;

/**
 * AI : TicTacToe.TICK
 */
class AI {
    // 搜索深度，为 2 时已经保证先后手均不会输
    private int depth = 2;

    /**
     * 决定最终下的棋子位置
     * @param pieces 棋盘
     */
    Point decide(int[][] pieces) {
        Node n = findMax(pieces, 1);
        return new Point(n.i, n.j);
    }

    /**
     * AI 视角，估算棋盘的分数
     * @param pieces 棋盘数组
     */
    private int evaluate(int[][] pieces) {
        if (Evaluator.maxSeries(TicTacToe.TICK, pieces) == 3) {
            return Integer.MAX_VALUE;
        } else if (Evaluator.maxSeries(TicTacToe.CROSS, pieces) == 3) {
            return Integer.MIN_VALUE;
        }
        return Evaluator.countLines(TicTacToe.TICK, pieces)
                - Evaluator.countLines(TicTacToe.CROSS, pieces);
    }

    /**
     * AI 下棋回合，找最大分数的落点
     */
    private Node findMax(int[][] pieces, int depth) {
        Node node = new Node();
        node.value = Integer.MIN_VALUE;
        // 深度到了，就从所有可能下的点中，挑选出 value 最大的
        if (depth == this.depth) {
            for (int i = 0; i < pieces.length; i++) {
                for (int j = 0; j < pieces[i].length; j++) {
                    if (pieces[i][j] != 0) {
                        continue;
                    }

                    int[][] copy = copy(pieces);
                    copy[i][j] = TicTacToe.TICK;

                    int value = evaluate(copy);

                    if (value > node.value) {
                        node.value = value;
                        node.i = i;
                        node.j = j;
                    }
                }
            }
        } else {
            for (int i = 0; i < pieces.length; i++) {
                for (int j = 0; j < pieces[i].length; j++) {
                    if (pieces[i][j] != 0) {
                        continue;
                    }

                    int[][] copy = copy(pieces);
                    copy[i][j] = TicTacToe.TICK;

                    Node newNode = findMin(copy, depth + 1);

                    if (newNode.value > node.value) {
                        node = newNode;
                        node.i = i;
                        node.j = j;
                    }
                }
            }
        }
        return node;
    }

    /**
     * AI 对手下棋回合，假设对手始终下的棋子位置，都尽可能使 AI 视角棋盘分数最低
     */
    private Node findMin(int[][] pieces, int depth) {
        Node node = new Node();
        node.value = Integer.MAX_VALUE;
        // 深度到了，就从所有可能下的点中，挑选出 value 最小的
        if (depth == this.depth) {
            for (int i = 0; i < pieces.length; i++) {
                for (int j = 0; j < pieces[i].length; j++) {
                    if (pieces[i][j] != 0) {
                        continue;
                    }

                    int[][] copy = copy(pieces);
                    copy[i][j] = TicTacToe.CROSS;

                    int value = evaluate(copy);

                    if (value < node.value) {
                        node.value = value;
                        node.i = i;
                        node.j = j;
                    }
                }
            }
        } else {
            // 深度未到，就从下一层 max 层提供的点中，选择最小的
            for (int i = 0; i < pieces.length; i++) {
                for (int j = 0; j < pieces[i].length; j++) {
                    if (pieces[i][j] != 0) {
                        continue;
                    }

                    int[][] copy = copy(pieces);
                    copy[i][j] = TicTacToe.CROSS;

                    Node newNode = findMax(copy, depth + 1);

                    if (newNode.value < node.value) {
                        node = newNode;
                    }
                }
            }
        }
        return node;
    }

    /**
     * 深度复制二维数组
     */
    private int[][] copy(int[][] pieces) {
        int[][] copy = new int[pieces.length][pieces.length];
        for (int k = 0; k < copy.length; k++) {
            copy[k] = pieces[k].clone();
        }

        return copy;
    }

    static class Node {
        int i;
        int j;
        int value;
    }

    public static void main(String[] args) {
        int[] a = {1, 2, 3};
        change(a);
        System.out.println(a[0]);

        int[][] pieces = {
                {0, 0, 0},
                {0, 0, 0},
                {0, 0, TicTacToe.CROSS}
        };
//        System.out.println(new AI().decide(pieces));
    }

    private static void change(int[] a) {
        a[0] = 666;
    }


}
