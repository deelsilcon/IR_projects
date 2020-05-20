package com.deelsilcon.e6;

import java.util.HashMap;


/**
 * 创建该类作为文本中的对象；保存tf-idf计算的数据
 *
 * @author deelsilcon
 * @time: 2020/5/20
 */
public class Words {
    /**
     * word: 该词项本身
     * frequency: 词项出现的次数, 用来计算idf值
     * docIds: 词项出现在某个文本的次数, 用来计算tf值
     */
    private String word;
    private int frequency;
    private HashMap<Integer, Integer> docIds;

    public Words(String word, int frequency, HashMap<Integer, Integer> docIds) {
        this.word = word;
        this.frequency = frequency;
        this.docIds = docIds;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public HashMap<Integer, Integer> getDocIds() {
        return docIds;
    }

    public void setDocIds(HashMap<Integer, Integer> docIds) {
        this.docIds = docIds;
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
}
