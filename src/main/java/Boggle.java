//import
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/*
 * Due: June 12th
 */

//the class boggle 
public class Boggle { 
    public static final int BOARD_SIZE = 5;
    public static final int[][] boardDirection = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1}}; //the possible direction that the program can move on the board 

    public static int minimumWordLength = 3, pointsToPlay = 20, numberOfPlayers = 2, currentPlayerIndex = 0;

    public static char[][] board = new char[BOARD_SIZE][BOARD_SIZE]; //declare and set the 2d array for the baord
    public static List<Player> players = new ArrayList<>(); //declare and set the array list for the players 

    public static HashSet<String> validWords = new HashSet<>();

    public static Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public static void generateBoard() {
        ArrayList<String> dice = new ArrayList<>(Arrays.asList("AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM", "AEEGMU", "AEGMNN", "AFIRSY", "BJKQXZ", "CCNSTW", "CEIILT", "CEILPT", "CEIPST", "DDLNOR", "DHHLOR", "DHHNOT", "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU", "FIPRSY", "GORRVW", "HIPRRY", "NOOTUW", "OOOTTU"));

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                int ind = (int) (Math.random() * dice.size());
                board[i][j] = dice.get(ind).charAt((int) (Math.random() * dice.get(ind).length()));
                dice.remove(ind);
            }
        }
    }

    public static boolean recursiveBoggleFind(int x, int y, boolean[][] visited, int searchIndex, String str) {
        if (searchIndex == str.length()) return true;
        visited[x][y] = true;

        for (int[] direct : boardDirection) {
            int nx = x + direct[0], ny = y + direct[1];
            if (nx >= BOARD_SIZE || nx < 0 || ny >= BOARD_SIZE || ny < 0) continue; // check out of bounds

            // check if not visited, correct character, and the recursive search returns true
            if (!visited[nx][ny] && (board[nx][ny] == str.charAt(searchIndex)) && recursiveBoggleFind(nx, ny, visited, searchIndex + 1, str)) {
                return true;
            }
        }
        return false;
    }

    public static boolean findBoggleWord(String str) {
        str = str.toUpperCase();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                boolean[][] visited = new boolean[BOARD_SIZE][BOARD_SIZE];
                visited[i][j] = true;
                if (recursiveBoggleFind(i, j, visited, 0, str)) return true;
            }
        }
        return false;
    }

    public static int getGuessWordsPoints(String word) {
        int score = 0;
        if (word.length() >= minimumWordLength && validWords.contains(word) && findBoggleWord(word)) {
            score = word.length();
        }
        return score;
    }

    public static void handleTurn(String word) {
        int points = getGuessWordsPoints(word);
        Player p = getCurrentPlayer();
        p.setScore(p.getScore() + points);
        System.out.println("Handling " + p.getName() + "'s turn.");
        System.out.println("Score: " + p.getScore());
        if (p.getScore() >= pointsToPlay) {
            // TODO win
        } else {
            nextTurn();
        }
    }

    public static void nextTurn() {
        currentPlayerIndex++;
        if( currentPlayerIndex == numberOfPlayers) currentPlayerIndex = 0;
        System.out.println("Switching to " + getCurrentPlayer().getName() + "'s turn.");
        BoggleGUI.stage.setScene(GameScene.getScene());
    }

    public static void startGame() {
        generateBoard();
        currentPlayerIndex = 0;
        BoggleGUI.stage.setScene(GameScene.getScene());
    }

    public static void main(String[] args) {
        System.out.println("Welcome to Boggle!");

        // index words
        try {
            Scanner s = new Scanner(new File("./wordlist.txt"));
            while (s.hasNext()) validWords.add(s.nextLine());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // start GUI
        BoggleGUI.main(args);
    }

}
