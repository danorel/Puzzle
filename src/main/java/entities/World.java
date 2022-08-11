package entities;

import db.PatternInstance;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class World {
    int k;
    char[][] board;
    HashMap<Character, State> index;

    private int h1 = 0;
    private int h2 = 0;

    public World(World that) {
        this(that.board, that.k, null);
    }

    public World(World that, PatternInstance patternInstance) {
        this(that.board, that.k, patternInstance);
    }

    public World(char[][] board, int k) {
        this(board, k, null);
    }

    public World(char[][] board, int k, PatternInstance patternInstance) {
        this.index = new HashMap<>();
        this.k = k;
        this.board = new char[k][k];
        // board filling
        for (int i = 0; i < k; ++i) {
            for (int j = 0; j < k; ++j) {
                if (patternInstance == null) {
                    this.board[i][j] = board[i][j];
                } else {
                    this.board[i][j] = (board[i][j] == '0' || patternInstance.contains(board[i][j])) ? board[i][j] : '*';
                }
                this.index.put(board[i][j], new State(i, j));
            }
        }
        // heuristic computation
        for (int i = 0; i < k; ++i) {
            for (int j = 0; j < k; ++j) {
                State expectedState = new State(i, j);
                State actualState = index.get((char) (48 + (i * k + j)));
                if (actualState == null) {
                    continue;
                }
                h1 += this.board[expectedState.x][expectedState.y] != this.board[actualState.y][actualState.y] ? 1 : 0;
                h2 += Score.manhattanDistance(expectedState, actualState);
            }
        }
    }

    public World transition(Agent agent, Action action) {
        switch (action) {
            case Left: {
                return new World(this).swap(agent.state.x, agent.state.y, agent.state.x, agent.state.y - 1);
            }
            case Up: {
                return new World(this).swap(agent.state.x, agent.state.y, agent.state.x - 1, agent.state.y);
            }
            case Right: {
                return new World(this).swap(agent.state.x, agent.state.y, agent.state.x, agent.state.y + 1);
            }
            case Down: {
                return new World(this).swap(agent.state.x, agent.state.y, agent.state.x + 1, agent.state.y);
            }
            default: {
                throw new RuntimeException("Unknown action: " + action);
            }
        }
    }

    public static World complete(int k) {
        char[][] board = new char[k][k];
        for (int i = 0; i < k; ++i) {
            for (int j = 0; j < k; ++j) {
                board[i][j] = (char) (48 + (i * k + j));
            }
        }
        return new World(board, k);
    }

    public State zero() {
        return this.index.get('0');
    }

    public int evaluate() {
        return h2;
    }

    private World swap(int i1, int j1, int i2, int j2) {
        if (i1 < 0 || i1 >= k || j1 < 0 || j1 >= k || i2 < 0 || i2 >= k || j2 < 0 || j2 >= k) {
            return null;
        }
        char temp = this.board[i1][j1];
        this.board[i1][j1] = this.board[i2][j2];
        this.board[i2][j2] = temp;
        return this;
    }

    public String getSerialization() {
        StringBuilder serialization = new StringBuilder();
        for (int i = 0; i < k; ++i) {
            for (int j = 0; j < k; ++j) {
                serialization.append(this.board[i][j]);
            }
        }
        return serialization.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof World)) return false;
        World world = (World) o;
        return k == world.k && Arrays.deepEquals(board, world.board) && index.equals(world.index);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(k, index);
        result = 31 * result + Arrays.deepHashCode(board);
        return result;
    }

    @Override
    public String toString() {
        return "World{" +
                "k=" + k +
                ", board=" + Arrays.deepToString(board) +
                '}';
    }
}
