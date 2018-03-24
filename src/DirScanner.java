import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Кирилл on 24.03.2018.
 */
public class DirScanner extends Thread {
    private String startPath;
    private ArrayList<String> filesList;
    private AtomicInteger countOfPassedFiles;
    public void run() {
        ArrayList<DirScanner> innerDirs = new ArrayList<>();
        File startDir = new File(startPath);
        if (startDir.isDirectory()) {
            try {
                for (File currentFile : startDir.listFiles()) {
                    if (currentFile.isDirectory()) {
                        innerDirs.add(new DirScanner(currentFile.getCanonicalPath(), filesList, countOfPassedFiles));
                        innerDirs.get(innerDirs.size() - 1).start();
                    } else {
                        filesList.add(currentFile.getCanonicalPath());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                countOfPassedFiles.incrementAndGet();
                //System.err.println("Dir processing error in \"" + startPath + "\": null. The scanning will be continued.");
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

    public int getCountOfPassedFiles() {
        return countOfPassedFiles.get();
    }

    public DirScanner(String startPath, ArrayList<String> filesList, AtomicInteger countOfPassedFiles) {
        this.startPath = startPath;
        this.filesList = filesList;
        this.countOfPassedFiles = countOfPassedFiles;
    }

    public DirScanner(String startPath) {
        this(startPath, new ArrayList<String>(), new AtomicInteger(0));
    }

    public ArrayList<String> getFilesList() {
        return filesList;
    }
}
