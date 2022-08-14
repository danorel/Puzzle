import java.util.*;
import org.apache.commons.math3.util.Pair;

import db.*;
import entities.*;
import tests.*;

class PuzzleKDisjointPatternDatabase {
    private static void play(World initialWorld, World goalWorld) {
        State initialState = initialWorld.zero();
        Agent initialAgent = new Agent(initialState);

        HashSet<String> worldDatabase = new HashSet<>();
        worldDatabase.add(initialWorld.getSerialization());

        PatternDatabase patternDatabase = new PatternDatabase()
                .getInstance()
                .addDefaultPatterns(initialWorld.k)
                .compute(initialAgent, initialWorld);

        Queue<Pair<Agent, World>> frontier = new PriorityQueue<>(
                Comparator.comparingInt(o -> o.getFirst().cost + patternDatabase.evaluate(o.getSecond())));
        frontier.add(new Pair<>(initialAgent, initialWorld));

        while (!frontier.isEmpty()) {
            Pair<Agent, World> front = frontier.poll();

            Agent currentAgent = front.getFirst();
            World currentWorld = front.getSecond();

            if (currentWorld.equals(goalWorld)) {
                Output.printPathAndWorld(currentAgent, initialWorld);
                return;
            }

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
                    String nextSerialization = nextWorld.getSerialization();
                    if (!worldDatabase.contains(nextSerialization)) {
                        frontier.add(new Pair<>(nextAgent, nextWorld));
                        worldDatabase.add(nextSerialization);
                    }
                }
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

        play(initialWorld, goalWorld);
    }
}
