import java.util.ArrayList;
import java.util.List;

public class Boggle {
    public static final int BOARD_SIZE = 5;
    public static final int[][] boardDirection = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1}};

    public static int minimumWordLength = 3;

    public static char[][] board = new char[BOARD_SIZE][BOARD_SIZE];
    public static List<Player> players = new ArrayList<Player>();

    public static boolean recursiveBoggleFind(int x, int y, boolean[][] visited, String build, String str) {
        if (build.length() >= str.length()) return false;
        visited[x][y] = true;

        for (int[] direct : boardDirection) {
            int nx = x + direct[0], ny = y + direct[1];
            if (!visited[nx][ny]) {
                String temp = build + board[nx][ny];
                if (temp.equals(str)) return true;
                recursiveBoggleFind(nx, ny, visited, temp, str);
            }
        }
        return false;
    }

    public static boolean findBoggleWord(String str) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                boolean[][] visited = new boolean[BOARD_SIZE][BOARD_SIZE];
                visited[i][j] = true;
                if (recursiveBoggleFind(i, j, visited, "" + board[i][j], str)) return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {

    }

}
