package store.shportfolio.board.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import store.shportfolio.board.command.user.*;
import store.shportfolio.board.exception.UserNotFoundException;
import store.shportfolio.board.exception.UserNotMatchingException;
import store.shportfolio.board.security.CustomUserDetails;
import store.shportfolio.board.service.UserService;
import store.shportfolio.board.vo.LoginVO;

import java.util.UUID;

@Slf4j
@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() {
        log.debug("home page");
        return "home";
    }

    @GetMapping("/login")
    public String login(LoginVO loginVO, Model model) {
        model.addAttribute("loginVO", loginVO);
        log.debug("login page");
        return "login";
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model, UserCreateCommand userCreateCommand) {
        model.addAttribute("userCreateCommand", userCreateCommand);
        return "signup";
    }

    @PostMapping("/user/created")
    public String createUser(@Valid @ModelAttribute("userCreateCommand") UserCreateCommand userCreateCommand,
                             BindingResult bindingResult, Model model) {

        log.info("userCreateCommand: {}", userCreateCommand);
        if (bindingResult.hasErrors()) {
            log.error("errors is -> {}", bindingResult.getAllErrors());
            model.addAttribute("verifiedMail", userCreateCommand.getEmail());
            // 유효성 검사 오류가 있으면 다시 폼을 렌더링
            return "signup";
        }

        UserCreateResponse user = userService.createUser(userCreateCommand);
        log.info("create user -> {}, email : {}", user.getUsername(), user.getEmail());
        return "redirect:/login";
    }

    @GetMapping("/user/{id}")
    public String retrieveUser(@PathVariable("id") UUID userId, Model model) {
        UserTrackQuery userTrackQuery = new UserTrackQuery(userId);
        UserTrackQueryResponse trackQueryResponse = userService.findUserById(userTrackQuery);
        model.addAttribute("user", trackQueryResponse);
        return "profile";
    }

    @PutMapping("/user/update")
    public String updateUser(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                             @ModelAttribute UserUpdateCommand userUpdateCommand) {
        if (!customUserDetails.getId().equals(userUpdateCommand.getUserId())) {
            throw new UserNotMatchingException("You are not authorized to update this user's information.");
        }
        userService.updateUser(userUpdateCommand);
        return "redirect:/profile";
    }

    @DeleteMapping("/user/delete")
    public String deleteUser(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (customUserDetails == null) {
            throw new UserNotFoundException("you are not log-in");
        }
        UserDeleteCommand userDeleteCommand = new UserDeleteCommand(customUserDetails.getId());
        userService.deleteUser(userDeleteCommand);
        return "redirect:/";
    }


}
