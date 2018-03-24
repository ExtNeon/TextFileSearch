import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Кирилл on 24.03.2018.
 */
public class ThreaderFileReader extends Thread {
    String pathToFile;
    String readedText = "";

    public void run() {

    }

    public String getReadedText() {
        return readedText;
    }

    public ThreaderFileReader(String pathToFile) {
        this.pathToFile = pathToFile;
    }
}
