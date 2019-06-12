import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


/*
   Assignment: Boggle Program

   Copyright 2019 Devin, Raz, Felix

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

/*
 * TODO LIST
 * Singleplayer counter should be total, and end
 * Wordlist include as resource
 */

/*
 * Class Name: Boggle
 * Description: Main utility class, with game central methods
 */

public class Boggle {
    
    // sets board size
    public static final int BOARD_SIZE = 5;

    // game variables (configurable)
    public static int minimumWordLength = 3, pointsToPlay = 100, numberOfPlayers = 2, currentPlayerIndex = 0, maxTimePerTurn = 20, roundsUntilAllowShakeBoard = 2;
    public static boolean highlightAsYouType = true, allowShakeBoard = true, allowDuplicateWordsBetweenPlayers = false;

    public static char[][] board = new char[BOARD_SIZE][BOARD_SIZE]; // the characters in each spot on the board
    public static ArrayList<Player> players = new ArrayList<>(); // the players in the game

    public static HashSet<String> validWords = new HashSet<>(); // store the list of valid words in a hashset for average constant time lookups ~O(1)

    // get player that currently has turn
    public static Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    // generate the board randomly using the specified dice
    public static void generateBoard() {
        // different dice that are possible
        ArrayList<String> dice = new ArrayList<>(Arrays.asList("AAAFRS", "AAEEEE", "AAFIRS", "ADENNN", "AEEEEM", "AEEGMU", "AEGMNN", "AFIRSY", "BJKQXZ", "CCNSTW", "CEIILT", "CEILPT", "CEIPST", "DDLNOR", "DHHLOR", "DHHNOT", "DHLNOR", "EIIITT", "EMOTTT", "ENSSSU", "FIPRSY", "GORRVW", "HIPRRY", "NOOTUW", "OOOTTU"));

        // loop over each board piece
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                // choose a random dice, and take a random face, and put it on the board
                int ind = (int) (Math.random() * dice.size());
                board[i][j] = dice.get(ind).charAt((int) (Math.random() * dice.get(ind).length()));
                dice.remove(ind); // don't repeat dice
            }
        }
    }

    private static boolean recursiveBoggleFind(int x, int y, boolean[][] visited, int searchIndex, String str) { //recursively use the search index to check which locations have been visited on the board
        if (searchIndex == str.length()) return true;
        visited[x][y] = true;

        int[][] boardDirection = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1}};

        // loop over possible directions on the board
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

    private static boolean findBoggleWord(String str) { //Find if the words exists on the board
        str = str.toUpperCase();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) { //
                boolean[][] visited = new boolean[BOARD_SIZE][BOARD_SIZE];
                visited[i][j] = true;
                // start at neighbouring character
                if (recursiveBoggleFind(i, j, visited, 0, str)) return true; //helper method for findBoggleWord
            }
        }
        return false;
    }

    private static int getGuessWordsPoints(String word) { //Use the word length to decide what would be the score that the user gets for a certain word
        int score = 0;
        if (word.length() >= minimumWordLength && validWords.contains(word.toLowerCase()) && findBoggleWord(word)) {
            score = word.length();
        }
        return score;
    }

    // executed once the player clicks the submit button in the game scene
    public static void handleTurn(String word) {
        Player p = getCurrentPlayer();

        // checks

        boolean isUsed = false;
        if (!Boggle.allowDuplicateWordsBetweenPlayers) {
            for (Player ps : Boggle.players) {
                if (ps.isUsedWord(word)) {
                    isUsed = true;
                    break;
                }
            }
        }

        if (p.isUsedWord(word) || isUsed) {
            System.out.println("Word already used!");
            GameScene.showModal("Word already used!\nTry again!", 1, ()->{});
            BoggleGUI.playSound("nope.wav");
            return;
        }

        int points = getGuessWordsPoints(word);
        if (points == 0) {
            BoggleGUI.playSound("nope.wav");
            return;
        }

        BoggleGUI.playSound("shine.wav");

        p.setScore(p.getScore() + points);
        p.addUsedWord(word);

        System.out.println("Handling " + p.getName() + "'s turn.");
        System.out.println("Score: " + p.getScore());

        if (p.getScore() >= pointsToPlay) {
            winGame(p);
        } else {
            nextTurn();
        }
    }

    public static void winGame(Player p) { //When a player wins the game
        BoggleGUI.playSound("impact.wav");
        System.out.println(p.getName() + " won!");
        BoggleGUI.stage.setScene(WinScene.getScene(p));
        players.clear();
    }

    public static void nextTurn() {  //go to the next turn of the next player
        currentPlayerIndex++;
        if( currentPlayerIndex == numberOfPlayers) {
            GameScene.currentRound++;
            currentPlayerIndex = 0;
        }
        System.out.println("Switching to " + getCurrentPlayer().getName() + "'s turn.");
        BoggleGUI.stage.setScene(GameScene.getScene()); //Set the stage for the next player
    }

    public static void startGame() {
        generateBoard();

        currentPlayerIndex = 0;
        GameScene.currentRound = 1;

        BoggleGUI.stage.setScene(GameScene.getScene());
    }

    public static void main(String[] args) {
        System.out.println("Welcome to Boggle!");

        // index words in our hashset
        try {
            File file = new File("./wordlist.txt");
            if (!file.exists()) {
                file = new File(Boggle.class.getResource("wordlist.txt").getFile());
            }

            Scanner s = new Scanner(file);
            while (s.hasNext())
                validWords.add(s.nextLine());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // start GUI
        BoggleGUI.main(args);
    }

    // helper function to do thread waiting
    public static void threadSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
