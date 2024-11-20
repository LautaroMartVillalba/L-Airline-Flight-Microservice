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

    @ExceptionHandler(value = ExistingObjectException.class)
    public ResponseEntity<ExceptionDTO> ExistingObjectExcHandler() {
        ExceptionDTO dto = ExceptionDTO.builder().message("This object already exists in the DataBase.")
                .code(HttpStatusCode.valueOf(409)).build();
        return new ResponseEntity<>(dto, dto.getCode());
    }

    @ExceptionHandler(value = MissingDataException.class)
    public ResponseEntity<ExceptionDTO> missingDataExcHandler() {
        ExceptionDTO dto = ExceptionDTO.builder().message("Insert all required information.")
                .code(HttpStatusCode.valueOf(404)).build();
        return new ResponseEntity<>(dto,dto.getCode());
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<ExceptionDTO> notFoundExcHandler() {
        ExceptionDTO dto = ExceptionDTO.builder().message("Not found in the DataBase.")
                .code(HttpStatusCode.valueOf(404)).build();
        return new ResponseEntity<>(dto,dto.getCode());
    }
}
