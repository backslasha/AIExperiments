package ai;

import widget.GoBangBoard;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.function.Consumer;

import static global.Const.*;
import static widget.GoBangBoard.BLACK;
import static widget.GoBangBoard.EMPTY;
import static widget.GoBangBoard.WHITE;

/**
 * ai.AI 固定黑手，用数字 9 表示
 */
public class AI {

    private int[][] pieces;
    private GoBangBoard board;
    private LinkedHashMap<String, Integer> map;
    private Class constClass = null;
    private int N;
    private int pieceType = BLACK;


    public AI(GoBangBoard board) {

        this.pieces = board.getPieces();
        this.board = board;
        this.N = pieces[0].length;
        try {
            constClass = Class.forName("global.Const");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (constClass == null) {
            return;
        }

        map = new LinkedHashMap();
    }

    public void drop() {
        Point point;
        if (GoBangEvaluator.evaluate(pieces) > 0) {
            point = hesitate(BLACK);
        } else {
            point = hesitate(WHITE);
        }
        board.drop(point.x, point.y, true);
    }

    private Point hesitate(int pieceType) {
        boolean attack = pieceType != BLACK;
        Point center = new Point(), result = new Point();
        int[][] imaginaryPieces = new int[15][15];
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; j++) {
                imaginaryPieces[i][j] = pieces[i][j];
            }
        }

