package dhbw.cloudia.splitter.control.exception;

import dhbw.cloudia.splitter.boundary.dto.ContactOutputTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Exception class. Gets thrown then incoming contact string cannot be parsed.
 * Can hold partly parsed contact output object.
 */
@AllArgsConstructor
@Getter
public class ContactParsingException extends RuntimeException {
    private final ContactOutputTO partyParsedContactOutput;
}
