/*
 * Autor: Robert Pölzelbauer
 * Datei: GetReport.java
 * Zweck: Laedt Reports von Virustotal.com
 * Datum: 15.04.2015
 */

package virustotal;

import com.kanishka.virustotal.dto.FileScanReport;
import com.kanishka.virustotal.exception.APIKeyNotFoundException;
import com.kanishka.virustotal.exception.QuotaExceededException;
import com.kanishka.virustotal.exception.UnauthorizedAccessException;
import com.kanishka.virustotalv2.VirusTotalConfig;
import com.kanishka.virustotalv2.VirustotalPublicV2;
import com.kanishka.virustotalv2.VirustotalPublicV2Impl;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class GetReport {

    public static void main(String[] args) {
        String line, resource, file, site;
        
        try {
            //Ausgabedatei
            PrintWriter fw = new PrintWriter("Results.csv", "UTF-8");
            
            //Virustotal Konfiguration
            VirusTotalConfig.getConfigInstance().setVirusTotalAPIKey("877f424db3730b91658e5228786f443179b33726b3959aa57fb2664816382364");
            VirustotalPublicV2 virusTotalRef = new VirustotalPublicV2Impl();
            
            //Liest Liste der hinaufgeladenen Dateien
            BufferedReader in = new BufferedReader(new FileReader("ScanIDs.txt"));
            while ((line = in.readLine()) != null) {
                site = line.split(":")[0];  //Webseitennummer
                resource = line.split(":")[1].split(":")[0];  //Virustotal-Kennung
                file = line.split("/")[6];  //Dateiname

                if (!resource.contains("null")) { //Fehler beim Upload
                    FileScanReport report = virusTotalRef.getScanReport(resource);
                    System.out.println("");
                    System.out.println(resource);
                    System.out.println(file + ":" + report.getPositives());
                    fw.println(site + "," + file + "," + report.getPositives());
                    fw.flush();
                }
            }
            fw.close();
        } catch (FileNotFoundException ex) {
            System.err.println("ERROR: Eingabedatei ScanIDs.txt nicht gefunden!");
        } catch (IOException ex) {
            System.err.println("ERROR: Kann Datei nicht lesen!");
        } catch (APIKeyNotFoundException ex) {
            System.err.println("ERROR: Falscher API-Schluessel!");
        } catch (UnauthorizedAccessException ex) {
            System.err.println("ERROR: Fehler beim Zugriff auf Report!");
        } catch (QuotaExceededException ex) {
            System.err.println("ERROR: Quota ueberschritten!");
        }
    }
}
