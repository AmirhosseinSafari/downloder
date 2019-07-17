package assignment10;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.jms.JMSException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;


public class Main {

    static HashSet<String> linksNoneRepeataion = new HashSet<>();
    public static void main(String[] args) throws IOException, JMSException {

        String url = args[0];                           //getting parameters
        String destination = args[1];
        int level = Integer.parseInt(args[2]);
        int numberOfThreads = 1;

        if(args[3] != null) {numberOfThreads = Integer.parseInt(args[3]); }
        //*


        URL firstURL = new URL(url);                    //first url html
        BufferedReader in = new BufferedReader(new InputStreamReader(firstURL.openStream()));
        String inputLine;
        File_handler firstHtml = new File_handler();

        while ((inputLine = in.readLine()) != null) {
            firstHtml.writeToFile(String.valueOf(url.hashCode()),destination, inputLine);
        }
        in.close();
        //*

        linksInSet(url);                                        //first links

        for(int counter = 0 ; counter<level ; counter++){       //level of depth
            for(String link : linksNoneRepeataion){
                linksInSet(link);
            }
        }

        activeMQ queue = new activeMQ();                //adding links to the queue and array
        String [] arrayOfLinks = new String[linksNoneRepeataion.size()];
        int counterForLinks = 0;

        for(String link : linksNoneRepeataion){
            //System.out.println(link);
            queue.sendToQueue(link);
            arrayOfLinks[counterForLinks] = link;
            counterForLinks++;
        }


        int outterCounetr = 0;                                  //working on Thread
        for( ;  ; ){
            for(int innerCounter = 0 ; innerCounter<numberOfThreads ; innerCounter++){
                Thread_handler thread = new Thread_handler(queue.getFromQueue(),destination,"thread No."+innerCounter);
                thread.start();
                outterCounetr++;
            }

            if(outterCounetr == linksNoneRepeataion.size()){
                break;
            }

            if(outterCounetr%10 == 0){                      //show
                System.out.println(outterCounetr + "/" + linksNoneRepeataion.size());
            }
        }                                                       //*


    }
    public static void linksInSet(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();        //links
        Elements links = doc.select("a[href]");

        for(Element link : links){
            String L = link.attr("href");
            if (Stream.of(url).anyMatch(s -> L.startsWith(s))){
                //        System.out.println(L);
                linksNoneRepeataion.add(L);
            }
        }
    }                                                   //*
}

