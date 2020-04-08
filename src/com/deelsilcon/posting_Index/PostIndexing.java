package com.deelsilcon.posting_Index;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

class Words implements Comparable<Words> {
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

public class PostIndexing {
    private List<String> files = new ArrayList<>();
    private Map<String, Words> wordsMap = new HashMap<>();
    private List<Integer> allIds = new ArrayList<>();

    private void readFiles(String directoryPath) {
        File directory = new File(directoryPath);
        File[] filesToProcessing = directory.listFiles();
        assert filesToProcessing != null;
        for (File file : filesToProcessing) {
            files.add(file.getName());
        }
        for (String fileName : files) {
            Map<String, Integer> dict = saveToDict(fileName, directoryPath);
            addToWords(dict);
        }
    }

    private Map<String, Integer> saveToDict(String fileName, String directoryPath) {
        int docId = Integer.parseInt(Pattern.compile("[^0-9]").matcher(fileName).replaceAll("").trim());
        allIds.add(docId);
        File file = new File(directoryPath + "/" + fileName);
        BufferedReader br = null;
        String curLine = null;
        Map<String, Integer> dict = new HashMap<>();
        try {
            br = new BufferedReader(new FileReader(file));
            while ((curLine = br.readLine()) != null) {
                String[] words = curLine.split(" ");
                for (String word : words) {
                    if (!dict.containsKey(word.toLowerCase())) {
                        dict.put(word.toLowerCase(), docId);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dict;
    }

    private void addToWords(@NotNull Map<String, Integer> dict) {
        for (String s : dict.keySet()) {
            int docId = dict.get(s);
            Words w = new Words(s, 0, new LinkedList<>());
            w.addToPostingIndex(docId);
            if (!wordsMap.containsKey(s)) {
                wordsMap.put(s, w);
            } else {
                wordsMap.get(s).addToPostingIndex(docId);
            }
        }

        for (String word : wordsMap.keySet()) {
            Words wordInMap = wordsMap.get(word);
            wordInMap.setFrequency(wordInMap.getDocIds().size());
            Collections.sort(wordInMap.getDocIds());
        }
    }

    private void writeToFile(String pathTo) {
        File fileTo = new File(pathTo);
        if (!fileTo.exists()) {
            for (String words : wordsMap.keySet()) {
                FileOutputStream fos;
                PrintStream ps = null;
                try {
                    fos = new FileOutputStream(fileTo, true);
                    ps = new PrintStream(fos, true, StandardCharsets.UTF_8);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                assert ps != null;
                ps.println(words + "\t" + wordsMap.get(words).getFrequency() + "\t" + wordsMap.get(words).printDocIds());
                ps.close();
            }
        }
    }


    private List<Integer> mergeTwo(@NotNull List<Integer> ls1, List<Integer> ls2) {
        List<Integer> ret = new LinkedList<>();
        int cur1 = 0, cur2 = 0;
        while (cur1 <= ls1.size() - 1 && cur2 <= ls2.size() - 1) {
            int docId1 = ls1.get(cur1);
            int docId2 = ls2.get(cur2);
            if (docId1 == docId2) {
                ret.add(docId1);
                cur1++;
                cur2++;
            } else if (docId1 > docId2) {
                cur2++;
            } else {
                cur1++;
            }
        }
        return ret;
    }

    private List<Integer> merge(@NotNull String... words) {
        List<Words> wordsList = new ArrayList<>();
        for (String s : words) {
            wordsList.add(wordsMap.get(s));
        }
        Collections.sort(wordsList);
        System.out.println(wordsList);
        List<Integer> ret = wordsList.get(0).getDocIds();
        int curIndex = 1;
        while (curIndex < wordsList.size()) {
            ret = mergeTwo(wordsList.get(curIndex).getDocIds(), ret);
            curIndex++;
        }
        return ret;
    }


    private List<Integer> addTwo(List<Integer> ls1, List<Integer> ls2) {
        List<Integer> ret = new LinkedList<>();
        int cur1 = 0, cur2 = 0;
        while (cur1 < ls1.size() && cur2 < ls2.size()) {
            int docId1 = ls1.get(cur1);
            int docId2 = ls2.get(cur2);
            if (docId1 > docId2) {
                ret.add(docId2);
                cur2++;
            } else if (docId1 == docId2) {
                ret.add(docId1);
                cur1++;
                cur2++;
            } else {
                ret.add(docId1);
                ;
                cur1++;
            }
        }
        while (cur1 < ls1.size()) {
            ret.add(ls1.get(cur1));
            cur1++;
        }
        while (cur2 < ls2.size()) {
            ret.add(ls2.get(cur2));
            cur2++;
        }
        return ret;
    }

    private List<Integer> add(@NotNull String... words) {
        List<Words> wordsList = new ArrayList<>();
        for (String s : words) {
            wordsList.add(wordsMap.get(s));
        }
        List<Integer> ret = wordsList.get(0).getDocIds();
        int curIndex = 1;
        while (curIndex < wordsList.size()) {
            ret = addTwo(wordsList.get(curIndex).getDocIds(), ret);
            curIndex++;
        }
        return ret;
    }

    private Words complementList(@NotNull Words w) {
        LinkedList<Integer> ls = new LinkedList<>(allIds);
        ls.removeAll(w.getDocIds());
        Collections.sort(ls);
        return new Words(w.getWord(), w.getFrequency(), ls);
    }

    private LinkedList<Integer> complementList(List<Integer> ls) {
        LinkedList<Integer> res = new LinkedList<>(allIds);
        res.removeAll(ls);
        Collections.sort(res);
        return res;
    }

    private LinkedList<String> toPostExpression(@NotNull String[] querySentence) {
        Map<String, Integer> precedence = new HashMap<>() {{
            put("(", 1);
            put("not", 2);
            put("and", 3);
            put("or", 3);
        }};
        LinkedList<String> stack = new LinkedList<>();
        LinkedList<String> res = new LinkedList<>();

        for (String s : querySentence) {
            if (!precedence.containsKey(s)) {
                res.add(s);

            } else {
                if (stack.isEmpty()) {
                    stack.addLast(s);
                } else if (s.equals(")")) {
                    while (!stack.getLast().equals("(")) {
                        res.addLast(stack.pollLast());
                    }
                    res.addLast(stack.pollLast());
                    stack.addLast(")");
                } else if (precedence.get(s) >= precedence.get(stack.getLast())) {
                    while (!stack.isEmpty() && precedence.get(s) <= precedence.get(stack.getLast())) {
                        res.addLast(stack.pollLast());
                    }
                    stack.addLast(s);
                } else {
                    stack.addLast(s);
                }
            }
        }
        while (!stack.isEmpty()) {
            res.addLast(stack.pollLast());
        }
        return res;
    }

    private List<Integer> calculatePostExpression(@NotNull LinkedList<String> postExpression) {
        LinkedList<List<Integer>> stack = new LinkedList<>();
        List<String> operation = new ArrayList<>() {{
            add("and");
            add("or");
            add("not");
            add(")");
            add("(");
        }};
        for (String s : postExpression) {
            if (!operation.contains(s)) {
                if (!wordsMap.containsKey(s)) {
                    return null;
                }
                stack.addLast(wordsMap.get(s).getDocIds());
            } else if (s.equals("and")) {
                List<Integer> tmp1 = stack.pollLast();
                List<Integer> tmp2 = stack.pollLast();
                List<Integer> result = mergeTwo(tmp1, tmp2);
                stack.addLast(result);
            } else if (s.equals("or")) {
                List<Integer> tmp1 = stack.pollLast();
                List<Integer> tmp2 = stack.pollLast();
                List<Integer> result = addTwo(tmp1, tmp2);
                stack.addLast(result);
            } else if (s.equals("not")) {
                List<Integer> tmp = stack.pollLast();
                List<Integer> result = complementList(tmp);
                stack.addLast(result);
            } else {
                continue;
            }
        }
        return stack.pollLast();
    }

    public void saveIndex(@NotNull Scanner sc) {
        System.out.println("Please enter the path to save dict.index");
        String toPath = sc.nextLine();
        writeToFile(toPath);
        System.out.println("Successfully save to " + toPath);
        System.out.println("-----------------------");
    }

    public void processingFile(@NotNull Scanner sc) {
        System.out.println("Please enter the directory path;");
        String directoryPath = sc.nextLine();
        readFiles(directoryPath);
    }

    public void boolSearch(@NotNull String query) {
        String[] querySentence = query.toLowerCase().trim().split(" +");
        List<Integer> ls = calculatePostExpression(toPostExpression(querySentence));
        if (ls == null) {
            System.out.println("No such word exists in any doc! Sorry");
            return;
        }
        for (int i : ls) {
            System.out.print(i + " ");
        }
    }


    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        PostIndexing pi = new PostIndexing();
        pi.processingFile(sc);
        System.out.println("Do you want to save the dict.index file?(YES/NO)");
        String ans = sc.nextLine();
        if (ans.equalsIgnoreCase("YES") || ans.equalsIgnoreCase("Y")) {
            pi.saveIndex(sc);
        }
        System.out.println("Please enter the bool search sentence");
        System.out.println("Caution:Please separate the parentheses by space");
        String query = sc.nextLine();
        pi.boolSearch(query);
        sc.close();
    }
}