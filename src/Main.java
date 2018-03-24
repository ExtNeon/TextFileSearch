import java.util.ArrayList;
import java.util.Scanner;
import java.text.DecimalFormat;
/**
 * Created by Кирилл on 24.03.2018.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        DirScanner scanner = new DirScanner(getEnteredString("Введите стартовый каталог: "));
        String textToFind = getEnteredString("Введите строку, которую необходимо найти: ");
        scanner.start();
        System.out.println("Выполняется поиск файлов...");
        System.out.print("0 файлов найдено");
        while (scanner.isAlive()) {
            System.out.print("\r"+scanner.getFilesList().size() + " файлов найдено");
            Thread.sleep(100);
        }
        ArrayList<String> textFilesList = new ArrayList<>();
        for (String currentFile : scanner.getFilesList()) {
            try {
                if (currentFile.substring(currentFile.lastIndexOf('.') + 1).equals("txt")) {
                    textFilesList.add(currentFile);
                }
            } catch (NullPointerException e) {
                System.err.println("Ошибка в процессе поиска: запись равна null. Данная запись была пропущена.");
            }
        }
        System.out.println("\nВ указанной дирректории и всех поддиректориях найдено " + textFilesList.size() + " текстовых файлов");
        if (textFilesList.size() > 0) {
            ArrayList<ThreadedFileTextSearcher> searchers = new ArrayList<>(textFilesList.size());
            ArrayList<String> foundFilesList = new ArrayList<>();
            for (String currentTextFile : textFilesList) {
                searchers.add(new ThreadedFileTextSearcher(currentTextFile, textToFind));
                searchers.get(searchers.size() - 1).start();
            }
            int countOfSearchers = searchers.size();
            int searchersDone = 0;
            System.out.println("Начат поиск в текстовых файлах...");
            System.out.print(getPercentLine((searchersDone / countOfSearchers) * 100, 60) + " done");
            while (searchers.size() > 0) {
                for (int i = 0; i < searchers.size(); i++) {
                    if (!searchers.get(i).isAlive()) {
                        if (searchers.get(i).isFounded()) {
                            foundFilesList.add(searchers.get(i).getPathToFile());
                        }

                        searchersDone++;
                        System.out.print("\r" + (searchers.get(i).isFounded() ? "Искомая строка найдена в файле \""+searchers.get(i).getPathToFile()+"\"\n" : "") + getPercentLine((((double) searchersDone / countOfSearchers) * 100), 60) + " done");
                        searchers.remove(i);
                        break;
                    }
                }
            }
            System.out.println("\nРезультат поиска: \nВсего файлов в дирректориях: " +  scanner.getFilesList().size() + ", из них текстовых файлов: " + textFilesList.size()
                    + "\nИскомый текст найден в " + foundFilesList.size() + " файлах.");
        }
    }

    private static String getEnteredString(String message) {
        System.out.print(message);
        return new Scanner(System.in).nextLine();
    }

    /**
     * Возвращает строку ASCII - графики, представляющую собой progressBar.
     *
     * @param percents процентное соотношение заполненной части шкалы к пустой.
     * @return ASCII - строка, представляющая собой progressBar.
     */
    private static String getPercentLine(double percents, int length) {
        StringBuilder temp = new StringBuilder("[");
        percents /= 100. / length;
        for (int i = 1; i <= length; i++) {
            if (i <= percents) {
                temp.append('=');
            } else {
                temp.append(' ');
            }
        }
        temp.append("] | ");
        temp.append(new DecimalFormat("#0.00").format(percents * 100. / length));
        temp.append('%');
        return temp.toString();
    }
}
