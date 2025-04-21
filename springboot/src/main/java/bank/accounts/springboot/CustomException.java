package bank.accounts.springboot;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomException extends Exception{

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {

        Map<String, List<String>> response = new HashMap<>();

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());

        List<String> status =new ArrayList<>();
        status.add(ex.getBody().getDetail());
        status.add("Endpoint: " + ex.getParameter().getExecutable().getName());

        response.put("Status Detail", status);
        response.put("Errors", errors);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(ex.getBody().getStatus()));
    }

}