        imaginaryPieces[0][0] = BLACK;
        int value = evaluate(center, pieceType, imaginaryPieces);
        imaginaryPieces[0][0] = EMPTY;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (EMPTY == pieces[i][j]) {
                    imaginaryPieces[i][j] = BLACK;
                    center.x = i;
                    center.y = j;
                    int s = evaluate(center, pieceType, imaginaryPieces);
                    s += terrainBonus(i, j);
                    if (attack) {
                        if (s > value) {
                            result.x = center.x;
                            result.y = center.y;
                            value = s;
                        }
                    } else {
                        if (s < value) {
                            result.x = center.x;
                            result.y = center.y;
                            value = s;
                        }
                    }
                    imaginaryPieces[i][j] = EMPTY;
                }
            }
        }
        System.out.println(result.x + "," + result.y + ": " + value);
        return result;
    }
    private static int terrainBonus(int i, int j) {
        return 8 - Math.max(Math.abs(i - 8), Math.abs(j - 8));
    }

    private int evaluate(Point point, int type, int[][] pieces) {
        int value = 0;
        for (int direction = VERTICAL; direction <= DIAGONAL; direction++) {
            String sample = getPiecesStreamByOrietation(direction, point, pieces);
            sample = sample.replaceAll(String.valueOf(pieceType), "x");
            for (int j = 0; j < 5; j++) {
                int counter = 0;
                int beginIndex = j;
                int endIndex = j + 5;
                if (beginIndex > sample.length() - 1) beginIndex = sample.length() - 1;
                if (endIndex > sample.length()) endIndex = sample.length();
                String near = sample.substring(beginIndex, endIndex);
                if (near.contains(String.valueOf("1"))) {
                    continue;
                }
                if (near.contains("xxxxx")) {
                    counter += 10000;
                } else if (near.contains("xxxx")) {
                    counter += 1000;
                } else if (near.contains("xxx")) {
                    counter += 300;
                } else if (near.contains("xx")) {
                    counter += 50;
                } else if (near.contains("x")) {
                    counter += 5;
                } else {
                    counter += 0;
                }
                value += counter;
            }
        }
        return value;
    }

    public int evaluateWhiteScores(Point point) {
        int[][] imagenaryPieces = new int[15][15];
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; j++) {
                imagenaryPieces[i][j] = pieces[i][j];
            }
        }

        int value = 0;
        imagenaryPieces[point.x][point.y] = 1;


        for (int direction = VERTICAL; direction <= DIAGONAL; direction++) {
            String sample = getPiecesStreamByOrietation(direction, point, imagenaryPieces);
            for (int j = 0; j < 5; j++) {
                int v = 0;
                String near = sample.substring(Math.min(sample.length() - 1, j), Math.min(j + 5, sample.length()));
                if (near.contains(String.valueOf("9"))) {
                    continue;
                }
                if (near.contains("11111")) {
                    v += 100000;
                } else if (near.contains("011110")) {
                    v += 10000;
                } else if (near.contains("11110") || near.contains("01111")) {
                    v += 1000;
                } else if (near.contains("01110")) {
                    v += 1000;
                } else if (near.contains("1110") || near.contains("0111")) {
                    v += 500;
                } else if (near.contains("111")) {
                    v += 200;
                } else if (near.contains("0110")) {
                    v += 100;
                } else if (near.contains("011") || near.contains("110")) {
                    v += 50;
                } else if (near.contains("11")) {
                    v += 30;
                } else if (near.contains("010")) {
                    v += 5;
                }
                value += v;
            }
        }
        for (int direction = VERTICAL; direction <= DIAGONAL; direction++) {
            if (board.getLastDrop().x == -1 || board.getLastDrop().y == -1) {
                break;
            }
            String sample = getPiecesStreamByOrietation(direction, board.getLastDrop(), imagenaryPieces);
            for (int j = 0; j < 5; j++) {
                int v = 0;
                String near = sample.substring(Math.min(sample.length() - 1, j), Math.min(j + 5, sample.length()));
                if (near.contains("99999")) {
                    v -= 99999;
                } else if (near.contains("99990")
                        || near.contains("09999")
                        || near.contains("99099")
                        || near.contains("99909")
                        || near.contains("90999")
                        ) {
                    v -= 59999;
                } else if (near.contains("09990")
                        || near.contains("09909")
                        || near.contains("09099")
                        || near.contains("90990")
                        || near.contains("99090")) {
                    v -= 12999;
                }
                value += v;
            }
        }

        return value;
    }

    /**
     * 获取某个点，在某个方向上的前 4 个点开始到后 4 个点结束的 9 个点，超出边界的点忽略。
     *
     * @param orietation 指定方向，{@link VERTICAL,HORIZONTAL,BACKSLASH,DIAGONAL}
     * @param center     指定点
     * @param pieces     当前棋盘数组
     * @return
     */
    private String getPiecesStreamByOrietation(int orietation, Point center, int[][] pieces) {
        StringBuilder orietationStream = new StringBuilder();
        int i = center.x;
        int j = center.y;
        switch (orietation) {
            case VERTICAL: {
                int v = i - 4, n = i + 4;
                if (v < 0) v = 0;
                if (n > N - 1) n = N - 1;
                for (; v <= n; v++) {
                    orietationStream.append(pieces[v][j]);
                }
            }
            break;
            case HORIZONTAL: {
                int h = i - 4, n = i + 4;
                if (h < 0) h = 0;
                if (n > N - 1) n = N - 1;
                for (; h <= n; h++) {
                    orietationStream.append(pieces[i][h]);
                }
            }
            break;
            // :\
            case BACKSLASH: {
                int d = Math.min(Math.min(j, i), 4);
                int t = i - d, l = j - d;
                int nt = Math.min(i + 4, N - 1);
                int nl = Math.min(j + 4, N - 1);
                for (; t <= nt && l <= nl; t++, l++) {
                    orietationStream.append(pieces[t][l]);
                }
            }
            break;
            // :/
            case DIAGONAL: {
                int d = Math.min(i - 0, N - 1 - j);
                if (d > 4) d = 4;
                int t = i - d, r = j + d;
                if (r > N - 1) r = N - 1;
                if (t < 0) t = 0;
                int nt = Math.min(i + 4, N - 1);
                int nr = Math.max(j - 4, 0);
                for (; t <= nt && r >= nr; t++, r--) {
                    orietationStream.append(pieces[t][r]);
                }
            }
            break;
        }
        return orietationStream.toString();
    }

    private String getPiecesStream(int orietation, Point center, int[][] pieces) {
        StringBuilder orietationStream = new StringBuilder();
        int i = center.x;
        int j = center.y;
        switch (orietation) {
            case VERTICAL:
                for (int v = 0; v < N; v++) {
                    orietationStream.append(pieces[v][j]);
                }
                break;
            case HORIZONTAL:
                for (int h = 0; h < N; h++) {
                    orietationStream.append(pieces[i][h]);
                }
                break;
            // :\
            case BACKSLASH:
                int d = Math.min(j, i);
                for (int t = i - d, l = j - d; t < N && l < N; t++, l++) {
                    orietationStream.append(pieces[t][l]);
                }
                break;
            // :/
            case DIAGONAL:
                int d1 = Math.min(i - 0, N - 1 - j);
                for (int t = i - d1, r = j + d1; t < N && r >= 0; t++, r--) {
                    orietationStream.append(pieces[t][r]);
                }
                break;
            default:
                break;
        }
        return orietationStream.toString();
    }

    public static void main(String[] args) {
        GoBangBoard goBangBoard = new GoBangBoard();
        AI ai = new AI(goBangBoard);
        int[][] test = goBangBoard.getPieces();

        Arrays.stream(test)
                .forEach(new Consumer<int[]>() {
                    @Override
                    public void accept(int[] ints) {
                        for (int i = 0; i < ints.length; i++) {
                            if (Math.random() > 0.6) {
                                ints[i] = BLACK;
                            }
                            if (Math.random() > 0.75) {
                                ints[i] = GoBangBoard.WHITE;
                            }
                        }
                    }
                });
        Arrays.stream(test)
                .forEach(new Consumer<int[]>() {
                    @Override
                    public void accept(int[] ints) {
                        for (int i = 0; i < ints.length; i++) {
                            System.out.print(ints[i] + " ");
                            if (i == ints.length - 1) {
                                System.out.println();
                            }
                        }
                    }
                });


        int x = (int) (Math.random() * 15);
        int y = (int) (Math.random() * 15);
//        System.out.println(x + "," + y + " : " + ai.getPiecesStreamByOrietation(12, new Point(x, y), test));
        System.out.println("\nif draw at " + x + "," + y + ", " + "the value is " +
                ai.evaluate(new Point(x, y), BLACK, test));


    }

}
