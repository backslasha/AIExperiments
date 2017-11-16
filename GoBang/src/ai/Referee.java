package ai;

import widget.GoBangBoard;

public class Referee {
    private int[][] pieces;
    private GoBangBoard board;

    public Referee(GoBangBoard board) {
        this.board = board;
        this.pieces = board.getPieces();
    }

    public boolean gameOver(int i, int j) {
        int N = pieces[i].length;
        int type = pieces[i][j];
        int succession = 1;
        for (int t = i - 1; t >= 0; t--) {
            if (type == pieces[t][j]) {
                succession++;
            } else {
                break;
            }
        }

        for (int b = i + 1; b < N; b++) {
            if (type == pieces[b][j]) {
                succession++;
            } else {
                break;
            }
        }
        if (succession >= 5) {
            return true;
        } else {
            succession = 1;
        }

        for (int l = j - 1; l >= 0; l--) {
            if (type == pieces[i][l]) {
                succession++;
            } else {
                break;
            }
        }


        for (int r = j + 1; r < N; r++) {
            if (type == pieces[i][r]) {
                succession++;
            } else {
                break;
            }
        }
        if (succession >= 5) {
            return true;
        } else {
            succession = 1;
        }

        for (int t = i - 1, l = j - 1; t >= 0 && l >= 0; t--, l--) {
            if (type == pieces[t][l]) {
                succession++;
            } else {
                break;
            }
        }

        for (int b = i + 1, r = j + 1; b < N && r < N; b++, r++) {
            if (type == pieces[b][r]) {
                succession++;
            } else {
                break;
            }
        }
        if (succession >= 5) {
            return true;
        } else {
            succession = 1;
        }

        for (int t = i - 1, r = j + 1; t >= 0 && r < N; t--, r++) {
            if (type == pieces[t][r]) {
                succession++;
            } else {
                break;
            }
        }

        for (int b = i + 1, l = j - 1; b < N && l >= 0; b++, l--) {
            if (type == pieces[b][l]) {
                succession++;
            } else {
                break;
            }
        }

        return succession >= 5;

    }
}
