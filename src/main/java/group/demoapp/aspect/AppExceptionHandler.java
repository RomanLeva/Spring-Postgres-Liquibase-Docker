package group.demoapp.aspect;

import group.demoapp.aspect.exception.UserException;
import group.demoapp.aspect.exception.WalletException;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.HibernateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
}
