package com.artlighter.glucosecontrolservice.authgateway.controller;

import com.artlighter.glucosecontrolservice.authgateway.util.mapper.ExceptionOutputUtils;
import com.artlighter.glucosecontrolservice.authgateway.util.exception.*;
import com.artlighter.glucosecontrolservice.diary.util.exception.NoRepositoryForEntryTypeException;
import com.artlighter.glucosecontrolservice.general.exception.ResourceAlreadyExistsException;
import com.artlighter.glucosecontrolservice.general.exception.ResourceNotFoundException;
import com.artlighter.glucosecontrolservice.user.util.exception.NoSuchEnumerableConstantException;
import com.artlighter.glucosecontrolservice.user.util.exception.UserIsNotPatientException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice({"com.artlighter.glucosecontrolservice.diary.controller",
        "com.artlighter.glucosecontrolservice.user.controller",
        "com.artlighter.glucosecontrolservice.authgateway.controller",
        "com.artlighter.glucosecontrolservice.calculations.controller",
        "com.artlighter.glucosecontrolservice.templates.controller"})
public class AppControllerAdvice {
    //private Logger log = LoggerFactory.getLogger(DiaryControllerAdvice.class);

    @ExceptionHandler(ValidationIsFailedException.class)
    public ResponseEntity<ExceptionDTO> validationIsFailedException(ValidationIsFailedException ex) {
        return ResponseEntity.badRequest().body(ExceptionOutputUtils.createValidationException(ex));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity httpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionOutputUtils.createOutputException(HttpStatus.BAD_REQUEST, ex, false));
    }

    @ExceptionHandler(UserIsNotPatientException.class)
    public ResponseEntity userIsNotPatientException(UserIsNotPatientException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionOutputUtils.createOutputException(HttpStatus.BAD_REQUEST, ex, false));
    }

//    @ExceptionHandler(NotCurrentUsersInfoException.class)
//    public ResponseEntity<ExceptionDTO> notCurrentUsersInfoException(NotCurrentUsersInfoException ex) {
//        return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                .body(DTOConvertUtils.createOutputException(HttpStatus.FORBIDDEN, ex, false));
//    }

    @ExceptionHandler(NoRepositoryForEntryTypeException.class)
    public ResponseEntity<ExceptionDTO> noRepositoryForEntryTypeException(NoRepositoryForEntryTypeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionOutputUtils.createOutputException(HttpStatus.INTERNAL_SERVER_ERROR, ex, true));
    }

//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<ExceptionDTO> illegalArgumentException(IllegalArgumentException ex) {
//        //ex.printStackTrace();
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(DTOConvertUtils.createOutputException(HttpStatus.INTERNAL_SERVER_ERROR, ex, true));
//    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ExceptionDTO> resourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ExceptionOutputUtils.createOutputException(HttpStatus.CONFLICT, ex, false));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionDTO> resourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionOutputUtils.createOutputException(HttpStatus.NOT_FOUND, ex, false));
    }

//    @ExceptionHandler(AuthoritiesException.class)
//    public ResponseEntity<ExceptionDTO> authoritiesException(AuthoritiesException ex) {
//        return ResponseEntity.badRequest()
//                .body(ExceptionOutputUtils.createOutputException(HttpStatus.BAD_REQUEST, ex, false));
//    }

    @ExceptionHandler(NoSuchEnumerableConstantException.class)
    public ResponseEntity<ExceptionDTO> noSuchEnum(NoSuchEnumerableConstantException ex) {
        return ResponseEntity.badRequest()
                .body(ExceptionOutputUtils.createOutputException(HttpStatus.BAD_REQUEST, ex, false));
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<ExceptionDTO> unsupportedOperationException(UnsupportedOperationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionOutputUtils.createOutputException(HttpStatus.INTERNAL_SERVER_ERROR, ex, true));
    }

}
