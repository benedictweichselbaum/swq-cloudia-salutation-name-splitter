package dhbw.cloudia.splitter.control.facade;

import dhbw.cloudia.splitter.boundary.dto.ContactDTO;
import dhbw.cloudia.splitter.boundary.dto.ContactStringInputTO;
import dhbw.cloudia.splitter.control.helper.ContactPartAllocation;
import dhbw.cloudia.splitter.control.helper.Tuple;
import dhbw.cloudia.splitter.control.service.ContactPartAllocationService;
import dhbw.cloudia.splitter.control.service.ContactSplitterService;
import dhbw.cloudia.splitter.control.service.NameAndTitleService;
import dhbw.cloudia.splitter.control.service.NotAllocatedHandlerService;
import dhbw.cloudia.splitter.control.service.OnlyOneContactPartService;
import dhbw.cloudia.splitter.control.service.SalutationService;
import dhbw.cloudia.splitter.control.service.TitleAllocationOptimizationService;
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
    private final NameAndTitleService nameAndTitleService;
    private final SalutationService salutationService;
    private final OnlyOneContactPartService onlyOneContactPartService;
    private final TitleAllocationOptimizationService titleAllocationOptimizationService;
    private final NotAllocatedHandlerService notAllocatedHandlerService;

    /**
     * Central workflow method for splitting the contact (salutation)
     * @param contactStringInputTO incoming contact string that gets parsed
     * @return final split contact DTO containing the split contact
     */
    public ContactDTO parseContactStringInput(ContactStringInputTO contactStringInputTO) {
        // split contact
        List<Tuple<Integer, String>> splitContact = this.contactSplitterService.splitContactString(contactStringInputTO);
        // edge case: only one contact part
        if (splitContact.size() == 1) {
            return this.onlyOneContactPartService.handleOnlyOneArgument(splitContact.get(0).getSecondObject());
        }
        // rest of workflow
        ContactDTO contactDTO = new ContactDTO();
        List<ContactPartAllocation> contactPartAllocationList = this.contactPartAllocationService.allocateContactParts(splitContact);
        contactPartAllocationList = this.titleAllocationOptimizationService.optimizeAllocation(contactPartAllocationList);
        contactDTO = this.nameAndTitleService.setNameAndTitle(contactPartAllocationList, contactDTO);
        contactDTO = this.salutationService.setSexAndLetterSalutation(contactPartAllocationList, contactDTO);
        this.notAllocatedHandlerService.handleNotAllocatedParts(contactPartAllocationList, contactDTO);

        // return final contact
        return contactDTO;
    }
}
