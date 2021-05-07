package dhbw.cloudia.splitter.boundary.handler;

import dhbw.cloudia.splitter.boundary.dto.ContactOutputTO;
import dhbw.cloudia.splitter.control.exception.ContactParsingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Exception handler class handling exceptions that occur in the application.
 */
@RestControllerAdvice
public class SplitterExceptionHandler {

    /**
     * Method hat handles ContactParsingException. If Exception has output object it get returned.
     * Otherwise the answer body is empty.
     * @param contactParsingException handled exception
     * @return HTTP-Status 400 with partial contact output if available
     */
    @ExceptionHandler(ContactParsingException.class)
    public ResponseEntity<ContactOutputTO> handleContactParseError(ContactParsingException contactParsingException) {
        return contactParsingException.getPartyParsedContactOutput() == null ?
                ResponseEntity.badRequest().build() :
                ResponseEntity.badRequest().body(contactParsingException.getPartyParsedContactOutput());
    }
}
