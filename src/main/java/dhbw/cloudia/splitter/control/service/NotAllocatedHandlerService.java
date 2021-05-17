package dhbw.cloudia.splitter.control.service;

import dhbw.cloudia.splitter.boundary.dto.ContactDTO;
import dhbw.cloudia.splitter.control.contactstringpart.ContactParts;
import dhbw.cloudia.splitter.control.exception.ContactParsingException;
import dhbw.cloudia.splitter.control.helper.ContactPartAllocation;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for handling the fact of not allocated contact parts
 */
@Service
public class NotAllocatedHandlerService {

    /**
     * Method handling not allocated contact parts
     * If certain assumption take place the service throws an exception triggering a HTTP 400 signaling
     * the user to check the final contact DTO.
     * @param contactPartAllocationList contact part list
     * @param contactDTO contact DTO to be updated
     */
    public void handleNotAllocatedParts(List<ContactPartAllocation> contactPartAllocationList, ContactDTO contactDTO) {
        List<ContactPartAllocation> notAllocatedParts = contactPartAllocationList.stream().filter(part ->
                ContactParts.NOT_ALLOCATED.equals(part.getContactStringPart()))
                .sorted(Comparator.comparingInt(part -> part.getContactPart().getFirstObject()))
                .collect(Collectors.toList());

        // everything is ok. return.
        if (notAllocatedParts.isEmpty()) return;

        // if last name is not filled and one not allocated contact part is available, set it as last name.
        if (notAllocatedParts.size() == 1 && contactDTO.getLastName().isEmpty()) {
            contactDTO.setLastName(contactPartAllocationList.get(0).getContactPart().getSecondObject());

        // if first name is not filled and one not allocated contact part is available, set it as first name.
        } else if (notAllocatedParts.size() == 1 && contactDTO.getFirstName().isEmpty()) {
            contactDTO.setFirstName(contactPartAllocationList.get(0).getContactPart().getSecondObject());

        // if there are multiple not allocated parts and the last name is empty set the last part as last name and the
        // rest as first name
        } else if (notAllocatedParts.size() > 1 && contactDTO.getLastName().isEmpty()) {
            int maxPositionInNotAllocatedParts = notAllocatedParts.stream().mapToInt(part -> part.getContactPart().getFirstObject()).max().orElse(-1);
            notAllocatedParts.forEach(part -> {
                if (part.getContactPart().getFirstObject() == maxPositionInNotAllocatedParts) {
                    contactDTO.setLastName(contactDTO.getLastName() + part.getContactPart().getSecondObject());
                } else {
                    contactDTO.setFirstName(contactDTO.getFirstName() + " " + part.getContactPart().getSecondObject());
                }
            });

        // if there are multiple not allocated parts just use them as a first name when the last name is already filled
        } else if (notAllocatedParts.size() > 1) {
            notAllocatedParts.forEach(part -> contactDTO.setFirstName(contactDTO.getFirstName() + " " + part.getContactPart().getSecondObject()));
        }
        throw new ContactParsingException(contactDTO, "Not all contact parts could be allocated. Please check the solution");
    }
}
