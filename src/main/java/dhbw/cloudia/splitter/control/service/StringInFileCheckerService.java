package dhbw.cloudia.splitter.control.service;

import dhbw.cloudia.splitter.control.exception.FileReadingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StringInFileCheckerService {

    private final ResourceLoader resourceLoader;

    public boolean stringIsInFile(String string, String filePath) {
        File file = loadFile(filePath);
        try {
            FileReader fileReader = new FileReader(file);
            int i = 0;
            Scanner scanner = new Scanner(fileReader);
            while (scanner.hasNextLine()) {
                String nextLine = scanner.nextLine();
                if (nextLine.equalsIgnoreCase(string.trim())) {
                    return true;
                }
            }
            return false;
        } catch (FileNotFoundException e){
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
