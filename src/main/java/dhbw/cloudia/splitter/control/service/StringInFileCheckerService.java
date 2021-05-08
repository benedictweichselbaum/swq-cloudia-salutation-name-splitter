package dhbw.cloudia.splitter.control.service;

import dhbw.cloudia.splitter.control.exception.FileReadingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StringInFileCheckerService {

    private final ResourceLoader resourceLoader;

    public boolean stringIsInFile(String string, String filePath) {
        File file = loadFile(filePath);
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                if (scanner.nextLine().equalsIgnoreCase(string.trim())) {
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            throw new FileReadingException(e.getMessage());
        }
    }

    private File loadFile(String filePath) {
        try {
            return this.resourceLoader.getResource(filePath).getFile();
        } catch (IOException e) {
            throw new FileReadingException(e.getMessage());
        }
    }
}