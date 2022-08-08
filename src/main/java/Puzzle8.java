import entities.*;
import tests.Input;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

class Puzzle8 {
    private static void play(World initialWorld, World goalWorld) {


        State initialState = initialWorld.zero();
        Agent initialAgent = new Agent(initialState);

        Agent currentAgent = initialAgent;
        World currentWorld = new World(initialWorld);

        Queue<World> frontier = new PriorityQueue<>((o1, o2) -> o2.evaluate() - o1.evaluate());

        for (Action action : Action.values()) {
            Agent nextAgent = currentAgent.transition(action);
            if (nextAgent.belongsTo(currentWorld)) {
                currentWorld = currentWorld.transition(action);
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(Input.TEST_1);

        int k = scanner.nextInt();

        int[][] board = new int[k][k];

        int x = 1, y = 1;
        while (scanner.hasNext()) {
            int cell = scanner.nextInt();
            board[x - 1][y - 1] = cell;
            if (y % k == 0) {
                ++x;
                y = 1;
            } else {
                ++y;
            }
        }

        World initialWorld = new World(board, k);
        World goalWorld = World.complete(k);
    }
}