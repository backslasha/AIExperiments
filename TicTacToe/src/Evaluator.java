import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

class Evaluator {

    /**
     * 计算当把空位都放上 pieceType 的棋子时，棋盘中 pieceType 的三连数目
     *
     * @param pieceType 棋子类型
     * @param pieces    棋盘
     */
    static int countLines(int pieceType, int[][] pieces) {
        int N = pieces.length;
        int succession = 0, successions = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (pieces[i][j] == pieceType || pieces[i][j] == 0) {
                    succession++;
                }
            }
            if (succession == pieces.length) successions++;
            succession = 0;
        }
        for (int j = 0; j < N; j++) {
            for (int i = 0; i < N; i++) {
                if (pieces[i][j] == pieceType || pieces[i][j] == 0) {
                    succession++;
                }
            }
            if (succession == pieces.length) successions++;
            succession = 0;
        }
        for (int i = 0, j = 0; i < N && j < N; i++, j++) {
            if (pieces[i][j] == pieceType || pieces[i][j] == 0) {
                succession++;
            }
        }
        if (succession == pieces.length) successions++;

        succession = 0;

        for (int i = 0, j = N - 1; i < N && j >= 0; i++, j--) {
            if (pieces[i][j] == pieceType || pieces[i][j] == 0) {
                succession++;
            }
        }
        if (succession == pieces.length) successions++;

        return successions;

    }

    /**
     * 计算 pieceType 棋子的最大连子数目
     */
    static int maxSeries(int pieceType, int[][] pieces) {
        int N = pieces.length;
        int series = 0, maxSeries = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (pieces[i][j] == pieceType) {
                    series++;
                }
            }

            maxSeries = Math.max(series, maxSeries);
            series = 0;
        }
        for (int j = 0; j < N; j++) {
            for (int i = 0; i < N; i++) {
                if (pieces[i][j] == pieceType) {
                    series++;
                }
            }
            maxSeries = Math.max(series, maxSeries);
            series = 0;
        }
        for (int i = 0, j = 0; i < N && j < N; i++, j++) {
            if (pieces[i][j] == pieceType) {
                series++;
            }
        }
        maxSeries = Math.max(series, maxSeries);
        series = 0;

        for (int i = 0, j = N - 1; i < N && j >= 0; i++, j--) {
            if (pieces[i][j] == pieceType) {
                series++;
            }
        }
        maxSeries = Math.max(series, maxSeries);

        return maxSeries;

    }

    private static int count = 0;

    static int countPieces(int[][] mPieces) {
        count = 0;
        Arrays.stream(mPieces).forEach(
                ints -> Arrays.stream(ints).forEach(
                        value -> {
                            if (value != TicTacToe.EMPTY) {
                                count++;
                            }
                        }
                )
        );
        return count;
    }
}
