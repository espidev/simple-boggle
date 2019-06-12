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
 * Class Name: Boggle
 * Description: Main utility class, with game central methods
 */

public class Boggle {

    // sets board size
    public static final int BOARD_SIZE = 5;

    // game variables (configurable)
    public static int minimumWordLength = 3, pointsToPlay = 20, numberOfPlayers = 2, currentPlayerIndex = 0, maxTimePerTurn = 20, roundsUntilAllowShakeBoard = 2;
    public static boolean highlightAsYouType = true, allowShakeBoard = true, allowDuplicateWordsBetweenPlayers = false;

    public static char[][] board = new char[BOARD_SIZE][BOARD_SIZE]; // the characters in each spot on the board
    public static ArrayList<Player> players = new ArrayList<>(); // the players in the game

    public static HashSet<String> validWords = new HashSet<>(); // store the list of valid words in a hashset for average constant time lookups ~O(1)

    // get player that currently has turn
    public static Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    // binary search helper method
    // proof of concept
    public static boolean binarySearch(List<Integer> list, int find) {
        int l = 0, r = list.size(), i = list.size() / 2;
        while (l <= r) {
            if (list.get(i) == find) {
                return true;
            } else if (list.get(i) < find) {
                l = (l + r) / 2 + 1;
            } else {
                r = (l + r) / 2 - 1;
            }
        }
        return false;
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
        if (searchIndex == str.length()) return true; // base case: leave when the word length is correct
        visited[x][y] = true; // don't repeat tiles

        // possible directions to go to
        int[][] boardDirection = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1}};

        // loop over possible directions on the board
        for (int[] direct : boardDirection) {
            int nx = x + direct[0], ny = y + direct[1];
            if (nx >= BOARD_SIZE || nx < 0 || ny >= BOARD_SIZE || ny < 0) continue; // check out of bounds

            // check if not visited, correct character, and the recursive search returns true (found)
            if (!visited[nx][ny] && (board[nx][ny] == str.charAt(searchIndex)) && recursiveBoggleFind(nx, ny, visited, searchIndex + 1, str)) {
                return true;
            }
        }
        // did not find anything
        return false;
    }

    // find if the word exists on the board
    private static boolean findBoggleWord(String str) {
        str = str.toUpperCase(); // uppercase to match characters on board

        // loop over each board tile to recursively find
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                // keep track of visited tiles
                boolean[][] visited = new boolean[BOARD_SIZE][BOARD_SIZE];
                visited[i][j] = true;
                // start at neighbouring character
                if (recursiveBoggleFind(i, j, visited, 0, str)) {
                    return true; // return true if recursive search yields results
                }

            }
        }
        return false;
    }

    // determine the amount of points that can be obtained with the word
    private static int getGuessWordsPoints(String word) {
        int score = 0;

        // score will only be added if it is above or equal to minimum word length, is a valid dictionary word
        // and findBoggleWord yields results

        // NOTE: validWords.contains is on a HashSet, which uses a tree to find if the word is contained
        // using logarithmic complexity
        if (word.length() >= minimumWordLength && validWords.contains(word.toLowerCase()) && findBoggleWord(word)) {
            score = word.length();
        }
        return score;
    }

    // executed once the player clicks the submit button in the game scene
    public static void handleTurn(String word) {
        Player p = getCurrentPlayer();

        // check if the word has already been used by other players (if the option is enabled)
        boolean isUsed = false;
        if (!Boggle.allowDuplicateWordsBetweenPlayers) {
            for (Player ps : Boggle.players) { // loop through players
                if (ps.isUsedWord(word)) {
                    isUsed = true;
                    break;
                }
            }
        }

        // check if player has used the word already, or others have
        if (p.isUsedWord(word) || isUsed) {
            System.out.println("Word already used!");
            GameScene.showModal("Word already used!\nTry again!", 1, () -> {
            });
            BoggleGUI.playSound("nope.wav");
            return; // exit if word has been used and show modal
        }

        int points = getGuessWordsPoints(word); // use helper function to get points worth
        if (points == 0) { // if function returned zero, the word is invalid so exit
            BoggleGUI.playSound("nope.wav");
            return;
        }

        // otherwise, prepare for next turn

        BoggleGUI.playSound("shine.wav");

        p.setScore(p.getScore() + points);
        p.addUsedWord(word);

        System.out.println("Handling " + p.getName() + "'s turn.");
        System.out.println("Score: " + p.getScore());

        if (p.getScore() >= pointsToPlay) { // if player has enough points to win, win
            winGame(p);
        } else { // otherwise, go to next player's turn
            nextTurn();
        }
    }

    // helper function to dictate win behaviour
    public static void winGame(Player p) {
        BoggleGUI.playSound("impact.wav");
        System.out.println(p.getName() + " won!");
        BoggleGUI.stage.setScene(WinScene.getScene(p)); // change to win scene
        players.clear();
    }

    // go to the next player's turn
    public static void nextTurn() {
        currentPlayerIndex++; // increment current player
        if (currentPlayerIndex == numberOfPlayers) { // if it's out of bounds, go to beginning of list
            GameScene.currentRound++; // going to beginning means it's a new round (pass)
            currentPlayerIndex = 0;
        }
        System.out.println("Switching to " + getCurrentPlayer().getName() + "'s turn.");
        BoggleGUI.stage.setScene(GameScene.getScene()); // set the game scene for the next player
    }

    // utility function to start a new game
    // called by GUI when setup is complete
    public static void startGame() {
        generateBoard();

        // reset variables
        currentPlayerIndex = 0;
        GameScene.currentRound = 1;

        // start the first player's turn
        BoggleGUI.stage.setScene(GameScene.getScene());
    }

    // entry point to program
    public static void main(String[] args) {
        System.out.println("Welcome to Boggle!");

        // load dictionary words from file into our hashset
        // builds a tree to query "contains" function in constant time
        try {
            // reads from wordlist.txt
            File file = new File("./wordlist.txt");
            if (!file.exists()) { // if it isn't found, use program's builtin wordlist.txt as substitute
                file = new File(Boggle.class.getResource("wordlist.txt").getFile());
            }

            // read the wordlist line by line
            Scanner s = new Scanner(file);
            while (s.hasNext()) { // loop over and add each line to words
                validWords.add(s.nextLine());
            }

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
