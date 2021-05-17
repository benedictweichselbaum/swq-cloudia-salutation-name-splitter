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
        new Tuple<>("/contact_info/last_name_prefix.txt", ContactParts.LAST_NAME_PREFIX),
        new Tuple<>("/contact_info/german_last_names.txt", ContactParts.LAST_NAME),
        new Tuple<>("/contact_info/german_first_names.txt", ContactParts.FIRST_NAME)
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

        // iterate over files
        for (Tuple<String, ContactParts> fileTuple : FILES) {
            List<Tuple<Integer, String>> partsToRemove = new ArrayList<>();
            // iterate over contact parts
            for (Tuple<Integer, String> currentContactTuple : contactParts) {
                String contactPart = getStringFromTupleAndCheckForSalutation(fileTuple, currentContactTuple);

                if (contactPart.contains("-")) { // special case for contact parts that are separated by a dash
                    handleDashSeparatedContactParts(contactPartAllocationList, fileTuple, partsToRemove, currentContactTuple, contactPart);
                } else if (this.stringInFileCheckerService.stringIsInFile(contactPart, fileTuple.getFirstObject())) { // case if contact part is part of a file
                    handleAllocationMatch(contactPartAllocationList, initialContactPartsSize, fileTuple, partsToRemove, currentContactTuple, contactPart, contactParts);
                }
            }

            // remove parts if they are allocated
            contactParts.removeAll(partsToRemove);
        }
        // Mark not allocated parts
        contactParts.forEach(part -> contactPartAllocationList.add(new ContactPartAllocation(part, ContactParts.NOT_ALLOCATED)));

        return contactPartAllocationList;
    }

    private void handleAllocationMatch(List<ContactPartAllocation> contactPartAllocationList,
                                       int initialContactPartsSize,
                                       Tuple<String, ContactParts> fileTuple,
                                       List<Tuple<Integer, String>> partsToRemove,
                                       Tuple<Integer, String> currentContactTuple,
                                       String contactPart,
                                       List<Tuple<Integer, String>> contactParts) {

        // fix to prevent a ConcurrentModificationException
        if (ContactParts.LAST_NAME_PREFIX.equals(fileTuple.getSecondObject()) && partsToRemove.contains(currentContactTuple)) {
            return;

        // if a last name prefix is found everything after it is also a prefix or the last name itself.
        } else if (ContactParts.LAST_NAME_PREFIX.equals(fileTuple.getSecondObject())) {
            contactParts.stream().filter(part -> part.getFirstObject() >= currentContactTuple.getFirstObject()).forEachOrdered(
                    tuple -> {
                        // is a prefix
                        if (this.stringInFileCheckerService.stringIsInFile(tuple.getSecondObject(), FILES.get(2).getFirstObject())) {
                            contactPartAllocationList.add(new ContactPartAllocation(tuple, ContactParts.LAST_NAME_PREFIX));
                        } else {
                            contactPartAllocationList.add(new ContactPartAllocation(tuple, ContactParts.LAST_NAME));
                        }
                        partsToRemove.add(tuple);
                    }
            );

        // if something seems to be a last name but it is not in the last position its either a last name prefix or a first name
        } else if (currentContactTuple.getFirstObject() != initialContactPartsSize - 1 && ContactParts.LAST_NAME.equals(fileTuple.getSecondObject())) {
            if (this.stringInFileCheckerService.stringIsInFile(contactPart, FILES.get(4).getFirstObject())) {
                contactPartAllocationList.add(new ContactPartAllocation(new Tuple<>(currentContactTuple.getFirstObject(), contactPart), ContactParts.LAST_NAME_PREFIX));
            } else {
                contactPartAllocationList.add(new ContactPartAllocation(new Tuple<>(currentContactTuple.getFirstObject(), contactPart), ContactParts.FIRST_NAME));
            }
            partsToRemove.add(currentContactTuple);

        // normal allocation according to current file that is being checked
        } else {
            contactPartAllocationList.add(new ContactPartAllocation(new Tuple<>(currentContactTuple.getFirstObject(), contactPart), fileTuple.getSecondObject()));
            partsToRemove.add(currentContactTuple);
        }
    }

    private String getStringFromTupleAndCheckForSalutation(Tuple<String, ContactParts> tuple, Tuple<Integer, String> currentTuple) {
        String contactPart = currentTuple.getSecondObject();

        // if salutation has a dot in it, ignore it
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

        // Dash separated contact parts are either titles or last names if they do not contain a dot.
        if (ContactParts.TITLE.equals(tuple.getSecondObject()) && this.stringInFileCheckerService.stringIsInFile(contactPart, tuple.getFirstObject())) {
            contactPartAllocationList.add(new ContactPartAllocation(new Tuple<>(currentTuple.getFirstObject(), contactPart), ContactParts.TITLE));
        } else if (!contactPart.contains(".")) {
            Arrays.asList(contactPart.split("-")).forEach(lastName ->
                    contactPartAllocationList.add(new ContactPartAllocation(new Tuple<>(currentTuple.getFirstObject(), lastName), ContactParts.LAST_NAME)));
        }
        partsToRemove.add(currentTuple);
    }
}
