package entities;

public enum Action {
    Left("LEFT"),
    Up("UP"),
    Right("RIGHT"),
    Down("DOWN");

    final String move;

    Action(String move) {
        this.move = move;
    }

    public String getMove() {
        return this.move;
    }

    @Override
    public String toString() {
        return "Action{" +
                "move='" + move + '\'' +
                '}';
    }
}
