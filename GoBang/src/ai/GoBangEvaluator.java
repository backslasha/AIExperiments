package ai;

import widget.GoBangBoard;

import java.awt.*;
import java.util.Arrays;
import java.util.LinkedHashMap;

import static global.Const.*;
import static widget.GoBangBoard.*;

/**
 * ai.AI 固定黑手，用数字 9 表示
 */
public class GoBangEvaluator {

    private static int[][] pieces;
    private GoBangBoard board;
    private LinkedHashMap<String, Integer> map;
    private Class constClass = null;
    private static int N;
    private static int pieceType = GoBangBoard.BLACK;


    public GoBangEvaluator(GoBangBoard board) {

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
        Point point = hesitate();
        board.drop(point.x, point.y, true);
    }

    private static Point hesitate() {
        Point result = new Point();
        int[][] assumePieces = new int[15][15];
        for (int i = 0; i < pieces.length; i++) {
            assumePieces[i] = pieces[i];
        }

        assumePieces[0][0] = pieceType;

        int value = evaluate(assumePieces);

        assumePieces[0][0] = EMPTY;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (EMPTY == pieces[i][j]) {
                    assumePieces[i][j] = pieceType;
                    int s = evaluate(assumePieces);

                    if (s > value) {
                        result.x = i;
                        result.y = j;
                        value = s;
                    }
                    assumePieces[i][j] = EMPTY;
//                    System.out.println("(" + i + "," + j + "): " + s);
                }
            }
        }

