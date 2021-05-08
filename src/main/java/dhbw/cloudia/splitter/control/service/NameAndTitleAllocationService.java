package dhbw.cloudia.splitter.control.service;

import dhbw.cloudia.splitter.boundary.dto.ContactDTO;
import dhbw.cloudia.splitter.control.contactstringpart.ContactStringPart;
import dhbw.cloudia.splitter.control.exception.ContactParsingException;
import dhbw.cloudia.splitter.control.helper.ContactPartAllocation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NameAndTitleAllocationService {

    public static final String SPACE = " ";
    public static final String LAST_NAME_SEPARATION = "-";

    public void setNameAndTitle(List<ContactPartAllocation> contactPartAllocationList, ContactDTO contactDTO) {
        StringBuilder titleBuilder = new StringBuilder();
        StringBuilder firstNameBuilder = new StringBuilder();
        StringBuilder lastNameBuilder = new StringBuilder();
        for (ContactPartAllocation contactPartAllocation : contactPartAllocationList.stream().filter(this::isNameOrTitle).collect(Collectors.toList())) {
            switch (contactPartAllocation.getContactStringPart()) {
                case FIRST_NAME:
                    firstNameBuilder.append(SPACE).append(contactPartAllocation.getContactPart().getSecondObject());
                    break;
                case LAST_NAME:
                    lastNameBuilder.append(LAST_NAME_SEPARATION).append(contactPartAllocation.getContactPart().getSecondObject());
                    break;
                case LAST_NAME_PREFIX:
                    lastNameBuilder.insert(0, contactPartAllocation.getContactPart().getSecondObject() + SPACE);
                    break;
                case TITLE:
                    titleBuilder.append(SPACE).append(contactPartAllocation.getContactPart().getSecondObject());
                    break;
                default:
                    throw new ContactParsingException(contactDTO, "Allocation error");
            }
        }
        contactDTO.setFirstName(firstNameBuilder.toString().trim());
        contactDTO.setLastName(transformLastName(lastNameBuilder.toString()));
        contactDTO.setTitle(titleBuilder.toString().trim());
    }

    private String transformLastName(String lastName) {
        String name = lastName.trim();
        if (lastName.startsWith("-")) name = lastName.substring(1);
        return name.replace(" -", SPACE);
    }

    private boolean isNameOrTitle(ContactPartAllocation contactStringPart) {
        return ContactStringPart.FIRST_NAME.equals(contactStringPart.getContactStringPart()) ||
                ContactStringPart.LAST_NAME.equals(contactStringPart.getContactStringPart()) ||
                ContactStringPart.LAST_NAME_PREFIX.equals(contactStringPart.getContactStringPart()) ||
                ContactStringPart.TITLE.equals(contactStringPart.getContactStringPart());
    }
}
