/*
 * Autor: Robert Poelzelbauer
 * Datei: AdCrawler.java
 * Zweck: Sucht nach Werbungen auf Webseiten und speichert die Ziel-URL
 * Datum: 15.04.2015
 */

package adCrawler;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

public class AdCrawler implements Runnable {

    String nr;          //Webseitennummer
    String url;         //Webseiten-URL
    List<String> lstAdN;   //Liste der Werbenetzwerke

    AdCrawler(String nr, String url, List<String> lstAdN) {
        this.nr = nr;
        this.url = url;
        this.lstAdN = lstAdN;
    }

    @Override
    public void run() {
        String baseurl = "http://" + url;
        String host = baseurl.split("//")[1];

        //Aufruf der Webseite
        WebDriver driver = new FirefoxDriver();
        driver.get(baseurl);

        List<String> lstAds = new ArrayList<>();

        //Speichern aller A-Tags
        List<WebElement> lstLinks = driver.findElements(By.xpath("//a"));
        String dst;
        for (WebElement link : lstLinks) {
            try {
                dst = link.getAttribute("href").toLowerCase();
                if (!dst.contains(host) && dst.startsWith("http")) {
                    lstAds.add(dst);
                }
            } catch (Exception ex) {
                System.err.println("Error: Kann URL nicht lesen!");
            }
        }

        //Durchsuchung aller IFrames nach A-Tags
        List<WebElement> iframes = new ArrayList<>();
        iframes = driver.findElements(By.tagName("iframe"));
        for (WebElement iframe : iframes) {
            try {
                driver.switchTo().frame(iframe).findElement(By.tagName("a")).click();
                Set<String> windowID = driver.getWindowHandles();
                Iterator<String> iterator = windowID.iterator();
                String mainWinID = iterator.next();
                String newAdwinID = iterator.next();
                driver.switchTo().window(newAdwinID);
                lstAds.add(driver.getCurrentUrl());
                Thread.sleep(4000);
                driver.close();
                driver.switchTo().window(mainWinID);
            } catch (Exception ex) {
                System.err.println("Error: Fehler beim Aufruf des IFrames!");
            }
        }
        driver.switchTo().defaultContent();

        //Klick auf alle Object-Elemente (Flash)
        List<WebElement> objects = new ArrayList<>();
        objects = driver.findElements(By.tagName("object"));
        for (WebElement object : objects) {
            try {
                Actions builder = new Actions(driver);
                builder.moveToElement(object).click().build().perform();
                Set<String> windowID = driver.getWindowHandles();
                Iterator<String> iterator = windowID.iterator();
                String mainWinID = iterator.next();
                String newAdwinID = iterator.next();
                driver.switchTo().window(newAdwinID);
                lstAds.add(driver.getCurrentUrl());
                Thread.sleep(4000);
                driver.close();
                driver.switchTo().window(mainWinID);
            } catch (Exception ex) {
                System.err.println("Error: Fehler beim Klick auf Object-Element!");
            }
        }

        //Klick auf alle Embed-Elemente (Flash)
        List<WebElement> embeds = new ArrayList<>();
        embeds = driver.findElements(By.tagName("embed"));
        for (WebElement embed : embeds) {
            try {
                Actions builder = new Actions(driver);
                builder.moveToElement(embed).click().build().perform();
                Set<String> windowID = driver.getWindowHandles();
                Iterator<String> iterator = windowID.iterator();
                String mainWinID = iterator.next();
                String newAdwinID = iterator.next();
                driver.switchTo().window(newAdwinID);
                lstAds.add(driver.getCurrentUrl());
                Thread.sleep(4000);
                driver.close();
                driver.switchTo().window(mainWinID);
            } catch (Exception ex) {
                System.err.println("Error: Fehler beim Klick auf Embed-Element");
            }
        }

        //Klick auf alle IFrames
        iframes = driver.findElements(By.tagName("iframe"));
        for (WebElement iframe : iframes) {
            try {
                Actions builder = new Actions(driver);
                builder.moveToElement(iframe).click().build().perform();

                Set<String> windowID = driver.getWindowHandles();
                Iterator<String> iterator = windowID.iterator();
                String mainWinID = iterator.next();
                String newAdwinID = iterator.next();
                driver.switchTo().window(newAdwinID);
                lstAds.add(driver.getCurrentUrl());
                Thread.sleep(4000);
                driver.close();
                driver.switchTo().window(mainWinID);
            } catch (Exception ex) {
                System.err.println("Error: Fehler beim Klick auf IFrame!");
            }
        }
        
        //Speicherung gefundener Werbe-Links
        PrintWriter writer;
        try {
            writer = new PrintWriter(nr + ".txt", "UTF-8");
            for (String ad : lstAds) {
                writer.println(ad);
            }
            writer.close();
        } catch (FileNotFoundException ex) {
            System.err.println("Error: Kann Datei nicht schreiben!");
        } catch (UnsupportedEncodingException ex) {
            System.err.println("Error: Fehler beim kodieren!");
        }

        //Durchsuchung der Webseite nach Werbeanbieter
        String content = driver.getPageSource();
        for (String ad : lstAdN) {
            if (content.contains(ad)) {
                try {
                    String filename = "AD-" + nr + ".txt";
                    FileWriter fw = new FileWriter(filename, true);
                    fw.write(ad);
                    fw.close();
                } catch (FileNotFoundException ex) {
                    System.err.println("Error: Kann Datei nicht schreiben!");
                } catch (UnsupportedEncodingException ex) {
                    System.err.println("Error: Fehler beim kodieren!");
                } catch (IOException ex) {
                    System.err.println("Error: Fehler beim Schreiben der Datei");
                }
            }
        }
        driver.quit();
    }
}
