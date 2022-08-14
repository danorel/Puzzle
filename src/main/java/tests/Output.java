package tests;

import entities.Agent;
import entities.World;

public class Output {
    public static void printPathAndWorld(Agent goalAgent, World initialWorld) {
        if (goalAgent != null) {
            System.out.printf("%d%n", goalAgent.size());
            System.out.printf("-----%n");
            Agent agentIter = goalAgent.reverseAndCopy();
            World worldIter = new World(initialWorld);
            while (agentIter != null) {
                System.out.printf("%s%n", worldIter.getBoard());
                if (agentIter.action != null) {
                    System.out.printf("%s%n", agentIter.action.getMove());
                    System.out.printf("-----%n");
                    worldIter = worldIter.transition(agentIter, agentIter.action);
                }
                agentIter = agentIter.parent;
            }
        }
    }

    public static void printPath(Agent goalAgent) {
        if (goalAgent != null) {
            System.out.printf("%d%n", goalAgent.size());
            Agent agentIter = goalAgent.reverseAndCopy();
            while (agentIter != null) {
                if (agentIter.action != null) {
                    System.out.printf("%s%n", agentIter.action.getMove());
                }
                agentIter = agentIter.parent;
            }
        }
    }
}
