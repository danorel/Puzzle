import entities.World;
import tests.Input;

import java.util.Arrays;
import java.util.Scanner;

class Puzzle8 {
    private static void play() {

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

        World world = new World(board, k);
        System.out.println(world);
        System.out.println(world.evaluate());
    }
}