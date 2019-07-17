package assignment10;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class File_handler {

    public static void writeToFile(String nameOfFile,String pathName,String text) throws IOException {

        File file =  new File(pathName + "\\" + nameOfFile + ".html");

        FileWriter fileWriter = new FileWriter(file,true);
        fileWriter.write(text);
        fileWriter.write("\n");
        fileWriter.close();
    }

}
