package store.shportfolio.board.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import store.shportfolio.board.command.user.UserUpdateCommand;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserEmailDuplicatedException.class)
    public String handleUserEmailDuplicated(Model model, UserEmailDuplicatedException e) {
        model.addAttribute("duplicated", e.getMessage());
        return "signup";
    }



    @ExceptionHandler(ConstraintViolationException.class)
    public String handleConstraintViolation(RedirectAttributes redirectAttributes,
                                            HttpServletRequest request,
                                            ConstraintViolationException e) {
        List<String> errorMessages = e.getConstraintViolations()
                .stream().map(a -> a.getMessage()).collect(Collectors.toList());

        redirectAttributes.addFlashAttribute("errors", errorMessages);


        String referer = request.getHeader("Referer"); // 이전 페이지 URL
        if (referer != null) {
            return "redirect:" + referer; // Referer 헤더가 있는 경우
        } else {
            return "redirect:/"; // Referer 헤더가 없는 경우 기본 URL
        }
    }

    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(RedirectAttributes redirectAttributes, UserNotFoundException e) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler(PostNotOwnerException.class)
    public String handlePostNotOwner(RedirectAttributes redirectAttributes, PostNotOwnerException e) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return "redirect:/";
    }

    @ExceptionHandler(PasswordNotMatchingException.class)
    public String handlePasswordNotMatching(Model model, PasswordNotMatchingException e) {

        model.addAttribute("notMatched", e.getMessage());
        model.addAttribute("userUpdateCommand", new UserUpdateCommand());

        return "user/update";
    }

    @ExceptionHandler(UserNotMatchingException.class)
    public String handleUserNotMatching(Model model, UserNotMatchingException e) {
        model.addAttribute("error", e.getMessage());
        return "/";
    }
}
