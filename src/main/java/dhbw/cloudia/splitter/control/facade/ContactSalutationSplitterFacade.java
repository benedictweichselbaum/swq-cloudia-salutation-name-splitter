package dhbw.cloudia.splitter.control.facade;

import dhbw.cloudia.splitter.boundary.dto.ContactDTO;
import dhbw.cloudia.splitter.boundary.dto.ContactStringInputTO;
import dhbw.cloudia.splitter.control.helper.ContactPartAllocation;
import dhbw.cloudia.splitter.control.helper.Tuple;
import dhbw.cloudia.splitter.control.service.AllocationOptimizationService;
import dhbw.cloudia.splitter.control.service.ContactPartAllocationService;
import dhbw.cloudia.splitter.control.service.ContactSplitterService;
import dhbw.cloudia.splitter.control.service.NameAndTitleAllocationService;
import dhbw.cloudia.splitter.control.service.NotAllocatedHandlerService;
import dhbw.cloudia.splitter.control.service.OnlyOneContactPartService;
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
    private final OnlyOneContactPartService onlyOneContactPartService;
    private final AllocationOptimizationService allocationOptimizationService;
    private final NotAllocatedHandlerService notAllocatedHandlerService;

    /**
     * Central workflow method for splitting the contact (salutation)
     * @param contactStringInputTO incoming contact string that gets parsed
     * @return final split contact DTO containing the split contact
     */
    public ContactDTO parseContactStringInput(ContactStringInputTO contactStringInputTO) {
        List<Tuple<Integer, String>> splitContact = this.contactSplitterService.splitContactString(contactStringInputTO);
        if (splitContact.size() == 1) {
            return this.onlyOneContactPartService.handleOnlyOneArgument(splitContact.get(0).getSecondObject());
        }

        ContactDTO contactDTO = new ContactDTO();

        List<ContactPartAllocation> contactPartAllocationList = this.contactPartAllocationService.allocateContactParts(splitContact);
        contactPartAllocationList = this.allocationOptimizationService.optimizeAllocation(contactPartAllocationList);
        contactDTO = this.nameAndTitleAllocationService.setNameAndTitle(contactPartAllocationList, contactDTO);
        contactDTO = this.salutationAllocationService.setSexAndLetterSalutation(contactPartAllocationList, contactDTO);
        this.notAllocatedHandlerService.handleNotAllocatedParts(contactPartAllocationList, contactDTO);
        return contactDTO;
    }
}
