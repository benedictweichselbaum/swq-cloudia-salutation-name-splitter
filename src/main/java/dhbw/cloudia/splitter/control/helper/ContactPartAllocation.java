package dhbw.cloudia.splitter.control.helper;

import dhbw.cloudia.splitter.control.contactstringpart.ContactParts;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Helper class. Representing the contact part allocation
 */
@Data
@AllArgsConstructor
public class ContactPartAllocation {
    private Tuple<Integer, String> contactPart;
    private ContactParts contactStringPart;
}
