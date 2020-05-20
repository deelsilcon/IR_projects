package com.deelsilcon.e5;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * @author deelsilcon
 */
public class Words implements Comparable<Words> {
    private String word;
    private int frequency;
    private HashMap<Integer, Integer> docIds;

    public Words(String word, int frequency, HashMap<Integer, Integer> docIds) {
        this.word = word;
        this.frequency = frequency;
        this.docIds = docIds;
    }

    @Override
    public int hashCode() {
        return word.hashCode() + frequency + docIds.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Words)) {
            return false;
        }

        Words other = (Words) obj;
        return this.word.equals(other.word);
    }

    public String getWord() {
        return word;
    }

    public int getFrequency() {
        return frequency;
    }

    public Map<Integer, Integer> getDocIds() {
        return docIds;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }


    @Override
    public String toString() {
        return "Words{" +
                "word='" + word + '\'' +
                ", frequency=" + frequency +
                ", docIds=" + docIds +
                '}';
    }

    @Override
    public int compareTo(@NotNull Words other) {
        return Integer.compare(this.frequency, other.frequency);
    }
}
