package ar.com.l_airline.exceptionHandler.custom_exceptions;

/**
 * This exception should be thrown when the received data format is unexpected.
 */
public class InvalidFormatException extends RuntimeException {
    public InvalidFormatException(String message) {
        super(message);
    }
}
