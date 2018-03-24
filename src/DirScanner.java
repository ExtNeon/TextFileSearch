import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Кирилл on 24.03.2018.
 */
public class DirScanner extends Thread {
    private String startPath;
    private ArrayList<String> filesList;
    public void run() {
        ArrayList<DirScanner> innerDirs = new ArrayList<>();
        File startDir = new File(startPath);
        if (startDir.isDirectory()) {
            for (File currentFile : startDir.listFiles()) {
                try {
                    if (currentFile.isDirectory()) {
                        innerDirs.add(new DirScanner(currentFile.getCanonicalPath(), filesList));
                        innerDirs.get(innerDirs.size() - 1).start();
                    } else {
                        filesList.add(currentFile.getCanonicalPath());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        for (DirScanner currentInnerDirScanner : innerDirs) {
            try {
                currentInnerDirScanner.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public DirScanner(String startPath, ArrayList<String> filesList) {
        this.startPath = startPath;
        this.filesList = filesList;
    }

    public DirScanner(String startPath) {
        this(startPath, new ArrayList<String>());
    }

    public ArrayList<String> getFilesList() {
        return filesList;
    }
}
