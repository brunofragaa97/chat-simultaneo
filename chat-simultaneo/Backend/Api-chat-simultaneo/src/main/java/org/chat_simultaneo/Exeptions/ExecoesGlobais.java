package org.chat_simultaneo.Exeptions;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.chat_simultaneo.DTO.ErrorResponse;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExecoesGlobais {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        // Se for uma violação de restrição do banco, trata como e-mail duplicado
        if (ex.getCause() instanceof ConstraintViolationException) {
            String message = "E-mail já cadastrado.";
            ErrorResponse errorResponse = new ErrorResponse("ERR_DUPLICATE_EMAIL", message, LocalDateTime.now());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        // Se for outra violação, retorne um erro genérico
        ErrorResponse errorResponse = new ErrorResponse("ERR_DATABASE", "Erro de integridade do banco de dados.", LocalDateTime.now());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}