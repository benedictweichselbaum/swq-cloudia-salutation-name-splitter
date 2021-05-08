package dhbw.cloudia.splitter.control.service;

import dhbw.cloudia.splitter.boundary.dto.ContactDTO;
import dhbw.cloudia.splitter.control.contactstringpart.ContactStringPart;
import dhbw.cloudia.splitter.control.exception.ContactParsingException;
import dhbw.cloudia.splitter.control.helper.ContactPartAllocation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotAllocatedHandlerService {

    public void handleNotAllocatedParts(List<ContactPartAllocation> contactPartAllocationList, ContactDTO contactDTO) {
        List<ContactPartAllocation> notAllocatedParts = contactPartAllocationList.stream().filter(part ->
                ContactStringPart.NOT_ALLOCATED.equals(part.getContactStringPart())).collect(Collectors.toList());
        if (notAllocatedParts.size() == 1 && contactDTO.getLastName().isEmpty()) {
            contactDTO.setLastName(contactPartAllocationList.get(0).getContactPart().getSecondObject());
        } else {
            throw new ContactParsingException(contactDTO, "Not all contact parts could be allocated");
        }
    }
}
