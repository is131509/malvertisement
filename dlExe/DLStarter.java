/*
 * Autor: Robert Poelzelbauer
 * Datei: DLFinder.java
 * Zweck: Liest zu downloadende URLs und startet Threads "DLExecutor"
 * Datum: 15.04.2015
 */

package dlExe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DLStarter {

    public static void main(String[] args) {

        final File folder = new File("DL"); //zu durchsuchender Ordner
        String nr;  //Webseitennummer
        int i;      //Zaehlvariable

        ExecutorService e = Executors.newFixedThreadPool(20);

        //alle Dateien aus Verzeichnis "DL" auslesen
        for (final File fileEntry : folder.listFiles()) {
            nr = fileEntry.getName().split("\\.")[0].split("-")[1];
            File dldir = new File(nr);  //Ziel-Ordner erstellen
            if (dldir.mkdir()) {
                i = 0;
                BufferedReader in;
                try {
                    in = new BufferedReader(new FileReader(fileEntry));

                    //alls URLs aus Datei auslesen und Thread starten
                    String line;
                    while ((line = in.readLine()) != null) {
                        i++;
                        URL from = new URL(line);
                        File to = new File(dldir, nr + "-" + i);
                        DLExecutor dl = new DLExecutor(from, to);
                        e.execute(dl);
                    }
                } catch (FileNotFoundException ex) {
                    System.err.println("ERROR: Kann Datei nicht finden!");
                } catch (IOException ex) {
                    System.err.println("ERROR: Kann Datei nicht lesen!");
                }
            }
        }
    }
}
