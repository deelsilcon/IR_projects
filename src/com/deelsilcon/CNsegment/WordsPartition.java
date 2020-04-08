package com.deelsilcon.CNsegment;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple  greedy method to implement Chinese words partition with the corpus;
 */
public class WordsPartition {
    private Set<String> dict = new HashSet<>();
    private List<String> content = new ArrayList<>();
    private List<String> res = new ArrayList<>();

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
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
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
    private void writeFile(String toPath) throws IOException {
        File fileTo = new File(toPath);
        if (!fileTo.exists()) {
            for (String str : res) {
                FileOutputStream fo = null;
                PrintStream ps = null;
                try {
                    fo = new FileOutputStream(fileTo, true);
                    ps = new PrintStream(fo, true, StandardCharsets.UTF_8);
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
    public void wordsPartition() {
        for (String s : content) {
            int length = s.length();
            int curLen = 0;
            while (curLen != length) {
                int maxSz = Math.min(length - curLen, 10);
                while (true) {
                    if (maxSz == 1) {
                        res.add(s.substring(curLen, curLen + 1) + " ");
                        curLen++;
                        break;
                    }
                    String subs = s.substring(curLen, curLen + maxSz);
                    if (dict.contains(subs)) {
                        res.add(subs + " ");
                        curLen += maxSz;
                        break;
                    } else {
                        maxSz--;
                    }
                }
            }
            res.add("\n");
        }
    }

    /**
     * Drive function;  m
     *
     * @param src    source file to partition
     * @param dict   corpus
     * @param toFile Destination file path
     */
    public void doPartition(String src, String dict, String toFile) throws IOException {
        readFile(src);
        readDict(dict);
        wordsPartition();
        writeFile(toFile);
    }

    public static void main(String[] args) throws IOException {
        WordsPartition wp = new WordsPartition();
        String src = args[0];
        String dict = args[1];
        File file = new File(src);
        String path = file.getParent();
        String out = path + "/corpus.out.txt";
        wp.doPartition(src, dict, out);

    }
}



