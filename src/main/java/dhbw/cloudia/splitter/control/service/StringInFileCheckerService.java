package dhbw.cloudia.splitter.control.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Service class checking if string is a line in file
 */
@Service
public class StringInFileCheckerService {

    /**
     * Method determining if a string is in file. File gets split at a new line.
     * @param string string on which the comparison takes place
     * @param filePath file path
     * @return true if string is in file, else false
     */
    public boolean stringIsInFile(String string, String filePath) {
        try (Scanner scanner = new Scanner(loadInputStream(filePath))){
            while (scanner.hasNextLine()) {
                String nextLine = scanner.nextLine();
                if (nextLine.equalsIgnoreCase(string.trim())) {
                    return true;
                }
            }
            return false;
        }
    }

    private InputStream loadInputStream(String filePath) {
        filePath = filePath.replace("/", File.separator);
        return this.getClass().getClassLoader().getResourceAsStream(filePath);
    }
}
