import java.awt.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.function.Consumer;

import static global.Const.*;

public class AI {

    private int[][] pieces;
    private GoBangBoard board;
    private LinkedHashMap<String, Integer> map;
    private Class constClass = null;


    public AI(GoBangBoard board) {

        this.pieces = board.getPieces();
        this.board = board;

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
        Point point = findPoint();
        board.drop(point.x, point.y);
        System.out.println("evaluate():" + evaluate(board.getLastDrop(), GoBangBoard.WHITE_ONE));
    }

    private Point findPoint() {
        int i = (int) (Math.random() * 15f);
        int j = (int) (Math.random() * 15f);
        if (pieces[i][j] != GoBangBoard.EMPTY) {
            drop();
        }
        return new Point(i, j);
    }

    private int evaluate(Point point, int type) {
        int value = 0;
        String sample;
        for (int i = TOP; i <= LEFT_BOTTOM; i++) {
            sample = getPiecesStream(i, point);
            for (String key : map.keySet()) {
                String patternReal = key.replaceAll("A", String.valueOf(GoBangBoard.WHITE_ONE))
                        .replaceAll(String.valueOf(type), String.valueOf(GoBangBoard.EMPTY));
                if (sample.matches(patternReal)) {
                    value += map.get(key);
                    break;
                }
            }
        }
        return value;
    }

    private String getPiecesStream() {
        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(pieces)
                .forEach(new Consumer<int[]>() {
                    @Override
                    public void accept(int[] ints) {
                        for (int i = 0; i < ints.length; i++) {
                            stringBuilder.append(ints[i]);
                        }
                    }
                });
        return stringBuilder.toString();
    }

    private String getPiecesStream(int orietation, Point point) {
        StringBuilder orietationStream = new StringBuilder();
        int i = point.x;
        int j = point.y;
        int N = pieces[i].length;
        switch (orietation) {
            case TOP:
                for (int t = i - 1; t >= 0; t--) {
                    orietationStream.append(pieces[t][j]);
                }
                break;
            case BOTTOM:
                for (int b = i + 1; b < N; b++) {
                    orietationStream.append(pieces[b][j]);
                }
                break;
            case LEFT:
                for (int l = j - 1; l >= 0; l--) {
                    orietationStream.append(pieces[i][l]);
                }
                break;
            case RIGHT:
                for (int r = j + 1; r < N; r++) {
                    orietationStream.append(pieces[i][r]);
                }
                break;
            case LEFT_TOP:
                for (int t = i - 1, l = j - 1; t >= 0 && l >= 0; t--, l--) {
                    orietationStream.append(pieces[t][l]);
                }
                break;
            case RIGHT_BOTTOM:
                for (int b = i + 1, r = j + 1; b < N && r < N; b++, r++) {
                    orietationStream.append(pieces[b][r]);
                }
                break;
            case RIGHT_TOP:
                for (int t = i - 1, r = j + 1; t >= 0 && r < N; t--, r++) {
                    orietationStream.append(pieces[t][r]);
                }
                break;
            case LEFT_BOTTOM:
                for (int b = i + 1, l = j - 1; b < N && l >= 0; b++, l--) {
                    orietationStream.append(pieces[b][l]);
                }
                break;
            default:
                break;
        }
        return orietationStream.toString();
    }

}
