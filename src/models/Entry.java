package models;
import java.io.Serializable;
import java.util.List;

/**
 * Created by tomek on 29.04.14.
 */
public class Entry implements Serializable, IEntry {

    private String word;
    private List<String> clues;


    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }


    public List<String> getClues() {
        return clues;
    }

    public void setClues(List<String> clues) {
        this.clues = clues;
    }
}