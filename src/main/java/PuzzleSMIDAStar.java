import com.google.common.collect.MinMaxPriorityQueue;
import entities.Action;
import entities.Agent;
import entities.State;
import entities.World;
import org.apache.commons.math3.util.Pair;
import tests.Input;
import tests.Output;

import java.util.*;

class PuzzleSMIDAStar {
    private static class MinMaxComparator {
        public static int compareTo(Pair<Agent, World> o) {
            return (o.getFirst().getCost() + o.getSecond().evaluate());
        }
    }

    private static Agent iterativeDeepening(World initialWorld, World goalWorld, int maximumDepth, int maximumSize) {
        State initialState = initialWorld.zero();
        Agent initialAgent = new Agent(initialState);

        HashSet<String> worldDatabase = new HashSet<>();
        worldDatabase.add(initialWorld.getSerialization());

        MinMaxPriorityQueue<Pair<Agent, World>> frontier = MinMaxPriorityQueue
                .orderedBy(Comparator.comparing(MinMaxComparator::compareTo))
                .maximumSize(maximumSize)
                .create();
        frontier.add(new Pair<>(initialAgent, initialWorld));

        while (!frontier.isEmpty()) {
            Pair<Agent, World> front = frontier.pollFirst();

            Agent currentAgent = front.getFirst();
            World currentWorld = front.getSecond();

            if (currentWorld.equals(goalWorld)) {
                return currentAgent;
            }

            for (Action action : Action.values()) {
                Agent nextAgent = currentAgent.transition(action);
                if (nextAgent == null) {
                    continue;
                }
                if (nextAgent.cost > maximumDepth) {
                    continue;
                }
                if (nextAgent.belongsTo(currentWorld)) {
                    World nextWorld = currentWorld.transition(currentAgent, action);
                    if (nextWorld == null) {
                        continue;
                    }
                    if (!worldDatabase.contains(nextWorld.getSerialization())) {
                        frontier.add(new Pair<>(nextAgent, nextWorld));
                        worldDatabase.add(nextWorld.getSerialization());
                        if (frontier.size() > maximumSize) {
                            frontier.removeLast();
                        }
                    }
                }
            }
        }

        return null;
    }

    private static Agent play(World initialWorld, World goalWorld, int maximumSize) {
        for (double maximumDepth = 1; ; maximumDepth *= Math.sqrt(2)) {
            int maximumDepthRounded = (int) Math.floor(maximumDepth);
            System.out.println("Search depth: " + maximumDepthRounded);
            Agent goalAgent = iterativeDeepening(initialWorld, goalWorld, maximumDepthRounded, maximumSize);
            if (goalAgent != null) {
                return goalAgent;
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(Input.TEST_4x4);

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
        Agent goalAgent = play(initialWorld, goalWorld, 64);
        Output.printPathAndWorld(goalAgent, initialWorld);
    }
}
