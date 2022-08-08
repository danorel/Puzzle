package entities;

import java.util.Arrays;
import java.util.HashMap;

public class World {
    int k;
    int[][] board;
    HashMap<Integer, State> index;

    public World(int[][] board, int k) {
        this.index = new HashMap<>();
        this.k = k;
        this.board = new int[k][k];
        for (int i = 0; i < k; ++i) {
            for (int j = 0; j < k; ++j) {
                this.board[i][j] = board[i][j];
                this.index.put(board[i][j], new State(i, j));
            }
        }
    }

    public int evaluate() {
        int distance = 0;
        for (int i = 0; i < k; ++i) {
            for (int j = 0; j < k; ++j) {
                State expectedState = new State(i, j);
                State actualState = index.get(i * k + j);
                distance += Score.manhattanDistance(expectedState, actualState);
            }
        }
        return distance;
    }

    @Override
    public String toString() {
        return "World{" +
                "k=" + k +
                ", board=" + Arrays.deepToString(board) +
                '}';
    }
}
