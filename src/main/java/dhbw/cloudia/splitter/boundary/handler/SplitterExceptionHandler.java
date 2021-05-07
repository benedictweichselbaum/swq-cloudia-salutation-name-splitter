package dhbw.cloudia.splitter.boundary.handler;

import dhbw.cloudia.splitter.boundary.dto.DataTransferObject;
import dhbw.cloudia.splitter.boundary.dto.ErrorDTO;
import dhbw.cloudia.splitter.control.exception.ContactParsingException;
import dhbw.cloudia.splitter.control.exception.FileReadingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Exception handler class handling exceptions that occur in the application.
 */
@RestControllerAdvice
public class SplitterExceptionHandler {

    /**
     * Method that handles ContactParsingException. If Exception has output object it get returned.
     * Otherwise the answer body is empty.
     * @param contactParsingException handled exception
     * @return HTTP-Status 400 with partial contact output if available
     */
    @ExceptionHandler(ContactParsingException.class)
    public ResponseEntity<DataTransferObject> handleContactParseError(ContactParsingException contactParsingException) {
        return contactParsingException.getPartyParsedContactOutput() == null ?
                ResponseEntity.badRequest().body(ErrorDTO.builder().errorMessage(contactParsingException.getMessage()).build()) :
                ResponseEntity.badRequest().body(contactParsingException.getPartyParsedContactOutput());
    }

    /**
     * Method that handles FileReadingException.
     * @param fileReadingException handled exception
     * @return HTTP-Status 500 with error message
     */
    @ExceptionHandler(FileReadingException.class)
    public ResponseEntity<ErrorDTO> handleFileReadError(FileReadingException fileReadingException) {
        return ResponseEntity.status(500).body(ErrorDTO.builder().errorMessage(fileReadingException.getMessage()).build());
    }
}
