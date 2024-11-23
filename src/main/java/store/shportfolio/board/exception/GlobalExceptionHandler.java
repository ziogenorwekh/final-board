package store.shportfolio.board.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserEmailDuplicatedException.class)
    public String handleUserEmailDuplicated(Model model, UserEmailDuplicatedException e) {
        model.addAttribute("duplicated", e.getMessage());
        return "signup";
    }



    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolation(RedirectAttributes redirectAttributes, ConstraintViolationException e) {
        List<String> errorMessages = e.getConstraintViolations()
                .stream().map(a -> a.getMessage()).collect(Collectors.toList());

        redirectAttributes.addFlashAttribute("errors", errorMessages);

        return "redirect:/signup";
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(Model model, UserNotFoundException e) {
        model.addAttribute("error", e.getMessage());
        return "/";
    }

    @ExceptionHandler(UserNotMatchingException.class)
    public String handleUserNotMatching(Model model, UserNotMatchingException e) {
        model.addAttribute("error", e.getMessage());
        return "/";
    }
}
