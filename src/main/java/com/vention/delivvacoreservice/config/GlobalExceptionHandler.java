package com.vention.delivvacoreservice.config;

import com.vention.delivvacoreservice.exception.JsonParsingException;
import com.vention.general.lib.dto.response.GlobalResponseDTO;
import com.vention.general.lib.exceptions.BadRequestException;
import com.vention.general.lib.exceptions.DataAlreadyExistException;
import com.vention.general.lib.exceptions.DataNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = JsonParsingException.class)
    public ResponseEntity<GlobalResponseDTO> apiExceptionHandler(JsonParsingException e) {
        log.warn(e.getMessage());
        return getResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @ExceptionHandler(value = BadRequestException.class)
    public ResponseEntity<GlobalResponseDTO> apiExceptionHandler(BadRequestException e) {
        log.warn(e.getMessage());
        return getResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(value = DataNotFoundException.class)
    public ResponseEntity<GlobalResponseDTO> apiExceptionHandler(DataNotFoundException e) {
        log.warn(e.getMessage());
        return getResponse(e.getMessage(), HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(value = {DataAlreadyExistException.class})
    public ResponseEntity<GlobalResponseDTO> apiExceptionHandler(RuntimeException e) {
        log.warn(e.getMessage());
        return getResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    private ResponseEntity<GlobalResponseDTO> getResponse(String message, int status) {
        return ResponseEntity.status(status).body(
                GlobalResponseDTO.builder()
                        .status(status)
                        .message(message)
                        .time(ZonedDateTime.now())
                        .build()
        );
    }
}
