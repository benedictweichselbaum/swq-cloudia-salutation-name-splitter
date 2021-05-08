package dhbw.cloudia.splitter.control.exception;

/**
 * Exception class then file reading in application fails
 */
public class FileReadingException extends RuntimeException {
    public FileReadingException(String message) {
        super(message);
    }
}
