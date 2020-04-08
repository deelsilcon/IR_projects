package com.deelsilcon.Exhtml;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class ExtractHtml {
    public static void readHtml(String path) throws IOException {
        List<String> linksArr = new ArrayList<>();
        List<String> linksName = new ArrayList<>();
        Document doc = Jsoup.parse(new File(path), "utf-8");
        Elements titles = doc.getElementsByTag("title");
        Elements body = doc.getElementsByTag("body");
        Elements links = doc.getElementsByTag("a");
        System.out.println("Title:");
        for (Element title : titles) {
            System.out.println(title.text());
        }
        System.out.println("\nBody:");
        for (Element content : body) {
            System.out.println(content.text());
        }
        for (Element link : links) {
            Attributes attr = link.attributes();
            System.out.println(link.text());
            linksName.add(link.text());
            linksArr.add(attr.get("href"));
        }
        System.out.println("\nLinks:");
        for (int i = 0; i < linksArr.size(); i++) {
            System.out.println(linksName.get(i) + " " + linksArr.get(i));
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter the path of the file");
        String path = sc.next();
        ExtractHtml.readHtml(path);
    }
}
