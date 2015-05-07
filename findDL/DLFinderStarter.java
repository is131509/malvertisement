/*
 * Autor: Robert Poelzelbauer
 * Datei: DLFinderStarter.java
 * Zweck: Liest alle URLs aus allen Dateien im Ordner "findings" und startet DLFinder.java
 * Datum: 15.04.2015
 */

package findDL;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DLFinderStarter {

    public static void main(String[] args) {

        final File folder = new File("findings");
        List<String> urls = new ArrayList<>();  //zu durchsuchende URLs
        String nr;  //Webseitennummer
        
        //Zeitgleiche Ausführung von jeweils 100 Threads
        ExecutorService e = Executors.newFixedThreadPool(100);
        
        //alle Dateien aus Ordner "findings" auslesen
        for (final File fileEntry : folder.listFiles()) {
            urls.clear();
            nr = fileEntry.getName().split("\\.")[0];
            try {
                BufferedReader in = new BufferedReader(new FileReader(fileEntry));
                String line;
                //Pro Zeile=URL einen Thread DLFinder starten
                while ((line = in.readLine()) != null) {
                    DLFinder dl = new DLFinder(nr, line);
                    e.execute(dl);
                }
            } catch (Exception ex) {
                System.err.println("ERROR: Kann Links nicht lesen!");
            }
        }
        e.shutdown();
    }
}