//        System.out.println("finally: (" + result.x + "," + result.y + "): " + value);

        return result;
    }

    private static int terrainBonus(int i, int j) {
        return 8 - Math.max(Math.abs(i - 8), Math.abs(j - 8));
    }

    public static int evaluate(int[][] pieces) {
        int value = 0;
        int oValue = 0;
        int n = pieces.length;

        for (int i = 0; i < n; i++) {
            String piecesStr = extract(i, 0, HORIZONTAL, pieces);
            value = Math.max(value, evaluateLine(piecesStr, BLACK));
            oValue = Math.max(oValue, evaluateLine(piecesStr, WHITE));
        }

        for (int j = 0; j < n; j++) {
            String piecesStr = extract(0, j, VERTICAL, pieces);
            value = Math.max(value, evaluateLine(piecesStr, BLACK));
            oValue = Math.max(oValue, evaluateLine(piecesStr, WHITE));
        }

        for (int a = 0; a < n - 4; a++) {
            String piecesStr = extract(a, 0, BACKSLASH, pieces);
            value = Math.max(value, evaluateLine(piecesStr, BLACK));
            oValue = Math.max(oValue, evaluateLine(piecesStr, WHITE));

            piecesStr = extract(0, a, BACKSLASH, pieces);
            value += evaluateLine(piecesStr, BLACK);
            oValue = Math.max(oValue, evaluateLine(piecesStr, WHITE));
        }
        {
            for (int a = 4; a < n; a++) {
                String piecesStr = extract(a, 0, DIAGONAL, pieces);
                value = Math.max(value, evaluateLine(piecesStr, BLACK));
                oValue = Math.max(oValue, evaluateLine(piecesStr, WHITE));
            }

            for (int a = 0; a < n - 4; a++) {
                String piecesStr = extract(n - 1, a, DIAGONAL, pieces);
                value = Math.max(value, evaluateLine(piecesStr, BLACK));
                oValue = Math.max(oValue, evaluateLine(piecesStr, WHITE));
            }
        }
        System.out.println("value:" + value + ",oValue:" + oValue);
        return value - oValue;
    }

    private static int evaluateLine(String piecesStr, int pieceType) {
        if (pieceType == GoBangBoard.BLACK) {
            piecesStr = piecesStr.replaceAll("x", "1");
            piecesStr = piecesStr.replaceAll("9", "x");
        } else if (pieceType == GoBangBoard.WHITE) {
            piecesStr = piecesStr.replaceAll("x", "9");
            piecesStr = piecesStr.replaceAll("1", "x");
        } else {
            throw new IllegalArgumentException("PieceType error!");
        }
        int counter = 0;
        if (piecesStr.contains("xxxxx")) {
            counter += 50000;
        } else if (piecesStr.contains("0xxxx0")) {
            counter += 4320;
        } else if (piecesStr.contains("0xxx00")) {
            counter += 720;
        } else if (piecesStr.contains("00xxx0")) {
            counter += 720;
        } else if (piecesStr.contains("0xx0x0")) {
            counter += 720;
        } else if (piecesStr.contains("0x0xx0")) {
            counter += 720;
        } else if (piecesStr.contains("xxxx0")) {
            counter += 720;
        } else if (piecesStr.contains("0xxxx")) {
            counter += 720;
        } else if (piecesStr.contains("x0xxx")) {
            counter += 720;
        } else if (piecesStr.contains("xxx0x")) {
            counter += 720;
        } else if (piecesStr.contains("xx0xx")) {
            counter += 720;
        } else if (piecesStr.contains("00xx00")) {
            counter += 120;
        } else if (piecesStr.contains("00x0x0")) {
            counter += 120;
        } else if (piecesStr.contains("0x0x00")) {
            counter += 120;
        } else if (piecesStr.contains("000x00")) {
            counter += 20;
        } else if (piecesStr.contains("00x000")) {
            counter += 20;
        }
        return counter;
    }

    /**
     * 拆出某一个点所在行、列、斜杆或者反斜杆的落子情况，转化为字符串返回
     *
     * @param i
     * @param j
     * @param orientation
     * @param pieces
     * @return
     */
    private static String extract(int i, int j, int orientation, int[][] pieces) {
        StringBuilder piecesStr = new StringBuilder();
        int N = pieces.length;
        switch (orientation) {
            case HORIZONTAL:
                Arrays.stream(pieces[i])
                        .forEach(piecesStr::append);
                break;
            case VERTICAL:
                Arrays.stream(pieces)
                        .forEach(ints -> piecesStr.append(ints[j]));
                break;
            // :\
            case BACKSLASH: {
                int d = Math.min(j, i);
                int t = i - d, l = j - d;
                for (; t <= N - 1 && l <= N - 1; t++, l++) {
                    piecesStr.append(pieces[t][l]);
                }
            }
            break;
            // :/
            case DIAGONAL: {
                int d = Math.min(i, N - 1 - j);
                int t = i - d, r = j + d;
                if (r > N - 1) r = N - 1;
                if (t < 0) t = 0;
                for (; t <= N - 1 && r >= 0; t++, r--) {
                    piecesStr.append(pieces[t][r]);
                }
            }
            break;
        }
        return piecesStr.toString();
    }

    public static void main(String[] args) {
        GoBangBoard goBangBoard = new GoBangBoard();
        GoBangEvaluator ai = new GoBangEvaluator(goBangBoard);
        int[][] test = goBangBoard.getPieces();

//        Arrays.stream(test)
//                .forEach(ints -> {
//                    for (int i = 0; i < ints.length; i++) {
//                        if (Math.random() > 0.6) {
//                            ints[i] = GoBangBoard.BLACK;
//                        }
//                        if (Math.random() > 0.75) {
//                            ints[i] = GoBangBoard.WHITE;
//                        }
//                    }
//                });
        test[0][2] = 9;
        Arrays.stream(test)
                .forEach(ints -> {
                    for (int i = 0; i < ints.length; i++) {
                        System.out.print(ints[i] + " ");
                        if (i == ints.length - 1) {
                            System.out.println();
                        }
                    }
                });
//

        int x = (int) (Math.random() * 15);
        int y = (int) (Math.random() * 15);

//        System.out.println("\nif draw at " + x + "," + y + ", " + "the value is " +
//                ai.evaluate(new Point(x, y), widget.GoBangBoard.BLACK, test));
        System.out.println(extract(0, 2, VERTICAL, test));
        hesitate();

    }

}
