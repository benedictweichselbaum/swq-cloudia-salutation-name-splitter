package dhbw.cloudia.splitter.control;

import dhbw.cloudia.splitter.boundary.dto.ContactDTO;
import dhbw.cloudia.splitter.boundary.dto.StringContactInputDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for parsing the contact
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SalutationNameParsingService {

    public ContactDTO parseContact(StringContactInputDTO contactInput){
        return null;
    }
}
