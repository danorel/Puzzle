package entities;

public class Score {
    private static int costDistance(Agent fromAgent, Agent toAgent) {
        return Math.abs(toAgent.cost - fromAgent.cost);
    }

    public static int manhattanDistance(State fromState, State toState) {
        return Math.abs(fromState.x - toState.x) + Math.abs(fromState.y - toState.y);
    }

    public static int hScore(Agent currentAgent, Agent goalAgent) {
        return manhattanDistance(currentAgent.state, goalAgent.state);
    }

    public static int gScore(Agent initialAgent, Agent currentAgent) {
        return costDistance(initialAgent, currentAgent);
    }

    public static int fScore(Agent initialAgent, Agent currentAgent, Agent goalAgent) {
        int g = gScore(initialAgent, currentAgent);
        int h = hScore(currentAgent, goalAgent);
        return g + h;
    }
}
