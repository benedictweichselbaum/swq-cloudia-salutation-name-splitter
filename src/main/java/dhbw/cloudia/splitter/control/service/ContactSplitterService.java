package dhbw.cloudia.splitter.control.service;

import dhbw.cloudia.splitter.boundary.dto.ContactStringInputTO;
import dhbw.cloudia.splitter.control.helper.Tuple;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for parsing the contact
 */
@Service
public class ContactSplitterService {

    private static final String SEPARATION_CHARACTER = "[ ]+";
    public static final String COMMA = ",";

    public List<Tuple<Integer, String>> splitContactString(ContactStringInputTO contactInput) {
        List<Tuple<Integer, String>> splitContact = new ArrayList<>();
        int index = 0;
        for (String s : contactInput.getInput().split(SEPARATION_CHARACTER)) {
            splitContact.add(new Tuple<>(index, s.replace(COMMA, "")));
            index++;
        }
        return splitContact;
    }
}
