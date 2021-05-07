package dhbw.cloudia.splitter.control.service;

import dhbw.cloudia.splitter.boundary.dto.ContactDTO;
import dhbw.cloudia.splitter.boundary.dto.ContactStringInputTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service class for parsing the contact
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ContactSplitterService {

    public ContactDTO parseContact(ContactStringInputTO contactInput){
        return null;
    }
}
