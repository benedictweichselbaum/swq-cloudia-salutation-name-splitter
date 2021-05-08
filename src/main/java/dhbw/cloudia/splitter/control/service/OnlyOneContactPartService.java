package dhbw.cloudia.splitter.control.service;

import dhbw.cloudia.splitter.boundary.dto.ContactDTO;
import org.springframework.stereotype.Service;

@Service
public class OnlyOneContactPartService {

    private static final String DEFAULT_LETTER_SALUTATION = "Sehr geehrte Damen und Herren";

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
