package edu.umsl;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.MalformedURLException;
import java.util.*;

public class WebCrawler {
    // URLs previously crawled
    ArrayList<String> listOfTraversedURLs = new ArrayList<>();
    // URLs to be crawled
    ArrayList<String> listOfPendingURLs = new ArrayList<>();
    // To track word occurrences <Word, Count>
    Map<String, Integer> wordCountMap = new HashMap<>();
    // To track title of pages visited
    ArrayList<String> listOfPageTitles = new ArrayList<>();
    // number of links to crawl
    int crawlNum;

    public WebCrawler(){
        this.crawlNum = 1;
    }

    public WebCrawler(int crawlNum){
        this.crawlNum = crawlNum;
    }

    // a valid link is one that links to other wiki article
    private static boolean isLinkValid(String linkHref){
        // our link must include /wiki/ to keep our search to wikipedia articles
        if (!linkHref.contains("/wiki/")) return false;
        // we don't want to crawl links to non text based sources (ie links to images and gifs etc)
        if (linkHref.matches("(.*/)*.+\\.(png|jpg|gif|bmp|jpeg|PNG|JPG|GIF|BMP|JPEG)$")) return false;
        // ignore Category links and other directory/helper links as they don't link to content pages
        if (linkHref.matches("/wiki/.+:.+")) return false;
        // some links contain the pattern 'wiki', but aren't relative links/link to outside content
        if (linkHref.contains("http")) return false;
        return true;
    }

    private static String cleanString(String input) {
        return input.replaceAll("[^a-zA-Z'-]+", "").toLowerCase();
    }

    // updates listOfPendingURLs field with valid links.
    private void addLinksToPending(Element content){
        // add more links if there are more links to traverse than the number within pending
        if ((crawlNum - listOfTraversedURLs.size()) > listOfPendingURLs.size()) {
            Elements links = content.getElementsByTag("a");
            for (Element link : links) {
                String linkHref = link.attr("href");
                String absLink = "https://en.wikipedia.org" + linkHref;
                // add link if it is a valid relative link
                // and if it isn't already in the pending traversal or already traversed list
                if (isLinkValid(linkHref)
                        && !listOfPendingURLs.contains(absLink)
                        && !listOfTraversedURLs.contains(linkHref)
                ) listOfPendingURLs.add(absLink);
                // stop adding links once we have more links to crawl than is given by the crawlNum
                if ((crawlNum - listOfTraversedURLs.size()) <= listOfPendingURLs.size()) break;
            }
        }
    }

    public void printPendingLinks(){
        for (String link : listOfPendingURLs){
            System.out.println(link);
        }
    }

    public void printWordMap(){
        // converts the entries into a stream so that we can sort them. Then prints each entry to System.out
        wordCountMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(System.out::println);
    }

    public void printTitleList(){
        for (String title : listOfPageTitles){
            System.out.println(title);
        }
    }

    private void countWords (Element content){
        String text = content.text();
        // change String text-block into array of Strings
        String[] textArr = text.split(" ");
        for (String string : textArr){
            string = cleanString(string);
            // if a string exists after cleaning, search it and add/increment it
            if (!string.equals("")){
                // if word is already inputted, increment the value by one
                if(wordCountMap.containsKey(string)) wordCountMap.put(string, wordCountMap.get(string) + 1);
                // add new word occurrence
                else wordCountMap.put(string, 1);
            }
        }
    }

    // Counts words and tracks page titles until specified number of pages has been crawled.
    public void startCrawl (String startingURL){
        listOfPendingURLs.add(startingURL);
        while (!listOfPendingURLs.isEmpty() && listOfTraversedURLs.size() < crawlNum){
            try {
                // remove url at beginning of list
                String URL = listOfPendingURLs.remove(0);
                // connect to URL and construct document
                Document doc = Jsoup.connect(URL).get();
                // add title of document to list of traversed titles
                listOfPageTitles.add(doc.title());
                System.out.println("Crawling: " + doc.title());
                System.out.println("URL = " + URL);
                // narrow data to content that is specific to article using ID selector
                Element content = doc.getElementById("mw-content-text");
                // add links on the page to pending traversal list
                addLinksToPending(content);
                // update wordCountMap with words from page
                countWords(content);
                // connection successful and data processed, add current URL to the listOfTraversedURLs
                listOfTraversedURLs.add(URL);
                // halts program for .05 seconds to reduce load on server
                Thread.sleep(50);
            } catch (MalformedURLException | IllegalArgumentException ex) {
                System.out.println("Invalid URL");
            } catch (java.io.IOException ex){
                System.out.println("I/O Errors: no such file");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        WebCrawler webCrawler = new WebCrawler(3);
        webCrawler.startCrawl("https://en.wikipedia.org/wiki/St._Louis");
        System.out.println("\nWords and their occurrences: ");
        webCrawler.printWordMap();
        System.out.println("\nTitle of Pages Crawled: ");
        webCrawler.printTitleList();
    }
}


