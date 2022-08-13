package tests;

import entities.Agent;

public class Output {
    public static void printPath(Agent agent) {
        if (agent != null) {
            System.out.printf("%d%n", agent.size());
            Agent agentIter = agent.reverseAndCopy();
            while (agentIter != null) {
                if (agentIter.action != null) {
                    System.out.printf("%s%n", agentIter.action.getMove());
                }
                agentIter = agentIter.parent;
            }
        }
    }
}
