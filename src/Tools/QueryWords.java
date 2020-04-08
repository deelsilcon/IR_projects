package Tools;

import java.io.*;
import java.util.*;

/**
 * This is a program to return all the words' frequency in a text by calculate
 * their times showing in the text;
 */
public class QueryWords {
    /**
     * @param filename The source file path
     * @param word     The word you search for
     * @author xyh;
     */
    public static void query(String filename, String word) {
        Map<String, Integer> hashMap = new HashMap<>();
        /*
        read the file;
         */
        File file = new File(filename);
        List<String> fileContent = new ArrayList<>();
        BufferedReader br = null;
        String cur = null;
        try {
            br = new BufferedReader(new FileReader(file));
            while ((cur = br.readLine()) != null) {
                fileContent.add(cur);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
        save all the words and it's times in the text into a map
         */
        for (String s : fileContent) {
            String[] lineWords = s.toLowerCase().split("[^a-z]+");
            Set<String> wordSet = hashMap.keySet();
            for (int i = 0; i < lineWords.length; i++) {
                if (wordSet.contains(lineWords[i])) {
                    Integer number = hashMap.get(lineWords[i]);
                    number++;
                    hashMap.put(lineWords[i], number);
                } else {
                    hashMap.put(lineWords[i], 1);
                }
            }
        }


        /*
        save the file;
         */
        File fileTo = new File("D:/IR/dict.index");
        if (!fileTo.exists()) {
            for (String str : hashMap.keySet()) {
                FileOutputStream fo = null;
                PrintStream ps = null;
                try {
                    fo = new FileOutputStream(fileTo, true);
                    ps = new PrintStream(fo);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ps.print(str + "\t" + hashMap.get(str) + "\n");
                ps.close();
            }
        }
        int times = hashMap.getOrDefault(word, 0);
        if (times == 1 || times == 0) {
            System.out.println("The word you search for shows " + times + " time.");
        } else {
            System.out.println("The word you search for shows " + times + " times.");
        }
        System.out.println("file has been saved at D:/IR/dict.index");
    }

    public static void main(String[] args) {
        QueryWords.query(args[0], args[1]);
    }
}
