import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Кирилл on 24.03.2018.
 */
public class ThreadedFileTextSearcher extends Thread {
    private String pathToFile;
    private String textToFind;
    private boolean isFounded = false;

    public void run() {
        try {
            Thread.sleep(1000); //Спим секунду, чтобы дать время запустить все потоки
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try (FileReader reader = new FileReader(pathToFile)) {
            StringBuilder fileText = new StringBuilder();
            int readedChar;
            while ((readedChar = reader.read()) != -1) {
                fileText.append((char) readedChar);
            }
            isFounded = fileText.indexOf(textToFind) != -1;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ThreadedFileTextSearcher(String filePath, String textToFind) {
        this.pathToFile = filePath;
        this.textToFind = textToFind;
    }

    public boolean isFounded() {
        return isFounded;
    }

    public String getPathToFile() {
        return pathToFile;
    }
}
