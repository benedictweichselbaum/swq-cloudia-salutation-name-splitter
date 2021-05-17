package dhbw.cloudia.splitter.control.exception;

/**
 * Exception for when a invalid title is supposed to be learned
 */
public class TitleLearningException extends RuntimeException {
    public TitleLearningException(String message) {
        super(message);
    }
}
