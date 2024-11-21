package store.shportfolio.board.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import store.shportfolio.board.vo.ExceptionResponseVO;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalApiExceptionHandler extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        List<ExceptionResponseVO> errors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach(objectError -> errors
                .add(new ExceptionResponseVO(objectError.getDefaultMessage())));
        errors.forEach(exceptionResponseVO -> log.error("error -> {}", exceptionResponseVO.getMessage()));
        return ResponseEntity.badRequest().body(errors);
    }
}
