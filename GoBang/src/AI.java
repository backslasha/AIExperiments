import global.Const;
import sun.invoke.empty.Empty;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashMap;

import static global.Const.*;

public class AI {

    private int[][] pieces;
    private GoBangBoard board;
    private LinkedHashMap<String, Integer> map;
    private Class constClass = null;
    private int N;
    private int pieceType = GoBangBoard.BLACK_ONE;


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
        Arrays.stream(constClass.getDeclaredFields())
                .filter(field -> field.getName().startsWith("SCORE"))
                .sorted((o1, o2) -> {
                    o1.setAccessible(true);
                    o2.setAccessible(true);
                    try {
                        return (int) o2.get(AI.this) - (int) o1.get(AI.this);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return 0;
                })
                .forEach(field -> {
                    try {
                        int score = (int) field.get(AI.this);
                        Field patternFiled = constClass.getDeclaredField(
                                field.getName()
                                        .replaceAll("SCORE", "PATTERN")
                        );
                        patternFiled.setAccessible(true);
                        String pattern = (String) patternFiled.get(AI.this);
                        map.put(pattern, score);
                    } catch (IllegalAccessException | NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                });
        System.out.println(map);

    }

    public void drop() {
        Point point = muse();
        board.drop(point.x, point.y);
    }

    private Point muse() {
        int value = 0;
        int[][] imagenaryPieces = pieces.clone();
        Point temp = new Point(), result = new Point();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (GoBangBoard.EMPTY == pieces[i][j]) {
                    imagenaryPieces[i][j] = pieceType;
                    temp.x = i;
                    temp.y = j;
                    if (evaluate(temp, pieceType, imagenaryPieces) > value) {
                        result.x = temp.x;
                        result.y = temp.y;
                    }
                    imagenaryPieces[i][j] = GoBangBoard.EMPTY;
                }
            }
        }
        return result;
    }

    private int evaluate(Point point, int type, int[][] pieces) {
        int value = 0;
        String sample;
        for (int i = VERTICAL; i <= DIAGONAL; i++) {
            sample = getPiecesStream(i, point, pieces);
            boolean findMax = false, findMin = false;
            for (String key : map.keySet()) {
                String patternReal = key.replaceAll("A", String.valueOf(type));
                patternReal = patternReal.replaceAll("O", String.valueOf(GoBangBoard.EMPTY));
                if (!findMax && sample.contains(patternReal)) {
                    value += map.get(key);
                    findMax = true;
                }
                int oppisiteType = pieceType == GoBangBoard.BLACK_ONE ? GoBangBoard.WHITE_ONE : GoBangBoard.BLACK_ONE;
                patternReal = patternReal.replaceAll(String.valueOf(pieceType), String.valueOf(oppisiteType));
                if (!findMin && sample.contains(patternReal)) {
                    int min = -map.get(key);
                    if (key == Const.PATTERN_THREE_ALIVE_OOAAAOO) {
                        min *= 100;
                    }
                    value = +min;
                    findMin = true;
                }

                if (findMax && findMin) {
                    break;
                }
            }
        }
        System.out.println("if draw at " + point.x + "," + point.y + ", " + "the value is " + value);
        return value;
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

}
