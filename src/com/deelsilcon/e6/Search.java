package com.deelsilcon.e6;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * 进行大规模向量空间的检索比较
 *
 * @author deelsilcon
 * @time: 2020/5/20
 */
public class Search {
    /**
     * wordsMap: 储存词项本身和对应的词项对象；
     * files: 储存所有文件名
     * fileMap: 储存文档编号和对应的文档对象
     */
    private final Map<String, Words> wordsMap = new HashMap<>();
    private final List<String> files = new ArrayList<>();
    private final Map<Integer, VsmFile> fileMap = new HashMap<>();
    private static final double EPSILON = 1e-6;

    /**
     * 类的实际逻辑方法
     */
    public static void doQuery() {
        Scanner sc = new Scanner(System.in);
        long start = System.currentTimeMillis();
        Search search = new Search();
        System.out.println("Please enter the file path");
        String filePath = sc.nextLine();
        int size = search.processFile(filePath);
        search.initFileMap(size);
        search.addToFileMap(size);
        long end = System.currentTimeMillis();
        System.out.println("In " + (double) (end - start) / 1000 + " seconds, processing " + (size - 1) + "files");
        System.out.println("Please enter the query sentence: ");
        String query = sc.nextLine();
        VsmFile vsmFile = search.changeQueryToVsmFile(query, size);
        search.showResult(size, vsmFile);
    }


    /**
     * 计算tf
     *
     * @param term       词项
     * @param documentId 文档Id
     * @return 词项对应某篇文档的Tf值
     */
    public double calTf(String term, int documentId) {
        int tf = wordsMap.get(term).getDocIds().get(documentId);
        return tf == 0 ? 0 : 1 + Math.log10(tf);
    }

    /**
     * 计算idf
     *
     * @param term 词项
     * @param size 文档集大小
     * @return 词项的Idf值
     */
    public double calIdf(String term, int size) {
        int df = wordsMap.get(term).getFrequency();
        return Math.log10((double) size / (double) df);
    }

    /**
     * 计算tf-idf
     *
     * @param term       词项
     * @param documentId 文档Id
     * @param size       文档集大小
     * @return 词项的tf-idf值
     */
    public double calTfAndIdf(String term, int documentId, int size) {
        return calTf(term, documentId) * calIdf(term, size);
    }

    /**
     * 添加文件但文件Map中
     *
     * @param size 文档集大小
     */
    public void addToFileMap(int size) {
        for (String s : wordsMap.keySet()) {
            Words word = wordsMap.get(s);
            for (int id : word.getDocIds().keySet()) {
                double tfIdf = calTfAndIdf(s, id, size);
                fileMap.get(id).addComponent(s, tfIdf);
            }
        }
    }

    /**
     * 将查询语句转换为一个向量空间模型文档对象
     *
     * @param query 查询语句
     * @param size  文档集大小
     * @return 查询语句对应的向量空间模型文档对象
     */
    private VsmFile changeQueryToVsmFile(String query, int size) {
        String[] words = query.split(" ");
        VsmFile vsmFile = new VsmFile();
        vsmFile.setDict(new HashMap<String, Double>(20, 0.75f));
        for (String s : words) {
            String sInLowerCase = s.toLowerCase();
            if (wordsMap.containsKey(sInLowerCase)) {
                double idf = calIdf(sInLowerCase, size);
                double tf = 1.0;
                double tfIdf = idf * tf;
                vsmFile.addComponent(sInLowerCase, tfIdf);
            }
        }
        return vsmFile;
    }

    /**
     * 输出匹配得分高的文档结果
     *
     * @param size 文档集大小
     * @param file 查询对应的VSM文档对象
     */
    private void showResult(int size, VsmFile file) {
        TreeMap<Double, Integer> map = new TreeMap<>();
        int count = 0;
        for (int i = 1; i <= size; i++) {
            double sim = VsmFile.getSimilarity(0.0, fileMap.get(i), file);
            if (Math.abs(sim - 0.0) > EPSILON) {
                count++;
            }
            map.put(sim, i);
        }
        System.out.println("SCORE\t\tFILE_ID ");
        if (count == 0) {
            System.out.println("null\t-1");
        } else {
            List<Double> keyLs = new ArrayList<>(map.keySet());
            keyLs.sort(Collections.reverseOrder());
            for (int i = 0; i < Math.min(5, count); i++) {
                double d = keyLs.get(i);
                System.out.println(d + "\t" + map.get(d));
            }
        }
    }

    /**
     * 读取并处理词项文件
     *
     * @param path 文件路径
     * @return 文件中包含的文档数目
     */
    private int processFile(String path) {
        File file = new File(path);
        BufferedReader br = null;
        String curLine;
        int curId = 1;
        try {
            br = new BufferedReader(new FileReader(file));
            while ((curLine = br.readLine()) != null) {
                String[] wordsInFile = curLine.split(" ");
                for (String s : wordsInFile) {
                    String sInLowercase = s.toLowerCase();
                    if (!wordsMap.containsKey(sInLowercase)) {
                        int finalCurId = curId;
                        Words newWord = new Words(sInLowercase, 1, new HashMap<Integer, Integer>(30, 0.75f) {{
                            put(finalCurId, 1);
                        }});
                        wordsMap.put(sInLowercase, newWord);
                    } else {
                        int times = wordsMap.get(sInLowercase).getDocIds().getOrDefault(curId, 0);
                        wordsMap.get(sInLowercase).getDocIds().put(curId, times + 1);
                    }
                    int freq = wordsMap.get(sInLowercase).getDocIds().size();
                    wordsMap.get(sInLowercase).setFrequency(freq);
                }
                curId++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return curId;
    }

    /**
     * 初始化文档Map
     *
     * @param size 文档集大小
     */
    private void initFileMap(int size) {
        for (int i = 1; i <= size; i++) {
            fileMap.put(i, new VsmFile(new HashMap<>(), i));
        }
    }


    public static void main(String[] args) {
        doQuery();
    }
}


