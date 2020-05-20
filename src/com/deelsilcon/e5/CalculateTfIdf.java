package com.deelsilcon.e5;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author deelsilcon
 */
public class CalculateTfIdf {
    private Map<String, Words> wordsMap = new HashMap<>();
    private List<String> files = new ArrayList<>();
    private Map<Integer, VsmFile> fileMap = new HashMap<>();

    private void readFiles(String directoryPath) {
        File directory = new File(directoryPath);
        //读取所有文件
        File[] filesToProcessing = directory.listFiles();
        assert filesToProcessing != null;
        for (File file : filesToProcessing) {
            files.add(file.getName());
        }
        for (String fileName : files) {
            saveToDict(fileName, directoryPath);
        }
    }

    private void saveToDict(String fileName, String directoryPath) {
        //取出doc文档编号
        int docId = Integer.parseInt(Pattern.compile("[^0-9]").matcher(fileName).replaceAll("").trim());
        File file = new File(directoryPath + "/" + fileName);
        BufferedReader br;
        String curLine;
        try {
            br = new BufferedReader(new FileReader(file));
            while ((curLine = br.readLine()) != null) {
                String[] words = curLine.split(" ");
                for (String word : words) {
                    if (!wordsMap.containsKey(word.toLowerCase())) {
                        Words newWord = new Words(word.toLowerCase(), 1, new HashMap<>() {{
                            put(docId, 1);
                        }});
                        wordsMap.put(word.toLowerCase(), newWord);
                    } else {
                        int times = wordsMap.get(word.toLowerCase()).getDocIds().getOrDefault(docId, 0);
                        wordsMap.get(word.toLowerCase()).getDocIds().put(docId, times + 1);
                    }
                    int freq = wordsMap.get(word.toLowerCase()).getDocIds().size();
                    wordsMap.get(word.toLowerCase()).setFrequency(freq);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printDict() {
        System.out.println(wordsMap);
    }

    private void initFileMap(int N) {
        for (int i = 1; i <= N; i++) {
            fileMap.put(i, new VsmFile(new HashMap<>(), i));
        }
    }

    public double calTf(String term, int documentId) {
        int tf = wordsMap.get(term).getDocIds().get(documentId);
        return tf == 0 ? 0 : 1 + Math.log10(tf);
    }

    public double calIdf(String term, int N) {
        int df = wordsMap.get(term).getFrequency();
        return Math.log10((double) N / (double) df);
    }

    public double calTfAndIdf(String term, int documentId, int N) {
        return calTf(term, documentId) * calIdf(term, N);
    }

    public void printFileMap() {
        System.out.println(fileMap);
    }

    public void addToFile() {
        for (String s : wordsMap.keySet()) {
            Words word = wordsMap.get(s);
            for (int id : word.getDocIds().keySet()) {
                double tfIdf = calTfAndIdf(s, id, 10);
                fileMap.get(id).addComponent(s, tfIdf);
            }
        }
    }

    public void getFileSim(int N) {
        for (int i = 1; i <= N; i++) {
            for (int j = i + 1; j <= N; j++) {
                double sim = VsmFile.getSimilarity(fileMap.get(i), fileMap.get(j));
                System.out.println("The similarity between file " + i + "and file " + j + " is: " + sim);
            }
        }
    }

    public static void main(String[] args) {
        CalculateTfIdf ca = new CalculateTfIdf();
        System.out.println("Please enter the directory path");
        Scanner sc = new Scanner(System.in);
        String path = sc.nextLine();
        ca.readFiles(path);
        ca.initFileMap(10);
        ca.addToFile();
        ca.getFileSim(10);
    }
}
