package group.demoapp.aspect;

import group.demoapp.aspect.exception.UserException;
import group.demoapp.aspect.exception.WalletException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.HibernateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Aspect
@ControllerAdvice
public class AppExceptionHandler {
    @ExceptionHandler(WalletException.class)
    public ResponseEntity<String> handleWalletException(WalletException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body("WALLET ERROR: " + e.getMessage());
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<String> handleUserException(UserException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body("USER ERROR: " + e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleRequestException(HttpMessageNotReadableException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("REQUEST JSON ERROR: " + e.getMessage());
    }

    @ExceptionHandler(HibernateException.class)
    public ResponseEntity<String> handleHibernateException(HibernateException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body("HIBERNATE ERROR: " + e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("VALIDATION ERROR: " + e.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<String> handleGetRequestParameterException(MissingServletRequestParameterException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("REQUEST PARAMETER ERROR: " + e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleJwtParameterException(MalformedJwtException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("JWT ERROR: " + "Unable to read JSON value");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException e){
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("ACCESS DENIED: " + e.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleRuntimeException(ExpiredJwtException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT EXPIRED ERROR");
    }
}
