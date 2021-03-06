package dhbw.cloudia.splitter.control.service;

import dhbw.cloudia.splitter.boundary.dto.ContactDTO;
import dhbw.cloudia.splitter.control.contactstringpart.ContactParts;
import dhbw.cloudia.splitter.control.exception.ContactParsingException;
import dhbw.cloudia.splitter.control.helper.ContactPartAllocation;
import dhbw.cloudia.splitter.control.helper.Tuple;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class determining the (letter) salutation for the contact
 */
@Service
public class SalutationService {

    private static final Map<String, Tuple<String, String>> SALUTATION_ALLOCATION_MAP = new HashMap<>();

    static {
        SALUTATION_ALLOCATION_MAP.put("herr", new Tuple<>("M", "Sehr geehrter Herr "));
        SALUTATION_ALLOCATION_MAP.put("frau", new Tuple<>("F", "Sehr geehrte Frau "));
        SALUTATION_ALLOCATION_MAP.put("herrn", new Tuple<>("H", "Sehr geehrter Herr "));
        SALUTATION_ALLOCATION_MAP.put("mrs", new Tuple<>("F", "Dear Mrs "));
        SALUTATION_ALLOCATION_MAP.put("mr", new Tuple<>("M", "Dear Mr "));
        SALUTATION_ALLOCATION_MAP.put("ms", new Tuple<>("F", "Dear Ms "));
        SALUTATION_ALLOCATION_MAP.put("signora", new Tuple<>("F", "Gentile Signora "));
        SALUTATION_ALLOCATION_MAP.put("sig.", new Tuple<>("M", "Egregio Signor "));
        SALUTATION_ALLOCATION_MAP.put("mme", new Tuple<>("W", "Madame "));
        SALUTATION_ALLOCATION_MAP.put("m", new Tuple<>("M", "Monsieur "));
        SALUTATION_ALLOCATION_MAP.put("señora", new Tuple<>("W", "Estimada Señora "));
        SALUTATION_ALLOCATION_MAP.put("señor", new Tuple<>("M", "Estimado Señor "));
        SALUTATION_ALLOCATION_MAP.put("", new Tuple<>("-", "Sehr geehrte Damen und Herren"));
    }

    /**
     * Method determining the salutation for the contact.
     * @param contactPartAllocationList allocated contact parts
     * @param contactDTO contact DTO to be updated
     * @return updated contact DTO
     */
    public ContactDTO setSexAndLetterSalutation(List<ContactPartAllocation> contactPartAllocationList, ContactDTO contactDTO) {
        // get salutations from contact parts
        List<ContactPartAllocation> salutation = contactPartAllocationList.stream()
                .filter(part -> ContactParts.SALUTATION.equals(part.getContactStringPart()))
                .collect(Collectors.toList());

        // One salutation in contact -> get right letter salutation from static salutation map
        if (salutation.size() == 1) {
            Tuple<String, String> sexAndSalutation = SALUTATION_ALLOCATION_MAP.get(salutation.get(0).getContactPart().getSecondObject().toLowerCase());
            contactDTO.setGender(sexAndSalutation.getFirstObject());
            contactDTO.setSalutation(salutation.get(0).getContactPart().getSecondObject());
            contactDTO.setLetterSalutation(sexAndSalutation.getSecondObject() + contactDTO.getTitle() + " " + contactDTO.getLastName());
            contactDTO.setLetterSalutation(contactDTO.getLetterSalutation().replaceAll("[ ]+", " ").trim());
            return contactDTO;
        } else if (salutation.isEmpty()) { // no salutation in contact -> get default from salutation map
            Tuple<String, String> sexAndSalutation = SALUTATION_ALLOCATION_MAP.get("");
            contactDTO.setGender(sexAndSalutation.getFirstObject());
            contactDTO.setLetterSalutation(sexAndSalutation.getSecondObject());
            contactDTO.setLetterSalutation(contactDTO.getLetterSalutation().replaceAll("[ ]+", " ").trim());
            return contactDTO;
        } else { // more than 1 salutation -> error
            throw new ContactParsingException(contactDTO, "Too many salutations");
        }
    }
}
