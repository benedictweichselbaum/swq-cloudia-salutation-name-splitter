package dhbw.cloudia.splitter.control.facade;

import dhbw.cloudia.splitter.boundary.dto.ContactInputTO;
import dhbw.cloudia.splitter.boundary.dto.ContactOutputTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Facade class controlling the of the application (the parsing of the address)
 */
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ContactSplitterFacade {

    /**
     * Method containing and executing the workflow for parsing the address string.
     * @param contactInputTO address input TO
     * @return parsed and split address
     */
    public ContactOutputTO parseAddressToOutputTO(ContactInputTO contactInputTO) {
        return null;
    }
}
