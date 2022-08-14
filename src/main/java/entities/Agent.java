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
        if (that != null) {
            Agent thisIter = this;
            Agent thatIter = that;
            while (thatIter != null) {
                thisIter.state = new State(thatIter.state);
                thisIter.action = thatIter.action;
                thisIter.cost = thatIter.cost;
                if (thatIter.parent != null) {
                    thisIter.parent = new Agent();
                }
                thisIter = thisIter.parent;
                thatIter = thatIter.parent;
            }
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

        Stack<Agent> thisStack = new Stack<>();

        Agent thisIterAgent = this;
        while (thisIterAgent != null) {
            thisStack.push(thisIterAgent);
            thisIterAgent = thisIterAgent.parent;
        }

        if (thisStack.empty()) {
            return reverse;
        }

        Agent thisParentIterAgent = thisStack.pop();
        Agent reverseIterAgent = reverse;
        while (!thisStack.empty()) {
            thisIterAgent = thisStack.pop();
            if (thisIterAgent.state != null) {
                reverseIterAgent.state = new State(thisParentIterAgent.state);
            }
            reverseIterAgent.cost = thisParentIterAgent.cost;
            reverseIterAgent.action = thisIterAgent.action;
            reverseIterAgent.parent = new Agent();
            reverseIterAgent = reverseIterAgent.parent;
            thisParentIterAgent = thisIterAgent;
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

    public int getCost() {
        return cost;
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
