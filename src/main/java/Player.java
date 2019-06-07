import java.util.HashSet;

public class Player implements Comparable<Player> {

    private int score;
    private String name;
    private HashSet<String> usedWords;

    public Player (String name) {
        this.name = name;
        score = 0;
        usedWords = new HashSet<>();
    }

    public int getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    public boolean isUsedWord(String str) {
        return usedWords.contains(str);
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addUsedWord(String word) {
        usedWords.add(word);
    }

    @Override
    public int compareTo(Player p) {
        return getScore() - p.getScore();
    }
}
