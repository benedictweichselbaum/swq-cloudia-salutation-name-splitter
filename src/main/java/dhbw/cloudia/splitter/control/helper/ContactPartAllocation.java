package dhbw.cloudia.splitter.control.helper;

import dhbw.cloudia.splitter.control.contactstringpart.ContactStringPart;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContactPartAllocation {
    private Tuple<Integer, String> contactPart;
    private ContactStringPart contactStringPart;
}
