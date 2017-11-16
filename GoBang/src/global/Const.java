package global;

import java.util.Arrays;
import java.util.function.Consumer;

public class Const {
    public static final int TOP = 1;
    public static final int BOTTOM = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;
    public static final int LEFT_TOP = 5;
    public static final int RIGHT_BOTTOM = 6;
    public static final int RIGHT_TOP = 7;
    public static final int LEFT_BOTTOM = 8;
    public static final int VERTICAL = 9;
    public static final int HORIZONTAL= 10;
    public static final int BACKSLASH = 11;
    public static final int DIAGONAL = 12;


    public static final int SCORE_SINGLE = 0;
    public static final int SCORE_TWO_DEAD_AAOOO = 150;
    public static final int SCORE_TWO_DEAD_OAOOAO = 200;
    public static final int SCORE_TWO_DEAD_OOAOAOO = 250;
    public static final int SCORE_THREE_DEAD_OOAAA = 500;
    public static final int SCORE_THREE_DEAD_AOAOA = 550;
    public static final int SCORE_THREE_DEAD_AOOAA = 600;
    public static final int SCORE_TWO_ALIVE_OOOAAOOO = 650;
    public static final int SCORE_THREE_DEAD_OAOAOOA = 800;
    public static final int SCORE_FOUR_DEAD_AAAAO = 2500;
    public static final int SCORE_FOUR_DEAD_AAOAA = 2600;
    public static final int SCORE_THREE_ALIVE_OOAAAOO = 3000;
    public static final int SCORE_FOUR_DEAD_AAAOA = 3000;
    public static final int SCORE_FOUR_ALIVE_OAAAAO = 300000;

    public static final String PATTERN_SINGLE = "A";
    public static final String PATTERN_TWO_DEAD_AAOOO = "AAOOO";
    public static final String PATTERN_TWO_DEAD_OAOOAO = "OAOOAO";
    public static final String PATTERN_TWO_DEAD_OOAOAOO = "OOAOAOO";
    public static final String PATTERN_TWO_ALIVE_OOOAAOOO = "OOOAAOOO";
    public static final String PATTERN_THREE_DEAD_AOAOA = "AOAOA";
    public static final String PATTERN_THREE_DEAD_AOOAA = "AOOAA";
    public static final String PATTERN_THREE_DEAD_OAOAOOA = "OAOAOOA";
    public static final String PATTERN_THREE_DEAD_OOAAA = "OOAAA";
    public static final String PATTERN_THREE_ALIVE_OOAAAOO = "OOAAAOO";
    public static final String PATTERN_FOUR_DEAD_AAOAA = "AAOAA";
    public static final String PATTERN_FOUR_DEAD_AAAOA = "AAAOA";
    public static final String PATTERN_FOUR_DEAD_AAAAO = "AAAAO";
    public static final String PATTERN_FOUR_ALIVE_OAAAAO = "OAAAAO";

    public static final int SCORE_TWO_DEAD_OOOAA = 150;
    public static final int SCORE_THREE_DEAD_AAAOO = 500;
    public static final int SCORE_THREE_DEAD_AAOOA = 600;
    public static final int SCORE_THREE_DEAD_AOOAOAO = 800;
    public static final int SCORE_FOUR_DEAD_OAAAA = 2500;
    public static final int SCORE_FOUR_DEAD_AOAAA = 3000;


    public static final String PATTERN_TWO_DEAD_OOOAA = "OOOAA";
    public static final String PATTERN_THREE_DEAD_AAAOO = "AAAOO";
    public static final String PATTERN_THREE_DEAD_AAOOA = "AAOOA";
    public static final String PATTERN_THREE_DEAD_AOOAOAO = "AOOAOAO";
    public static final String PATTERN_FOUR_DEAD_OAAAA = "OAAAA";
    public static final String PATTERN_FOUR_DEAD_AOAAA = "AOAAA";


    public static void main(String[] args) {
        int[][] pieces = new int[5][5];
        int[][] imagenaryPieces = pieces.clone();
        Arrays.stream(imagenaryPieces).forEach(ints -> {
            for (int i = 0; i < ints.length; i++) {
                ints[i] = 3;
            }
        });
        Arrays.stream(pieces).forEach(ints -> {
            for (int i = 0; i < ints.length; i++) {
                System.out.print(ints[i]);
                if (i == ints.length - 1) {
                    System.out.println();
                }
            }
        });

        if (pieces[0] == imagenaryPieces[0]) {
            System.out.println("shit");
        }

        int[] test = new int[10];
        int[] copyOfTest = test.clone();
        for (int i = 0; i < copyOfTest.length; i++) {
            copyOfTest[i]= 55;
        }
        for (int i = 0; i < test.length; i++) {
            System.out.print(test[i]);
        }

    }
}
