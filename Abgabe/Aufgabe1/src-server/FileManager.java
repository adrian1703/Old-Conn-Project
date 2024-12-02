

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class FileManager {
    String currentDir;

    public FileManager()
    {
        currentDir = System.getProperty("user.dir");
    }

    public String getFileList()
    {
        File currentDirAsFile = new File(currentDir);
        StringBuilder fileNames = new StringBuilder();
        for (String path : Objects.requireNonNull(currentDirAsFile.list())) {
            fileNames.append(path).append("\n");
        }
        return fileNames.toString();
    }

    public String getFileContent(String fileName)
    {
        boolean fileExists;
        File currentDirAsFile = new File(currentDir);

        //check if file exists within current directory
        fileExists = Arrays.stream(Objects.requireNonNull(currentDirAsFile.list()))
                .anyMatch(s -> s.equals(fileName));

        if (fileExists) {
            StringBuilder fileContent = new StringBuilder();
            File file = new File(fileName);
            try {
                Scanner fileReader = new Scanner(file);
                while (fileReader.hasNextLine()) {
                    fileContent.append(fileReader.nextLine()).append("\n");
                }
                return fileContent.substring(0, fileContent.length() - 1);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "Error while reading file content";
            }
        } else {
            return fileName + " does not exist in the current directory";
        }

    }
}
