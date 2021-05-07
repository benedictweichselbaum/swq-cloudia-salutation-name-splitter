package dhbw.cloudia.splitter.boundary;

import dhbw.cloudia.splitter.boundary.dto.ErrorDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Special Spring exception handler that takes thrown exception and creates response.
 */
@RestControllerAdvice
public class SalutationNameParsingApiExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleException(Exception e) {
        return ResponseEntity.status(500).body(ErrorDTO.builder().errorMessage(e.getMessage()).build());
    }
}
