package com.deelsilcon.posting_Index;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class Words implements Comparable<Words> {
    private String word;
    private int frequency;
    private List<Integer> docIds;

    public Words(String word, int frequency, LinkedList<Integer> docIds) {
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

    public List<Integer> getDocIds() {
        return docIds;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public void addToPostingIndex(int docId) {
        getDocIds().add(docId);
    }

    public String printDocIds() {
        StringBuilder sb = new StringBuilder();
        for (int docId : getDocIds()) {
            sb.append(docId);
            sb.append(" ");
        }
        return sb.toString();
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
