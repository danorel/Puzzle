package entities;

public class State {
    int x;
    int y;

    public State(State that) {
        if (that != null) {
            this.x = that.x;
            this.y = that.y;
        }
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
