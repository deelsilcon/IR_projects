package com.deelsilcon.CNsegment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is the test of out word partition;
 */
public class WordsPartitionTest {
    private int total = 0;
    private int partitionTotal = 0;
    private int rightMatchTotal = 0;
    private double precision;
    private double recall;

    /**
     * @param outPath Our file path;
     * @param refPath The correct reference answer's path
     * @return F value;
     */
    public double testResult(String outPath, String refPath) {
        List<String> fileContent1 = new ArrayList<>();
        List<String> fileContent2 = new ArrayList<>();
        readFile(outPath, fileContent1);
        readFile(refPath, fileContent2);
        for (int i = 0; i < fileContent1.size(); i++) {
            String[] s1 = fileContent1.get(i).split(" ");
            String[] s2 = fileContent2.get(i).split(" ");
            compare(s1, s2);
        }
        precision = (double) rightMatchTotal / (double) partitionTotal;
        recall = (double) rightMatchTotal / (double) total;
        return precision * recall * 2 / (precision + recall);
    }

    /**
     * @param path    The file path
     * @param content The List to store file content
     */
    private void readFile(String path, List<String> content) {
        File file = new File(path);
        BufferedReader br = null;
        String curLine = null;
        try {
            br = new BufferedReader(new FileReader(file));
            while ((curLine = br.readLine()) != null) {
                content.add(curLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param s1 string to compare
     * @param s2 string to compare
     */
    private void compare(String[] s1, String[] s2) {
        List<String> out = putInList(s1);
        List<String> ref = putInList(s2);
        int rightMatch = 0;
        int curPosOut = 0;
        int curPosRef = 0;
        int i = 0, j;
        while (i < out.size()) {
            if (i == ref.size())
                break;
            if (curPosOut < curPosRef) {
                String s = out.get(i);
                curPosOut += s.length();
                i++;
            } else if (curPosOut > curPosRef) {
                j = i;
                while (curPosOut > curPosRef) {
                    curPosRef += ref.get(j++).length();
                }
            } else {
                if (out.get(i).equals(ref.get(i))) {
                    curPosOut += out.get(i).length();
                    curPosRef += out.get(i).length();
                    rightMatch++;
                }
                i++;
            }
        }
        rightMatchTotal += rightMatch;
        partitionTotal += s1.length;
        total += s2.length;
    }

    /**
     * @param words The words to put in
     * @return List contains all the words in the String[]
     */
    private List<String> putInList(String[] words) {
        return new ArrayList<>(Arrays.asList(words));
    }

    public static void main(String[] args) {
        WordsPartitionTest wp = new WordsPartitionTest();
        double f = wp.testResult(args[0], args[1]);
        System.out.printf("The precision: is %2.2f%% \n", wp.precision * 100);
        System.out.printf("The recall is: %2.2f%% \n", wp.recall * 100);
        System.out.printf("The F value is: %2.2f%% ", f * 100);
    }
}
