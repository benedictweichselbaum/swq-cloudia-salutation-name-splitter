package dhbw.cloudia.splitter.control.service;

import dhbw.cloudia.splitter.control.contactstringpart.ContactParts;
import dhbw.cloudia.splitter.control.helper.ContactPartAllocation;
import dhbw.cloudia.splitter.control.helper.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Service that allocates contact parts according to the reference files.
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ContactPartAllocationService {

    // Do not change order
    private static final List<Tuple<String, ContactParts>> FILES = Arrays.asList(
        new Tuple<>("/contact_info/titles.txt", ContactParts.TITLE),
        new Tuple<>("/contact_info/salutations.txt", ContactParts.SALUTATION),
        new Tuple<>("/contact_info/german_last_names.txt", ContactParts.LAST_NAME),
        new Tuple<>("/contact_info/german_first_names.txt", ContactParts.FIRST_NAME),
        new Tuple<>("/contact_info/last_name_prefix.txt", ContactParts.LAST_NAME_PREFIX)
    );

    private final StringInFileCheckerService stringInFileCheckerService;

    /**
     * Method creating allocation for the contact parts. Allocates string to different contact categories.
     * Has multiple subroutines that cover edge cases
     * @param contactParts split contact parts from split service
     * @return contact part allocations
     */
    public List<ContactPartAllocation> allocateContactParts(List<Tuple<Integer, String>> contactParts) {
        List<ContactPartAllocation> contactPartAllocationList = new ArrayList<>();
        int initialContactPartsSize = contactParts.size();

        FILES.forEach(tuple -> {
            List<Tuple<Integer, String>> partsToRemove = new ArrayList<>();
            for (Tuple<Integer, String> currentTuple : contactParts) {
                String contactPart = getStringFromTupleAndCheckForSalutation(tuple, currentTuple);

                if (contactPart.contains("-")) {
                    handleDashSeparatedContactParts(contactPartAllocationList, tuple, partsToRemove, currentTuple, contactPart);
                } else if (this.stringInFileCheckerService.stringIsInFile(contactPart, tuple.getFirstObject())) {
                    handleAllocationMatch(contactPartAllocationList, initialContactPartsSize, tuple, partsToRemove, currentTuple, contactPart);
                }
            }

            // remove parts if they are allocated
            contactParts.removeAll(partsToRemove);
        });

        // Mark not allocated parts
        contactParts.forEach(part -> contactPartAllocationList.add(new ContactPartAllocation(part, ContactParts.NOT_ALLOCATED)));

        return contactPartAllocationList;
    }

    private void handleAllocationMatch(List<ContactPartAllocation> contactPartAllocationList,
                                       int initialContactPartsSize,
                                       Tuple<String, ContactParts> tuple,
                                       List<Tuple<Integer, String>> partsToRemove,
                                       Tuple<Integer, String> currentTuple,
                                       String contactPart) {
        if (currentTuple.getFirstObject() != initialContactPartsSize -1 && ContactParts.LAST_NAME.equals(tuple.getSecondObject())) {
            if (this.stringInFileCheckerService.stringIsInFile(contactPart, FILES.get(4).getFirstObject())) {
                contactPartAllocationList.add(new ContactPartAllocation(new Tuple<>(currentTuple.getFirstObject(), contactPart), ContactParts.LAST_NAME_PREFIX));
            } else {
                contactPartAllocationList.add(new ContactPartAllocation(new Tuple<>(currentTuple.getFirstObject(), contactPart), ContactParts.FIRST_NAME));
            }
        } else {
            contactPartAllocationList.add(new ContactPartAllocation(new Tuple<>(currentTuple.getFirstObject(), contactPart), tuple.getSecondObject()));
        }
        partsToRemove.add(currentTuple);
    }

    private String getStringFromTupleAndCheckForSalutation(Tuple<String, ContactParts> tuple, Tuple<Integer, String> currentTuple) {
        String contactPart = currentTuple.getSecondObject();

        if (ContactParts.SALUTATION.equals(tuple.getSecondObject())) {
            contactPart = contactPart.replace(".", "");
        }
        return contactPart;
    }

    private void handleDashSeparatedContactParts(List<ContactPartAllocation> contactPartAllocationList,
                                                 Tuple<String, ContactParts> tuple,
                                                 List<Tuple<Integer, String>> partsToRemove, 
                                                 Tuple<Integer, String> currentTuple, 
                                                 String contactPart) {
        if (ContactParts.TITLE.equals(tuple.getSecondObject()) && this.stringInFileCheckerService.stringIsInFile(contactPart, tuple.getFirstObject())) {
            contactPartAllocationList.add(new ContactPartAllocation(new Tuple<>(currentTuple.getFirstObject(), contactPart), ContactParts.TITLE));
        } else if (!contactPart.contains(".")) {
            Arrays.asList(contactPart.split("-")).forEach(lastName ->
                    contactPartAllocationList.add(new ContactPartAllocation(new Tuple<>(currentTuple.getFirstObject(), lastName), ContactParts.LAST_NAME)));
        }
        partsToRemove.add(currentTuple);
    }
}
