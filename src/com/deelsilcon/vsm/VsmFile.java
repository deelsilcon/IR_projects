package com.deelsilcon.vsm;

import java.util.HashMap;

/**
 * @author deelsilcon
 */
public class VsmFile {
    private HashMap<String, Double> dict;
    private int id;

    public VsmFile(HashMap<String, Double> dict, int id) {
        this.dict = dict;
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

    public int getId() {
        return id;
    }


    public HashMap<String, Double> getDict() {
        return dict;
    }

    public void addComponent(String word, double tfIdf) {
        dict.put(word, tfIdf);
    }

    public void normalize() {
        double sum = 0;
        for (String s : dict.keySet()) {
            double tfIdf = dict.get(s);
            sum += tfIdf * tfIdf;
        }
        double mod = Math.sqrt(sum);
        dict.replaceAll((s, v) -> v / mod);
    }

    public double getSimilarity(VsmFile other) {
        this.normalize();
        other.normalize();
        double sum = 0;
        for (String s : dict.keySet()) {
            if (other.getDict().containsKey(s)) {
                double tfIdf1 = dict.get(s);
                double tfIdf2 = other.getDict().get(s);
                sum += tfIdf1 * tfIdf2;
            }
        }
        return sum;
    }

    public static double getSimilarity(VsmFile file1, VsmFile file2) {
        file1.normalize();
        file2.normalize();
        double sum = 0;
        for (String s : file1.getDict().keySet()) {
            if (file2.getDict().containsKey(s)) {
                double tfIdf1 = file1.getDict().get(s);
                double tfIdf2 = file2.getDict().get(s);
                sum += tfIdf1 * tfIdf2;
            }
        }
        return sum;
    }
}
