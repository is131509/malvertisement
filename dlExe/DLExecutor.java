/*
 * Autor: Robert Poelzelbauer
 * Datei: DLFinder.java
 * Zweck: Sucht auf einer Webseite nach verdächtigen Dateien
 * Datum: 15.04.2015
 */

package dlExe;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.FileUtils;


class DLExecutor implements Runnable {

    URL from;   //Quell-URL
    File to;    //Ziel Speicherort

    DLExecutor(URL from, File to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public void run() {
        try {
            //Laedt Datei von "from" nach "to" herunter
            // 20000 - Connect-Timeout 20 Sekunden
            //600000 - Download-Timeout 10 Minuten
            FileUtils.copyURLToFile(from, to, 20000, 600000);
            System.out.println("Success for " + from);
        } catch (IOException ex) {
            System.err.println("ERROR: Download failed for " + from);
        }

    }

}
