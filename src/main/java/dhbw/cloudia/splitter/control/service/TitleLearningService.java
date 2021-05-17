package dhbw.cloudia.splitter.control.service;

import dhbw.cloudia.splitter.boundary.dto.NewTitleDTO;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Service class that handles the learning of new titles
 */
@Service
public class TitleLearningService {

    private static final Set<String> LEARNED_TITLES = new HashSet<>();
    private static final String SPACE = "[ ]+";

    /**
     * Method that saves new title in title file
     * @param newTitleDTO title DTO
     */
    public void learnNewTitle(NewTitleDTO newTitleDTO) {
        String transformedTitleToLearn = newTitleDTO.getNewTitle().trim().toLowerCase();
        String[] splitTitle = transformedTitleToLearn.split(SPACE);
        if (splitTitle.length > 1) {
            LEARNED_TITLES.addAll(Arrays.asList(splitTitle));
        } else {
            LEARNED_TITLES.add(newTitleDTO.getNewTitle().trim().toLowerCase());
        }
    }

    /**
     * Method that tells if a title has been learned
     * @param possibleTitle possible learned title to be checked
     * @return true if title is learned, false else
     */
    public boolean titleIsLearned(String possibleTitle) {
        return LEARNED_TITLES.contains(possibleTitle.trim().toLowerCase());
    }
}
