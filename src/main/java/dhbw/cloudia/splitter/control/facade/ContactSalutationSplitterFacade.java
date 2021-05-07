package dhbw.cloudia.splitter.control.facade;

import dhbw.cloudia.splitter.boundary.dto.ContactDTO;
import dhbw.cloudia.splitter.boundary.dto.ContactStringInputTO;
import dhbw.cloudia.splitter.control.service.ContactSplitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Facade spring component controlling the workflow of the contact splitting by
 * delegating the necessary tasks to spring services.
 */
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ContactSalutationSplitterFacade {

    private final ContactSplitterService contactSplitterService;

    /**
     * Central workflow method for splitting the contact (salutation)
     * @param contactStringInputTO incoming contact string that gets parsed
     * @return final split contact DTO containing the split contact
     */
    public ContactDTO parseContactStringInput(ContactStringInputTO contactStringInputTO) {
        return null;
    }
}
