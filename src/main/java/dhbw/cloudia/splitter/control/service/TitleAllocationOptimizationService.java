package dhbw.cloudia.splitter.control.service;

import dhbw.cloudia.splitter.control.contactstringpart.ContactParts;
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
public class TitleAllocationOptimizationService {

    public static final String TITLES_PATH = "/contact_info/titles.txt";
    private final StringInFileCheckerService stringInFileCheckerService;

    /**
     * Method that is possible changing the part allocation by checking if there
     * is a longer possible title for the contact by taking the non allocated contact parts
     * into consideration
     * @param contactPartAllocations current contact part allocations
     * @return updated contact part allocation
     */
    public List<ContactPartAllocation> optimizeAllocation(List<ContactPartAllocation> contactPartAllocations) {
        // filter for not allocated and tile parts and sort them
        List<ContactPartAllocation> notAllocatedAndTitleParts = contactPartAllocations.stream().filter(part ->
                ContactParts.NOT_ALLOCATED.equals(part.getContactStringPart()) ||
                ContactParts.TITLE.equals(part.getContactStringPart())
        ).sorted(Comparator.comparingInt(a -> a.getContactPart().getFirstObject()))
        .collect(Collectors.toList());

        // if there are none or only one not allocated of title part sort allocated parts and return them unchanged
        if (notAllocatedAndTitleParts.size() < 2) {
            contactPartAllocations.sort(Comparator.comparingInt(a -> a.getContactPart().getFirstObject()));
            return contactPartAllocations;
        }

        // create possible concat titles
        String possibleTitleSpace = getCompletePossibleTitle(notAllocatedAndTitleParts, " ");
        String possibleTitleDash = getCompletePossibleTitle(notAllocatedAndTitleParts, "-");

        for (int i = notAllocatedAndTitleParts.size() - 1; i > 0; i--) {
            // check if concat string is a valid title
            if (this.stringInFileCheckerService.stringIsInFile(possibleTitleSpace, TITLES_PATH) ||
                    this.stringInFileCheckerService.stringIsInFile(possibleTitleDash, TITLES_PATH)) {
                contactPartAllocations.add(new ContactPartAllocation(new Tuple<>(notAllocatedAndTitleParts.get(0).getContactPart().getFirstObject(), possibleTitleSpace), ContactParts.TITLE));
                contactPartAllocations.removeAll(notAllocatedAndTitleParts.subList(0, i + 1));
                break;
            }

            // cut title string for new approach
            possibleTitleSpace = possibleTitleSpace.substring(0, possibleTitleSpace.length() - (notAllocatedAndTitleParts.get(i).getContactPart().getSecondObject().length() + 1));
            possibleTitleDash = possibleTitleDash.substring(0, possibleTitleDash.length() - (notAllocatedAndTitleParts.get(i).getContactPart().getSecondObject().length() + 1));
        }
        contactPartAllocations.sort(Comparator.comparingInt(a -> a.getContactPart().getFirstObject()));
        return contactPartAllocations;
    }

    /**
     * Private method creating possible contact titles by concat the single strings of the not allocated and title parts
     * Concat with separation character
     * @param notAllocatedAndTitleParts contact parts to be concat
     * @param separationCharacter concat separation character
     * @return concat string
     */
    private String getCompletePossibleTitle(List<ContactPartAllocation> notAllocatedAndTitleParts, String separationCharacter) {
        StringBuilder titleCompletionBuilder = new StringBuilder();
        notAllocatedAndTitleParts.forEach(part -> titleCompletionBuilder.append(part.getContactPart().getSecondObject()).append(separationCharacter));
        String finalString = titleCompletionBuilder.toString();
        return finalString.substring(0, finalString.length()-1);
    }
}
