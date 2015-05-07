/*
 * Autor: Robert Poelzelbauer
 * Datei: GetURLs.java
 * Zweck: Filtert URLs welche in der Datei URLBlacklists.txt sind aus der
 *        Datei Top1M.txt und schreibt diese in URLListToDo.txt
 * Datum: 15.04.2015
 */

package getURLs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetURLs {

    public static void main(String[] args) {
        List<String> lstURLBl = new ArrayList<>(); //Liste fuer URLBlacklists.txt
        List<String> lstURLToDo = new ArrayList<>(); //Liste der zu ueberpruefenden URLs

        try {
            //URLBlacklists.txt in lstURLBl einlesen
            BufferedReader in = new BufferedReader(new FileReader("URLBlacklists.txt"));
            String line;
            while ((line = in.readLine()) != null) {
                lstURLBl.add(line);
            }

            if (lstURLBl.size() > 0) {
                //Einlesen der Datei Top1M.txt
                in = new BufferedReader(new FileReader("Top1M.txt"));
                while ((line = in.readLine()) != null) {
                    //wenn Top1M-URL in Blacklist dann in lstURLToDo einfuegen
                    if (lstURLBl.contains(line.split(",")[1])) {
                        lstURLToDo.add(line);
                    }
                }
            } else {
                System.err.println("Error: Eingabedatei leer!");
            }

        } catch (FileNotFoundException ex) {
            System.err.println("Error: Eingabedatei nicht gefunden!");
        } catch (IOException ex) {
            System.err.println("Error: Eingabedatei nicht lesbar!");
        }

        //lstURLToDo in URLListToDo.txt schreiben
        FileWriter fw;
        try {
            fw = new FileWriter("URLListToDo.txt", true);
            for (String url : lstURLToDo) {
                fw.write(url + "\n");
            }
            fw.close();
        } catch (IOException ex) {
            System.err.println("Error: Kann Datei nicht schreiben!");
        }

    }
}
