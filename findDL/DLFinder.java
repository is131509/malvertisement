/*
 * Autor: Robert Poelzelbauer
 * Datei: DLFinder.java
 * Zweck: Sucht auf einer Webseite nach verdächtigen Dateien
 * Datum: 15.04.2015
 */

package findDL;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

public class DLFinder implements Runnable {

    String url; //zu durchsuchende URL
    String nr;  //Webseitennummer

    DLFinder(String nr, String url) {
        this.url = url;
        this.nr = nr;
    }

    @Override
    public void run() {
        List<String> dls = new ArrayList<>();   //gefundene Dateien
        WebDriver driver = new HtmlUnitDriver();
        
        //Webseite aufrufen und links in List "links" speichern
        driver.get(url);
        List<WebElement> links = driver.findElements(By.tagName("a"));
        driver.quit();

        //Filterung nach verdächtigen Dateien
        String dst;
        for (WebElement link : links) {
            try {
                dst = link.getAttribute("href").toLowerCase();
                if (dst.endsWith(".exe") || dst.endsWith(".msi") || dst.endsWith(".dll") || dst.endsWith(".bat")
                        || dst.endsWith(".cmd") || dst.endsWith(".zip") || dst.endsWith(".rar") || dst.endsWith(".gz")) {
                    dls.add(dst);
                }
            } catch (Exception ex) {
            }
        }

        //Schreiben der URLs in eine Datei (falls URLs gefunden wurden)
        if (!dls.isEmpty()) {
            try {
                String filename = "DL-" + nr + ".txt";
                FileWriter fw = new FileWriter(filename, true);
                for (String dl : dls) {
                    fw.write(dl + "\n");
                }
                fw.close();
            } catch (FileNotFoundException ex) {
                System.err.println("ERROR: Kann Datei nicht schreiben!");
            } catch (UnsupportedEncodingException ex) {
                System.err.println("ERROR: Fehler beim Codieren der Datei!");
            } catch (IOException ex) {
                System.err.println("ERROR: IO-Fehler beim Schreiben der Datei!");
            }
            dls.clear();
        }
    }
}
