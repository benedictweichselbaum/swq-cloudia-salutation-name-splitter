package dhbw.cloudia.splitter.control.service;

import dhbw.cloudia.splitter.boundary.dto.ContactDTO;
import org.springframework.stereotype.Service;

/**
 * Service class handling the if the contact only has one part.
 */
@Service
public class OnlyOneContactPartService {

    private static final String DEFAULT_LETTER_SALUTATION = "Sehr geehrte Damen und Herren";

    /**
     * Method handling a contact input with only one part.
     * Assumption: the one part represents the last name
     * @param contactPart string of the only contact part
     * @return final contact DTO
     */
    public ContactDTO handleOnlyOneArgument(String contactPart) {
        return ContactDTO.builder()
                .lastName(contactPart)
                .letterSalutation(DEFAULT_LETTER_SALUTATION)
                .salutation("")
                .gender("-")
                .firstName("")
                .title("")
                .build();
    }
}
