package dhbw.cloudia.splitter.control.service;

import dhbw.cloudia.splitter.boundary.dto.ContactStringInputTO;
import dhbw.cloudia.splitter.control.helper.Tuple;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for parsing the contact by splitting string at spaces.
 */
@Service
public class ContactSplitterService {

    private static final String SEPARATION_CHARACTER = "[ ]+";
    public static final String COMMA = ",";

    /**
     * Method spliting the incoming string at spaces determining the single contact parts that
     * need to be allocated
     * @param contactInput input object coming from the client
     * @return contact part tuples containing string and position in string (as index starting at 0)
     */
    public List<Tuple<Integer, String>> splitContactString(ContactStringInputTO contactInput) {
        List<Tuple<Integer, String>> splitContact = new ArrayList<>();
        int index = 0;
        for (String s : contactInput.getContactString().split(SEPARATION_CHARACTER)) {
            splitContact.add(new Tuple<>(index, s.replace(COMMA, "")));
            index++;
        }
        return splitContact;
    }
}
