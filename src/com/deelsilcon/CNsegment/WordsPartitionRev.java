package com.deelsilcon.CNsegment;

import java.io.*;
import java.util.*;

/**
 * A simple  greedy method to implement Chinese words partition with the corpus;
 */
public class WordsPartitionRev {
    private Set<String> dict = new HashSet<>();
    private List<String> content = new ArrayList<>();
    private LinkedList<String> res = new LinkedList<>();

    /**
     * @param path the dict we want to open and read
     */
    private void readDict(String path) {
        File file = new File(path);
        BufferedReader br = null;
        String curLine = null;
        int line = 0;
        try {
            br = new BufferedReader(new FileReader(file));
            while ((curLine = br.readLine()) != null) {
                line++;
                if (line == 1) continue;
                dict.add(curLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param path the File's path
     */
    private void readFile(String path) {
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
     * @param toPath The destination path
     */
    private void writeFile(String toPath) {
        File fileTo = new File(toPath);
        if (!fileTo.exists()) {
            for (String str : res) {
                FileOutputStream fo = null;
                PrintStream ps = null;
                try {
                    fo = new FileOutputStream(fileTo, true);
                    ps = new PrintStream(fo);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                assert ps != null;
                ps.print(str);
                ps.close();
            }
        }
    }

    /**
     * Main function to implement partition;
     */
    public void wordsPartitionRev() {
        for (String s : content) {
            int curLenLeft = s.length();
            while (curLenLeft != 0) {
                int maxSz = Math.min(curLenLeft, 10);
                int start = curLenLeft - maxSz;
                String subString = s.substring(start, curLenLeft);
                while (!dict.contains(subString) && subString.length() > 1) {
                    start++;
                    subString = s.substring(start, curLenLeft);
                }
                res.addFirst(subString + " ");
                curLenLeft -= subString.length();
            }
        }
        res.add("\n");
    }

    /**
     * Drive function;  m
     *
     * @param src    source file to partition
     * @param dict   corpus
     * @param toFile Destination file path
     */
    public void doPartition(String src, String dict, String toFile) {
        readFile(src);
        readDict(dict);
        wordsPartitionRev();
        writeFile(toFile);
    }

    public static void main(String[] args) throws IOException {
        WordsPartition wp = new WordsPartition();
        String src = "D:\\IR\\src\\com\\deelsilcon\\CNsegment\\corpus.sentence.txt";
        String dict = "D:\\IR\\src\\com\\deelsilcon\\CNsegment\\corpus.dict.txt";
        String toFile = "D:\\out3.txt";
        wp.doPartition(src, dict, toFile);
    }
}