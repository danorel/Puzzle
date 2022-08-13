package entities;

import java.util.Stack;

public class Agent {
    public State state;
    public Action action;
    public int cost;
    public Agent parent;

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
        if (parent != null) {
            this.parent = new Agent(parent);
        }
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

    public int size() {
        if (this.parent == null) {
            return 0;
        }
        int size = 0;
        Agent thisIterAgent = this.parent;
        while (thisIterAgent.parent != null) {
            ++size;
            thisIterAgent = thisIterAgent.parent;
        }
        return size - 1;
    }

    public Agent reverseAndCopy() {
        Agent reverse = new Agent();

        Stack<Agent> stack = new Stack<>();

        Agent thatIterAgent = this;
        while (thatIterAgent != null) {
            stack.push(thatIterAgent);
            thatIterAgent = thatIterAgent.parent;
        }

        Agent thisIterAgent = reverse;
        while (!stack.empty()) {
            thatIterAgent = stack.pop();
            thisIterAgent.state = new State(thatIterAgent.state);
            thisIterAgent.cost = thatIterAgent.cost;
            thisIterAgent.action = thatIterAgent.action;
            thisIterAgent.parent = new Agent();
            thisIterAgent = thisIterAgent.parent;
        }

        return reverse;
    }

    public Agent transition(Action action) {
        switch (action) {
            case Left: {
                return new Agent(new State(this.state.x, this.state.y - 1), action, cost + 1, new Agent(this));
            }
            case Up: {
                return new Agent(new State(this.state.x - 1, this.state.y), action, cost + 1, new Agent(this));
            }
            case Right: {
                return new Agent(new State(this.state.x, this.state.y + 1), action, cost + 1, new Agent(this));
            }
            case Down: {
                return new Agent(new State(this.state.x + 1, this.state.y), action, cost + 1, new Agent(this));
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
