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

    public Agent transition(Action action) {
        switch (action) {
            case Left: {
                return new Agent(new State(this.state.x, this.state.y - 1), action, cost + 1, new Agent(this.parent));
            }
            case Up: {
                return new Agent(new State(this.state.x - 1, this.state.y), action, cost + 1, new Agent(this.parent));
            }
            case Right: {
                return new Agent(new State(this.state.x, this.state.y + 1), action, cost + 1, new Agent(this.parent));
            }
            case Down: {
                return new Agent(new State(this.state.x + 1, this.state.y), action, cost + 1, new Agent(this.parent));
            }
            default: {
                throw new RuntimeException("Unknown action: " + action);
            }
        }
    }

    public boolean belongsTo(World world) {
        return (this.state.x >= 0 && this.state.x < world.k) &&
                (this.state.y >= 0 && this.state.y < world.k);
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
