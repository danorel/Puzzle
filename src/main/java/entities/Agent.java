package entities;

public class Agent {
    State state;
    Action action;
    int cost;
    Agent parent;

    public Agent() {
        this(null, null, 0, null);
    }

    public Agent(State state) {
        this(state, null, 0, null);
    }

    public Agent(State state, Action action, int cost, Agent parent) {
        this.state = new State(state);
        this.action = action;
        this.cost = cost;
        this.parent = new Agent(parent);
    }

    public Agent(Agent that) {
        Agent thisIter = this;
        Agent thatIter = that;
        while (thatIter != null) {
            if (thatIter.state != null) {
                thisIter.state = new State(thatIter.state);
            }
            thisIter.action = thatIter.action;
            thisIter.cost = thatIter.cost;
            if (thatIter.parent != null) {
                thisIter.parent = new Agent();
            }
            thisIter = thisIter.parent;
            thatIter = thatIter.parent;
        }
    }

    @Override
    public String toString() {
        return "Agent{" +
                "cost=" + cost +
                ", state=" + state +
                ", action=" + action +
                ", parent=" + parent +
                '}';
    }
}
