package dhbw.cloudia.splitter.control.service;

import dhbw.cloudia.splitter.control.contactstringpart.ContactStringPart;
import dhbw.cloudia.splitter.control.helper.ContactPartAllocation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ContactPartAllocationService {

    private static final Map<String, ContactStringPart> FILES = Map.of(
            "classpath:contact_info/titles.txt", ContactStringPart.TITLE,
            "classpath:contact_info/salutations.txt", ContactStringPart.SALUTATION,
            "classpath:contact_info/german_first_names.txt", ContactStringPart.FIRST_NAME,
            "classpath:contact_info/german_last_names.txt", ContactStringPart.LAST_NAME,
            "classpath:contact_info/last_name_prefix.txt", ContactStringPart.LAST_NAME_PREFIX
    );

    private final StringInFileCheckerService stringInFileCheckerService;

    public List<ContactPartAllocation> allocateContactParts(List<String> contactParts) {
        List<ContactPartAllocation> contactPartAllocationList = new ArrayList<>();
        FILES.forEach((k, v) -> {
            List<String> partsToRemove = new ArrayList<>();
            for (String contactPart : contactParts) {
                contactPart = contactPart.replace(".", "");
                if (contactPart.contains("-")) {
                    Arrays.asList(contactPart.split("-")).forEach(lastName ->
                            contactPartAllocationList.add(new ContactPartAllocation(lastName, ContactStringPart.LAST_NAME)));
                    partsToRemove.add(contactPart);
                } else if (this.stringInFileCheckerService.stringIsInFile(contactPart, k)) {
                    contactPartAllocationList.add(new ContactPartAllocation(contactPart, v));
                    partsToRemove.add(contactPart);
                }
            }
            contactParts.removeAll(partsToRemove);
        });
        contactParts.forEach(part -> contactPartAllocationList.add(new ContactPartAllocation(part, ContactStringPart.NOT_ALLOCATED)));
        return contactPartAllocationList;
    }
}
