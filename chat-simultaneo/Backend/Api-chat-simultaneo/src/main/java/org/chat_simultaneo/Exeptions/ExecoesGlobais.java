package org.chat_simultaneo.Exeptions;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.chat_simultaneo.DTO.ErrorResponse;

import java.time.LocalDateTime;

// Define esta classe como um manipulador global de exceções no Spring Boot
@ControllerAdvice
public class ExecoesGlobais {

    // Anota um método que lida com exceções do tipo DataIntegrityViolationException
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {

        // Verifica se a causa raiz do erro é uma violação de restrição do banco de dados
        if (ex.getCause() instanceof ConstraintViolationException) {
            // Define uma mensagem específica para erro de e-mail duplicado
            String message = "E-mail já cadastrado.";

            // Cria um objeto ErrorResponse contendo um código de erro, mensagem e timestamp
            ErrorResponse errorResponse = new ErrorResponse("ERR_DUPLICATE_EMAIL", message, LocalDateTime.now());

            // Retorna a resposta com status HTTP 400 (Bad Request) e o corpo da mensagem de erro
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        // Se a exceção não for por e-mail duplicado, cria uma resposta de erro genérico do banco de dados
        ErrorResponse errorResponse = new ErrorResponse("ERR_DATABASE", "Erro de integridade do banco de dados.", LocalDateTime.now());

        // Retorna a resposta com status HTTP 500 (Internal Server Error)
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
