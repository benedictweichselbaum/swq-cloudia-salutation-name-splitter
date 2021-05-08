package dhbw.cloudia.splitter.control.service;

import dhbw.cloudia.splitter.boundary.dto.ContactStringInputTO;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for parsing the contact
 */
@Service
public class ContactSplitterService {

    private static final String SEPARATION_CHARACTER = " ";
    public static final String COMMA = ",";

    public List<String> splitContactString(ContactStringInputTO contactInput){
        return Arrays.stream(contactInput.getInput().split(SEPARATION_CHARACTER))
                .map(part -> part.trim().replace(COMMA, "")).collect(Collectors.toList());
    }
}
