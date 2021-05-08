package dhbw.cloudia.splitter.control.service;

import dhbw.cloudia.splitter.boundary.dto.ContactDTO;
import dhbw.cloudia.splitter.control.contactstringpart.ContactStringPart;
import dhbw.cloudia.splitter.control.exception.ContactParsingException;
import dhbw.cloudia.splitter.control.helper.ContactPartAllocation;
import dhbw.cloudia.splitter.control.helper.Tuple;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SalutationAllocationService {

    private static final Map<String, Tuple<String, String>> SALUTATION_ALLOCATION_MAP = new HashMap<>();

    static {
        SALUTATION_ALLOCATION_MAP.put("Herr", new Tuple<>("M", "Sehr geehrter Herr "));
        SALUTATION_ALLOCATION_MAP.put("Frau", new Tuple<>("F", "Sehr geehrte Frau "));
        SALUTATION_ALLOCATION_MAP.put("Herrn", new Tuple<>("H", "Sehr geehrter Herr "));
        SALUTATION_ALLOCATION_MAP.put("Mrs", new Tuple<>("F", "Dear Mrs "));
        SALUTATION_ALLOCATION_MAP.put("Mr", new Tuple<>("M", "Dear Mr "));
        SALUTATION_ALLOCATION_MAP.put("Ms", new Tuple<>("F", "Dear Ms "));
        SALUTATION_ALLOCATION_MAP.put("Signora", new Tuple<>("F", "Gentile Signora "));
        SALUTATION_ALLOCATION_MAP.put("Sig.", new Tuple<>("M", "Egregio Signor "));
        SALUTATION_ALLOCATION_MAP.put("Mme", new Tuple<>("W", "Madame "));
        SALUTATION_ALLOCATION_MAP.put("M", new Tuple<>("M", "Monsieur "));
        SALUTATION_ALLOCATION_MAP.put("Señora", new Tuple<>("W", "Estimada Señora "));
        SALUTATION_ALLOCATION_MAP.put("Señor", new Tuple<>("M", "Estimado Señor "));
        SALUTATION_ALLOCATION_MAP.put("", new Tuple<>("-", "Sehr geehrte Damen und Herren"));
    }

    public void setSexAndLetterSalutation(List<ContactPartAllocation> contactPartAllocationList, ContactDTO contactDTO) {
        List<ContactPartAllocation> salutation = contactPartAllocationList.stream()
                .filter(part -> ContactStringPart.SALUTATION.equals(part.getContactStringPart()))
                .collect(Collectors.toList());
        if (salutation.size() == 1) {
            Tuple<String, String> sexAndSalutation = SALUTATION_ALLOCATION_MAP.get(salutation.get(0).getContactPart());
            contactDTO.setGender(sexAndSalutation.getFirstObject());
            contactDTO.setSalutation(salutation.get(0).getContactPart());
            contactDTO.setLetterSalutation(sexAndSalutation.getSecondObject() + contactDTO.getTitle() + " " + contactDTO.getFirstName() + " " + contactDTO.getLastName());
        } else if (salutation.isEmpty()) {
            Tuple<String, String> sexAndSalutation = SALUTATION_ALLOCATION_MAP.get("");
            contactDTO.setGender(sexAndSalutation.getFirstObject());
            contactDTO.setLetterSalutation(sexAndSalutation.getSecondObject());
        } else {
            throw new ContactParsingException(contactDTO, "Too many salutations");
        }
    }
}
