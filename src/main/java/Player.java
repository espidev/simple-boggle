import java.util.HashSet;

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
 * Class: Player
 * Description: Object to represent each individual player ingame
 */

public class Player implements Comparable<Player> {

    private int score; // creating variable to store individual player score
    private String name; // creating variable to store individual player name
    private HashSet<String> usedWords; // creating HashSet to store each individual players used words

    // constructor
    public Player (String name) {
        this.name = name;
        score = 0;
        usedWords = new HashSet<>();
    }

    public int getScore() {
        return score; // returns players score when called
    }

    public String getName() {
        return name; // returns players name when called
    }

    public boolean isUsedWord(String str) {
        return usedWords.contains(str); // returns players list of used words when called
    }

    public void setScore(int score) {
        this.score = score; // sets players score when called
    }

    public void setName(String name) {
        this.name = name; // sets players name when called
    }

    public void addUsedWord(String word) {
        usedWords.add(word); // sets players used words when called (adds the word)
    }

    public HashSet<String> getUsedWords() {
        return usedWords; //return used words
    }

    // custom comparator for Collections.sort()
    @Override
    public int compareTo(Player p) {
        return getScore() - p.getScore();
    }
}
