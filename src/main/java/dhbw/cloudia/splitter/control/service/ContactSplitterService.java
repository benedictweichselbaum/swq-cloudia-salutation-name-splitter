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

    public List<String> splitContactString(ContactStringInputTO contactInput){
        return Arrays.stream(contactInput.getInput().split(SEPARATION_CHARACTER)).collect(Collectors.toList());
    }
}
