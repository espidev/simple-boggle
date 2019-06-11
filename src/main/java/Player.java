import java.util.HashSet;

public class Player implements Comparable<Player> {

    private int score;                                      // creating variable to store individual player score
    private String name;                                    // creating variable to store individual player name
    private HashSet<String> usedWords;                      // creating HashSet to store each individual players used words

    public Player (String name) {
        this.name = name;                                   // initializes player name
        score = 0;                                          // initializes player score
        usedWords = new HashSet<>();                        // initializes player usedwords
    }

    public int getScore() {
        return score;                                       // returns players score when called
    }

    public String getName() {
        return name;                                        // returns players name when called
    }

    public boolean isUsedWord(String str) {
        return usedWords.contains(str);                     // returns players list of used words when called
    }

    public void setScore(int score) {
        this.score = score;                                 // sets players score when called
    }

    public void setName(String name) {
        this.name = name;                                   // sets players name when called
    }

    public void addUsedWord(String word) {
        usedWords.add(word);                                // sets players used words when called (adds the word)
    }

    @Override
    public int compareTo(Player p) {
        return getScore() - p.getScore();
    }
}
