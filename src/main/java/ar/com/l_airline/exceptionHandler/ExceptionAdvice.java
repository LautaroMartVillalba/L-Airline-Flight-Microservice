package ar.com.l_airline.exceptionHandler;

import ar.com.l_airline.exceptionHandler.custom_exceptions.ExistingObjectException;
import ar.com.l_airline.exceptionHandler.custom_exceptions.MissingDataException;
import ar.com.l_airline.exceptionHandler.custom_exceptions.NotFoundException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    /**
     *  Return a 409 code if an existing record with matching data is found in the database.
     * @return 409, "This object already exists in the DataBase".
     */
    @ExceptionHandler(value = ExistingObjectException.class)
    public ResponseEntity<ExceptionDTO> ExistingObjectExcHandler() {
        ExceptionDTO dto = ExceptionDTO.builder().message("This object already exists in the DataBase.")
                .code(HttpStatusCode.valueOf(409)).build();


        return new ResponseEntity<>(dto, dto.getCode());
    }

    /**
     *  Return a 400 code if required data was not received.
     * @return 400, "Insert all required information".
     */
    @ExceptionHandler(value = MissingDataException.class)
    public ResponseEntity<ExceptionDTO> missingDataExcHandler(MissingDataException exception) {
        ExceptionDTO dto = ExceptionDTO.builder().message(exception.getMessage())
                .code(HttpStatusCode.valueOf(400)).build();
        return new ResponseEntity<>(dto,dto.getCode());
    }

    /**
     *  Return a 404 code if it doesn't found a record with data matching in the database.
     * @return 404, "Not found in the DataBase".
     */
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ExceptionDTO> notFoundExcHandler() {
        ExceptionDTO dto = ExceptionDTO.builder().message("Not found in the DataBase.")
                .code(HttpStatusCode.valueOf(404)).build();
        return new ResponseEntity<>(dto,dto.getCode());
    }

    /**
     *  Return a 400 code if an unknowned error has occurred during server communication.
     * @return 400, "An error has occurred. Tru again or contact the service developers".
     */
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ExceptionDTO> internalServiceExcHandler() {
        ExceptionDTO dto = ExceptionDTO.builder().message("An error has occurred. Tru again or contact the service developers.")
                .code(HttpStatusCode.valueOf(400)).build();
        return new ResponseEntity<>(dto,dto.getCode());
    }
}
