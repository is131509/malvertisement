/*
 * Autor: Robert Poelzelbauer
 * Datei: Crawler-Starter.java
 * Zweck: Liest ToDo-URLs und startet Ad-Crawler-Threads
 * Datum: 15.04.2015
 */

package adCrawler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CrawlerStarter {

    public static void main(String[] args) {
        //Datei adNetworks einlesen und in lstAdN speichern
        List<String> lstAdN = new ArrayList<>();
        try {
            BufferedReader in = new BufferedReader(new FileReader("adNetworks.txt"));
            String line;
            while ((line = in.readLine()) != null) {
                lstAdN.add(line.split("www.")[1]);
            }
        } catch (IOException ex) {
            System.err.println("Error: Kann adNetworks.txt nicht lesen");
        }
        
        //Starte Theads mit zu durchsuchender URL
        ExecutorService e = Executors.newFixedThreadPool(20);
        try{
            BufferedReader in = new BufferedReader(new FileReader("URLListToDo.txt"));
            String line;
            while((line = in.readLine()) != null){
                AdCrawler sc = new AdCrawler(line.split(",")[0], line.split(",")[1], lstAdN);
                e.execute(sc);
            }
        } catch (IOException ex) {
            System.err.println("Error: Kann Datei URLListToDo.txt nicht lesen");
        }
        e.shutdown();
    }
}