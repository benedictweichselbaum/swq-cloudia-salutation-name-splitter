package dhbw.cloudia.splitter.control.service;

import dhbw.cloudia.splitter.control.contactstringpart.ContactStringPart;
import dhbw.cloudia.splitter.control.helper.ContactPartAllocation;
import dhbw.cloudia.splitter.control.helper.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AllocationOptimizationService {

    public static final String TITLES_PATH = "classpath:contact_info/titles.txt";
    private final StringInFileCheckerService stringInFileCheckerService;

    public List<ContactPartAllocation> optimizeAllocation(List<ContactPartAllocation> contactPartAllocations) {
        List<ContactPartAllocation> notAllocatedAndTitleParts = contactPartAllocations.stream().filter(part ->
                ContactStringPart.NOT_ALLOCATED.equals(part.getContactStringPart()) ||
                ContactStringPart.TITLE.equals(part.getContactStringPart())
        ).sorted(Comparator.comparingInt(a -> a.getContactPart().getFirstObject()))
        .collect(Collectors.toList());

        if (notAllocatedAndTitleParts.size() < 2) {
            contactPartAllocations.sort(Comparator.comparingInt(a -> a.getContactPart().getFirstObject()));
            return contactPartAllocations;
        }

        String possibleTitleSpace = getCompletePossibleTitle(notAllocatedAndTitleParts, " ");
        String possibleTitleDash = getCompletePossibleTitle(notAllocatedAndTitleParts, "-");
        for (int i = notAllocatedAndTitleParts.size() - 1; i > 0; i--) {
            if (this.stringInFileCheckerService.stringIsInFile(possibleTitleSpace, TITLES_PATH) ||
                    this.stringInFileCheckerService.stringIsInFile(possibleTitleDash, TITLES_PATH)) {
                contactPartAllocations.add(new ContactPartAllocation(new Tuple<>(notAllocatedAndTitleParts.get(0).getContactPart().getFirstObject(), possibleTitleSpace), ContactStringPart.TITLE));
                contactPartAllocations.removeAll(notAllocatedAndTitleParts.subList(0, i + 1));
                break;
            }
            possibleTitleSpace = possibleTitleSpace.substring(0, possibleTitleSpace.length() - (notAllocatedAndTitleParts.get(i).getContactPart().getSecondObject().length() + 1));
            possibleTitleDash = possibleTitleDash.substring(0, possibleTitleDash.length() - (notAllocatedAndTitleParts.get(i).getContactPart().getSecondObject().length() + 1));
        }
        contactPartAllocations.sort(Comparator.comparingInt(a -> a.getContactPart().getFirstObject()));
        return contactPartAllocations;
    }

    private String getCompletePossibleTitle(List<ContactPartAllocation> notAllocatedParts, String separationCharacter) {
        StringBuilder titleCompletionBuilder = new StringBuilder();
        notAllocatedParts.forEach(part -> titleCompletionBuilder.append(part.getContactPart().getSecondObject()).append(separationCharacter));
        String finalString = titleCompletionBuilder.toString();
        return finalString.substring(0, finalString.length()-1);
    }
}
