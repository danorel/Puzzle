package entities;

public class State {
    int x;
    int y;

    public State(State that) {
        this(that.x, that.y);
    }

    public State(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "State{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
