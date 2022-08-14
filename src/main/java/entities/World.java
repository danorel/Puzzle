package entities;

import db.Pattern;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class World {
    public int k;
    public String[][] board;
    public HashMap<String, State> index;

    private int h1 = 0;
    private int h2 = 0;

    public World(World that) {
        this(that.board, that.k, null);
    }

    public World(World that, Pattern pattern) {
        this(that.board, that.k, pattern);
    }

    public World(String[][] board, int k) {
        this(board, k, null);
    }

    public World(String[][] board, int k, Pattern pattern) {
        this.index = new HashMap<>();
        this.k = k;
        this.board = new String[k][k];
        // board filling
        for (int i = 0; i < k; ++i) {
            for (int j = 0; j < k; ++j) {
                if (pattern == null) {
                    this.board[i][j] = board[i][j];
                } else {
                    if (board[i][j].equals("0") || pattern.contains(board[i][j])) {
                        this.board[i][j] = board[i][j];
                    } else {
                        this.board[i][j] = "*";
                    }
                }
                this.index.put(board[i][j], new State(i, j));
            }
        }
        // heuristic computation
        for (int i = 0; i < k; ++i) {
            for (int j = 0; j < k; ++j) {
                State expectedState = new State(i, j);
                State actualState = index.get(String.valueOf(i * k + j));
                if (actualState == null) {
                    continue;
                }
                h1 += !Objects.equals(this.board[expectedState.x][expectedState.y], this.board[actualState.y][actualState.y]) ? 1 : 0;
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
        String[][] board = new String[k][k];
        for (int i = 0; i < k; ++i) {
            for (int j = 0; j < k; ++j) {
                board[i][j] = String.valueOf(i * k + j);
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
        String temp = this.board[i1][j1];
        this.board[i1][j1] = this.board[i2][j2];
        this.board[i2][j2] = temp;
        return this;
    }

    public String getSerialization() {
        return this.getSerialization(",");
    }

    public String getSerialization(String delimiter) {
        StringBuilder serialization = new StringBuilder();
        for (int i = 0; i < k; ++i) {
            for (int j = 0; j < k; ++j) {
                serialization.append(this.board[i][j]).append(delimiter);
            }
        }
        return serialization.toString();
    }

    public String getBoard() {
        StringBuilder board = new StringBuilder();
        for (int i = 0; i < k; ++i) {
            for (int j = 0; j < k; ++j) {
                board.append(this.board[i][j]).append(" ");
            }
            if (i != k - 1) {
                board.append("\n");
            }
        }
        return board.toString();
    }

    public boolean equals(World that) {
        if (this.k != that.k) {
            return false;
        }
        for (int i = 0; i < k; ++i) {
            for (int j = 0; j < k; ++j) {
                if (!Objects.equals(this.board[i][j], that.board[i][j])) {
                    return false;
                }
            }
        }
        return true;
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
