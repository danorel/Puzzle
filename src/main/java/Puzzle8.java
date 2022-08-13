import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

import db.PatternDatabase;
import entities.*;
import org.apache.commons.math3.util.Pair;

import tests.Input;
import tests.Output;

class Puzzle8 {
    private static void play(World initialWorld, World goalWorld) {
        State initialState = initialWorld.zero();
        Agent initialAgent = new Agent(initialState);

        HashSet<String> worldDatabase = new HashSet<>();
        worldDatabase.add(initialWorld.getSerialization());

        PatternDatabase patternDatabase = new PatternDatabase()
                .getInstance()
                .addPattern("1234")
                .addPattern("5678")
                .compute(initialAgent, initialWorld);

        Queue<Pair<Agent, World>> frontier = new PriorityQueue<>(
                (o1, o2) -> (patternDatabase.evaluate(o2.getSecond()) - patternDatabase.evaluate(o1.getSecond())
        ));
        frontier.add(new Pair<>(initialAgent, initialWorld));

        while (!frontier.isEmpty()) {
            Pair<Agent, World> front = frontier.poll();

            Agent currentAgent = front.getFirst();
            World currentWorld = front.getSecond();

            if (currentWorld.equals(goalWorld)) {
                Output.printPath(currentAgent);
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
                    if (!worldDatabase.contains(nextWorld.getSerialization())) {
                        frontier.add(new Pair<>(nextAgent, nextWorld));
                        worldDatabase.add(nextWorld.getSerialization());
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(Input.TEST_1);

        int k = scanner.nextInt();

        char[][] board = new char[k][k];

        int x = 1, y = 1;
        while (scanner.hasNext()) {
            int digit = scanner.nextInt();
            board[x - 1][y - 1] = (char) (48 + digit);
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
