package ar.com.l_airline.exceptionHandler.custom_exceptions;

/**
 * This exception should be thrown when required data was not received.
 */
public class MissingDataException extends RuntimeException {
    public MissingDataException(String message){
        super(message);
    }
}
