package com.deelsilcon.e6;

import java.util.HashMap;

/**
 * 作为文本对象；保存id, 词项:tf-idf键值对
 *
 * @author deelsilcon
 * @time: 2020/5/20
 */
public class VsmFile {
    /**
     * dict: 保存词项:tf-idf键值对
     * id: 保存文档id
     */
    private HashMap<String, Double> dict;
    private int id;

    public VsmFile() {
    }

    public VsmFile(HashMap<String, Double> dict, int id) {
        this.dict = dict;
        this.id = id;
    }

    public HashMap<String, Double> getDict() {
        return dict;
    }

    public void setDict(HashMap<String, Double> dict) {
        this.dict = dict;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "VsmFile{" +
                "dict=" + dict +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VsmFile)) {
            return false;
        }

        VsmFile vsmFile = (VsmFile) o;

        return id == vsmFile.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    /**
     * 为词项词典添加数据
     *
     * @param word  添加的词项
     * @param tfIdf tfidf值
     */

    public void addComponent(String word, double tfIdf) {
        dict.put(word, tfIdf);
    }

    /**
     * 对文档的向量进行单位化
     */
    public void normalize() {
        double sum = 0;
        for (String s : dict.keySet()) {
            double tfIdf = dict.get(s);
            sum += tfIdf * tfIdf;
        }
        double mod = Math.sqrt(sum);
        dict.replaceAll((s, v) -> v / mod);
    }

    /**
     * 计算两个文档的余弦相似度
     *
     * @param file1  进行相似度比较的文档
     * @param file2  进行相似度比较的文档
     * @param filter 设置参与比较分量的tf-idf下限
     * @return 两文档的相似度
     */
    public static double getSimilarity(Double filter, VsmFile file1, VsmFile file2) {
        file1.normalize();
        file2.normalize();
        double sum = 0;
        for (String s : file1.getDict().keySet()) {
            if (file2.getDict().containsKey(s)) {
                double tfIdf1 = file1.getDict().get(s);
                double tfIdf2 = file2.getDict().get(s);
                if (tfIdf1 > filter && tfIdf2 > filter) {
                    sum += tfIdf1 * tfIdf2;
                }
            }
        }
        return sum;
    }
}
