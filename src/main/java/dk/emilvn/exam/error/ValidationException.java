package dk.emilvn.exam.error;

public class ValidationException extends RuntimeException{
    public ValidationException(String message) {
        super(message);
    }
}
