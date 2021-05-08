package dhbw.cloudia.splitter.control.service;

import dhbw.cloudia.splitter.control.contactstringpart.ContactStringPart;
import dhbw.cloudia.splitter.control.helper.ContactPartAllocation;
import dhbw.cloudia.splitter.control.helper.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ContactPartAllocationService {

    // Do not change order
    private static final List<Tuple<String, ContactStringPart>> FILES = Arrays.asList(
        new Tuple<>("classpath:contact_info/titles.txt", ContactStringPart.TITLE),
        new Tuple<>("classpath:contact_info/salutations.txt", ContactStringPart.SALUTATION),
        new Tuple<>("classpath:contact_info/german_last_names.txt", ContactStringPart.LAST_NAME),
        new Tuple<>("classpath:contact_info/german_first_names.txt", ContactStringPart.FIRST_NAME),
        new Tuple<>("classpath:contact_info/last_name_prefix.txt", ContactStringPart.LAST_NAME_PREFIX)
    );

    private final StringInFileCheckerService stringInFileCheckerService;

    public List<ContactPartAllocation> allocateContactParts(List<Tuple<Integer, String>> contactParts) {
        List<ContactPartAllocation> contactPartAllocationList = new ArrayList<>();
        FILES.forEach(tuple -> {
            List<Tuple<Integer, String>> partsToRemove = new ArrayList<>();
            for (Tuple<Integer, String> currentTuple : contactParts) {
                String contactPart = currentTuple.getSecondObject();
                if (ContactStringPart.SALUTATION.equals(tuple.getSecondObject())) {
                    contactPart = contactPart.replace(".", "");
                }
                if (contactPart.contains("-")) {
                    if (ContactStringPart.TITLE.equals(tuple.getSecondObject()) && this.stringInFileCheckerService.stringIsInFile(contactPart, tuple.getFirstObject())) {
                        contactPartAllocationList.add(new ContactPartAllocation(new Tuple<>(currentTuple.getFirstObject(), contactPart), ContactStringPart.TITLE));
                    } else if (!contactPart.contains(".")) {
                        Arrays.asList(contactPart.split("-")).forEach(lastName ->
                                contactPartAllocationList.add(new ContactPartAllocation(new Tuple<>(currentTuple.getFirstObject(), lastName), ContactStringPart.LAST_NAME)));
                    }
                    partsToRemove.add(currentTuple);
                } else if (this.stringInFileCheckerService.stringIsInFile(contactPart, tuple.getFirstObject())) {
                    contactPartAllocationList.add(new ContactPartAllocation(new Tuple<>(currentTuple.getFirstObject(), contactPart), tuple.getSecondObject()));
                    partsToRemove.add(currentTuple);
                }
            }
            contactParts.removeAll(partsToRemove);
        });
        contactParts.forEach(part -> contactPartAllocationList.add(new ContactPartAllocation(part, ContactStringPart.NOT_ALLOCATED)));
        return contactPartAllocationList;
    }
}
