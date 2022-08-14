import entities.Action;
import entities.Agent;
import entities.State;
import entities.World;
import org.apache.commons.math3.util.Pair;
import tests.Input;
import tests.Output;

import java.util.*;

class PuzzleRBFS {
    private static Agent recursiveBestFirstSearch(Agent currentAgent, World currentWorld, int fLimit, HashSet<String> worldDatabase, World goalWorld) {
        if (currentWorld.equals(goalWorld)) {
            return currentAgent;
        }

        PriorityQueue<Pair<Agent, World>> successors = new PriorityQueue<>(Comparator.comparingInt(o -> o.getFirst().cost));

        for (Action action : Action.values()) {
            Agent nextAgent = currentAgent.transition(action);
            if (nextAgent == null) {
                continue;
            }
            if (nextAgent.belongsTo(currentWorld)) {
                World nextWorld = currentWorld.transition(currentAgent, action);
                if (nextWorld == null) {
                    continue;
                }
                nextAgent.cost = Math.max(nextAgent.cost + nextWorld.evaluate(), currentAgent.cost);
                if (!worldDatabase.contains(nextWorld.getSerialization())) {
                    worldDatabase.add(nextWorld.getSerialization());
                    successors.add(new Pair<>(nextAgent, nextWorld));
                }
            }
        }

        while (!successors.isEmpty()) {
            Pair<Agent, World> bestPair = successors.poll();
            Agent bestAgent = bestPair.getFirst();
            if (bestAgent.cost > fLimit) {
                return null;
            }
            if (successors.isEmpty()) {
                return null;
            }
            Pair<Agent, World> alternativePair = successors.poll();
            Agent alternativeAgent = alternativePair.getFirst();
            World alternativeWorld = alternativePair.getSecond();
            Agent alternativeSolution = recursiveBestFirstSearch(alternativeAgent, alternativeWorld, Math.min(alternativeAgent.cost  + alternativeWorld.evaluate(), fLimit), worldDatabase, goalWorld);
            if (alternativeSolution != null) {
                return alternativeSolution;
            }
        }

        return null;
    }

    private static Agent play(World initialWorld, World goalWorld) {
        State initialState = initialWorld.zero();
        Agent initialAgent = new Agent(initialState, null, initialWorld.evaluate(), null);

        HashSet<String> worldDatabase = new HashSet<>();
        worldDatabase.add(initialWorld.getSerialization());

        return recursiveBestFirstSearch(initialAgent, initialWorld, Short.MAX_VALUE, worldDatabase, goalWorld);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(Input.TEST_3x3);

        int k = scanner.nextInt();

        String[][] board = new String[k][k];

        int x = 1, y = 1;
        while (scanner.hasNext()) {
            int number = scanner.nextInt();
            board[x - 1][y - 1] = String.valueOf(number);
            if (y % k == 0) {
                ++x;
                y = 1;
            } else {
                ++y;
            }
        }

        World initialWorld = new World(board, k);
        World goalWorld = World.complete(k);
        Agent goalAgent = play(initialWorld, goalWorld);
        Output.printPathAndWorld(goalAgent, initialWorld);
    }
}
