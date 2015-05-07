/*
 * Autor: Robert Poelzelbauer
 * Datei: ScanFile.java
 * Zweck: Laedt verdaechtige Dateien nach Virustotal.com
 * Datum: 15.04.2015
 */

package virustotal;

import com.kanishka.virustotal.dto.ScanInfo;
import com.kanishka.virustotal.exception.APIKeyNotFoundException;
import com.kanishka.virustotal.exception.QuotaExceededException;
import com.kanishka.virustotal.exception.UnauthorizedAccessException;
import com.kanishka.virustotalv2.VirusTotalConfig;
import com.kanishka.virustotalv2.VirustotalPublicV2;
import com.kanishka.virustotalv2.VirustotalPublicV2Impl;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScanFile {

    public static void main(String[] args) {

        //Quellordner mit Ordner pro Webseite
        File folder = new File("/media/rob/4dc820f1-7f63-4b24-b61c-9cafac56d527/EXE");
        int i = 0;

        //Virustotal API "APIKEY" wurde statt richtigem Key eingefuegt
        VirusTotalConfig.getConfigInstance().setVirusTotalAPIKey("APIKEY");
        VirustotalPublicV2 virusTotalRef;
        try {
            //Verbindung mit Virustotal mit APIKey
            virusTotalRef = new VirustotalPublicV2Impl();
            ScanInfo scanInformation;

            try {
                //Ausgabe-Datei "ScanIDs.txt"
                PrintWriter fw = new PrintWriter("ScanIDs.txt", "UTF-8");
                //alle Dateien aus allen Ordnern einlesen 
                for (File folders : folder.listFiles()) {
                    for (File files : folders.listFiles()) {
                        try {
                            //Naechste Datei:
                            System.out.println("");
                            System.out.println(++i + " - " + files.getAbsolutePath());

                            //Upload nach Virustotal
                            scanInformation = virusTotalRef.scanFile(files);

                            //Logging
                            System.out.println(files.getAbsolutePath());
                            System.out.println("MD5 :\t" + scanInformation.getMd5());
                            System.out.println("Perma Link :\t" + scanInformation.getPermalink());
                            System.out.println("Resource :\t" + scanInformation.getResource());
                            System.out.println("Scan Date :\t" + scanInformation.getScanDate());
                            System.out.println("Scan Id :\t" + scanInformation.getScanId());
                            System.out.println("SHA1 :\t" + scanInformation.getSha1());
                            System.out.println("SHA256 :\t" + scanInformation.getSha256());
                            System.out.println("Verbose Msg :\t" + scanInformation.getVerboseMessage());
                            System.out.println("Response Code :\t" + scanInformation.getResponseCode());
                            fw.println(folders.getName() + ":" + scanInformation.getResource() + ":" + files.getAbsolutePath());
                            fw.flush();
                        } catch (UnsupportedEncodingException ex) {
                            System.err.println("ERROR: Nicht unterstuetztes Encoding-Format!" + ex.getMessage());
                        } catch (UnauthorizedAccessException ex) {
                            System.err.println("ERROR: Fehlerhafter API Key " + ex.getMessage());
                        } catch (IOException  ex) {
                            System.err.println("ERROR: Fehler beim Lesen der Datei! " + ex.getMessage());
                        }catch (QuotaExceededException ex) {
                            System.err.println("ERROR: Quota ueberschritten! " + ex.getMessage());
                        }
                    }
                }
                fw.close();
            } catch (IOException ex) {
                System.err.println("ERROR: Kann Log-Datei nicht schreiben!");
            }
        } catch (APIKeyNotFoundException ex) {
            System.err.println("ERROR: API-Key nicht gefunden!");
        }
    }
}
