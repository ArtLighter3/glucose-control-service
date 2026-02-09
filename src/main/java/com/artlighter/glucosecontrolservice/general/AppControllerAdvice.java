package com.artlighter.glucosecontrolservice.general;

import com.artlighter.glucosecontrolservice.auth.util.mapper.DTOConvertUtils;
import com.artlighter.glucosecontrolservice.auth.util.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice({"com.artlighter.glucosecontrolservice.diary.controller",
        "com.artlighter.glucosecontrolservice.user.controller", "com.artlighter.glucosecontrolservice.auth.controller",
        "com.artlighter.glucosecontrolservice.calculations.controller",
        "com.artlighter.glucosecontrolservice.templates.controller"})
public class AppControllerAdvice {
    //private Logger log = LoggerFactory.getLogger(DiaryControllerAdvice.class);

    @ExceptionHandler(ValidationIsFailedException.class)
    public ResponseEntity<ExceptionDTO> validationIsFailedException(ValidationIsFailedException ex) {
        return ResponseEntity.badRequest().body(DTOConvertUtils.createValidationException(ex));
    }

//    @ExceptionHandler(NotCurrentUsersInfoException.class)
//    public ResponseEntity<ExceptionDTO> notCurrentUsersInfoException(NotCurrentUsersInfoException ex) {
//        return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                .body(DTOConvertUtils.createOutputException(HttpStatus.FORBIDDEN, ex, false));
//    }

    @ExceptionHandler(NoRepositoryForEntryTypeException.class)
    public ResponseEntity<ExceptionDTO> noRepositoryForEntryTypeException(NoRepositoryForEntryTypeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(DTOConvertUtils.createOutputException(HttpStatus.INTERNAL_SERVER_ERROR, ex, true));
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
                .body(DTOConvertUtils.createOutputException(HttpStatus.CONFLICT, ex, false));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionDTO> resourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(DTOConvertUtils.createOutputException(HttpStatus.NOT_FOUND, ex, false));
    }


    @ExceptionHandler(AuthoritiesException.class)
    public ResponseEntity<ExceptionDTO> authoritiesException(AuthoritiesException ex) {
        return ResponseEntity.badRequest()
                .body(DTOConvertUtils.createOutputException(HttpStatus.BAD_REQUEST, ex, false));
    }

    @ExceptionHandler(NoSuchEnumerableConstantException.class)
    public ResponseEntity<ExceptionDTO> noSuchEnum(NoSuchEnumerableConstantException ex) {
        return ResponseEntity.badRequest()
                .body(DTOConvertUtils.createOutputException(HttpStatus.BAD_REQUEST, ex, false));
    }
}
