package dhbw.cloudia.splitter.control.facade;

import dhbw.cloudia.splitter.boundary.dto.ContactDTO;
import dhbw.cloudia.splitter.boundary.dto.ContactStringInputTO;
import dhbw.cloudia.splitter.control.helper.ContactPartAllocation;
import dhbw.cloudia.splitter.control.service.ContactPartAllocationService;
import dhbw.cloudia.splitter.control.service.ContactSplitterService;
import dhbw.cloudia.splitter.control.service.NameAndTitleAllocationService;
import dhbw.cloudia.splitter.control.service.SalutationAllocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Facade spring component controlling the workflow of the contact splitting by
 * delegating the necessary tasks to spring services.
 */
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ContactSalutationSplitterFacade {

    private final ContactSplitterService contactSplitterService;
    private final ContactPartAllocationService contactPartAllocationService;
    private final NameAndTitleAllocationService nameAndTitleAllocationService;
    private final SalutationAllocationService salutationAllocationService;

    /**
     * Central workflow method for splitting the contact (salutation)
     * @param contactStringInputTO incoming contact string that gets parsed
     * @return final split contact DTO containing the split contact
     */
    public ContactDTO parseContactStringInput(ContactStringInputTO contactStringInputTO) {
        ContactDTO contactDTO = new ContactDTO();
        List<String> splitContact = this.contactSplitterService.splitContactString(contactStringInputTO);
        List<ContactPartAllocation> contactPartAllocationList = this.contactPartAllocationService.allocateContactParts(splitContact);
        this.nameAndTitleAllocationService.setNameAndTitle(contactPartAllocationList, contactDTO);
        this.salutationAllocationService.setSexAndLetterSalutation(contactPartAllocationList, contactDTO);
        return contactDTO;
    }
}
