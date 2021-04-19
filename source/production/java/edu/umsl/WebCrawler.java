package edu.umsl;

import java.net.URL;
import java.util.Scanner;

public class WebCrawler {

//    public static void crawler(String startingURL) {
//        ArrayList<String> listOfPendingURLs = new ArrayList<>();
//        ArrayList<String> listOfTraversedURLs = new ArrayList<>();
//
//        listOfPendingURLs.add(startingURL);
//
//        while(!listOfPendingURLs.isEmpty() && listOfTraversedURLs.size() <= 100){
//            String urlString = listOfPendingURLs.remove(0);
//            listOfTraversedURLs.add(urlString);
//            System.out.println("Crawl " + urlString);
//            // for subURLs found in the urlString, add to pendingURL array list of not already processed
//            for (String s: getSubURLs(urlString)){
//                if (!listOfTraversedURLs.contains(s)){
//                    listOfPendingURLs.add(s);
//                }
//            }
//        }
//    }
//
//    public static ArrayList<String> getSubURLs (String urlString) {
//        ArrayList<String> list = new ArrayList<>();
//
//        try {
//            URL url = new URL(urlString);
//
//            Thread.sleep(50);
//            Scanner input = new Scanner(url.openStream());
//            int current = 0;
//            while (input.hasNext()) {
//                String line = input.nextLine();
//                // we only want to traverse english wikipedia pages
//                current = line.indexOf("https://en.wikipedia.org/wiki/", current);
//                while (current > 0) {
//                    int endIndex = line.indexOf("\"", current);
//                    if (endIndex > 0){
//                        list.add(line.substring(current, endIndex));
//                        current = line.indexOf("https://en.wikipedia.org/wiki/", endIndex);
//                    }
//                    else current -=1;
//                }
//            }
//
//        } catch (Exception ex) {
//            System.out.println("Error: " + ex.getMessage());
//        }
//
//        return list;
//    }

    public static void main(String[] args){
//        Scanner input = new Scanner(System.in);
//        System.out.print("Enter a starting URL: ");
//        String url = input.nextLine();
//        crawler(url); // Travers the web from the starting url

        try {
            URL url = new URL("https://en.wikipedia.org/wiki/Gateway_Arch");
            int count = 0;
            Scanner input = new Scanner(url.openStream());
            while (input.hasNext()){
                System.out.println(input.nextLine());
            }


        } catch (java.net.MalformedURLException ex){
            System.out.println("Invalid URL");
        } catch (java.io.IOException ex) {
            System.out.println("I/O Errors: no such file");
        }

    }
}

