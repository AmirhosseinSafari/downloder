package assignment10;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class Thread_handler extends Thread{
    String nameOfFile;
    String pathName;
    String nameOfThread;

    public Thread_handler(String nameOfFile,String pathName,String nameOfThread){
        this.nameOfFile = nameOfFile;
        this.pathName = pathName;
        this.nameOfThread = nameOfThread;
    }

    @Override
    public void run(){
        try {
            URL firstURL = new URL(nameOfFile);                    //downloding
            BufferedReader in = new BufferedReader(new InputStreamReader(firstURL.openStream()));
            String inputLine;
            File_handler file = new File_handler();

            while ((inputLine = in.readLine()) != null) {
                file.writeToFile(String.valueOf(nameOfFile.hashCode()),pathName, inputLine);
            }
            in.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
