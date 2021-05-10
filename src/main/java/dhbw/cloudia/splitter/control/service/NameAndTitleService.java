package dhbw.cloudia.splitter.control.service;

import dhbw.cloudia.splitter.boundary.dto.ContactDTO;
import dhbw.cloudia.splitter.control.contactstringpart.ContactParts;
import dhbw.cloudia.splitter.control.exception.ContactParsingException;
import dhbw.cloudia.splitter.control.helper.ContactPartAllocation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class building the name and title part of the contact
 */
@Service
public class NameAndTitleService {

    private static final String SPACE = " ";
    private static final String LAST_NAME_SEPARATION = "-";

    /**
     * Method building the name and title part of the contact by taking the allocated
     * contact parts and concat the containing strings for each contact part.
     * @param contactPartAllocationList contact part allocations in a list
     * @param contactDTO final contact DTO
     * @return updated contact DTO
     */
    public ContactDTO setNameAndTitle(List<ContactPartAllocation> contactPartAllocationList, ContactDTO contactDTO) {
        // creating string builders for contact parts
        StringBuilder titleBuilder = new StringBuilder();
        StringBuilder firstNameBuilder = new StringBuilder();
        StringBuilder lastNameBuilder = new StringBuilder();

        // last name prefixes get put in front of last name so there is an index on there the next prefix should be
        int prefixLengthCounter = 0;

        // iterate through name, last name and prefix contact parts and apply string builders
        for (ContactPartAllocation contactPartAllocation : contactPartAllocationList.stream().filter(this::isNameOrTitle).collect(Collectors.toList())) {
            switch (contactPartAllocation.getContactStringPart()) {
                case FIRST_NAME:
                    firstNameBuilder.append(SPACE).append(contactPartAllocation.getContactPart().getSecondObject());
                    break;
                case LAST_NAME:
                    lastNameBuilder.append(LAST_NAME_SEPARATION).append(contactPartAllocation.getContactPart().getSecondObject());
                    break;
                case LAST_NAME_PREFIX:
                    lastNameBuilder.insert(prefixLengthCounter, contactPartAllocation.getContactPart().getSecondObject() + SPACE);
                    prefixLengthCounter += (contactPartAllocation.getContactPart().getSecondObject() + SPACE).length();
                    break;
                case TITLE:
                    titleBuilder.append(SPACE).append(contactPartAllocation.getContactPart().getSecondObject());
                    break;
                default:
                    throw new ContactParsingException(contactDTO, "Allocation error");
            }
        }

        // set final strings to contact
        contactDTO.setFirstName(firstNameBuilder.toString().trim());
        contactDTO.setLastName(transformLastName(lastNameBuilder.toString()));
        contactDTO.setTitle(titleBuilder.toString().trim());
        return contactDTO;
    }

    /**
     * Private method correcting the last name string because the string building can create errors
     * @param lastName last name string from builder
     * @return correct last name string
     */
    private String transformLastName(String lastName) {
        String name = lastName.trim();
        if (lastName.startsWith("-")) name = lastName.substring(1);
        return name.replace(" -", SPACE);
    }

    private boolean isNameOrTitle(ContactPartAllocation contactStringPart) {
        return ContactParts.FIRST_NAME.equals(contactStringPart.getContactStringPart()) ||
                ContactParts.LAST_NAME.equals(contactStringPart.getContactStringPart()) ||
                ContactParts.LAST_NAME_PREFIX.equals(contactStringPart.getContactStringPart()) ||
                ContactParts.TITLE.equals(contactStringPart.getContactStringPart());
    }
}
