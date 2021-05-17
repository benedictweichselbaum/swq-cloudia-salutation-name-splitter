package dhbw.cloudia.splitter.control.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Service class checking if string is a line in file
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StringInFileCheckerService {

    private static final String TITLES_PATH = "contact_info/titles.txt";

    private final TitleLearningService titleLearningService;

    /**
     * Method determining if a string is in file. File gets split at a new line.
     * Special case. When concerning titles it is also looked at the learned titles by
     * consulting the TitleLearningService.
     * @param string string on which the comparison takes place
     * @param filePath file path
     * @return true if string is in file (or learned title), else false
     */
    public boolean stringIsInFile(String string, String filePath) {

        if (TITLES_PATH.equals(filePath) && this.titleLearningService.titleIsLearned(string.trim())) {
            return true;
        }

        try (Scanner scanner = new Scanner(loadInputStream(filePath))){
            while (scanner.hasNextLine()) {
                String nextLine = new String(scanner.nextLine().getBytes(), StandardCharsets.UTF_8);
                if (nextLine.equalsIgnoreCase(string.trim())) {
                    return true;
                }
            }
            return false;
        }
    }

    private InputStream loadInputStream(String filePath) {
        return this.getClass().getClassLoader().getResourceAsStream(filePath);
    }
}
