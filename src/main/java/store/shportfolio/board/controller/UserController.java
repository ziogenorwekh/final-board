package store.shportfolio.board.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import store.shportfolio.board.cache.CustomCacheManager;
import store.shportfolio.board.command.user.*;
import store.shportfolio.board.exception.UserNotFoundException;
import store.shportfolio.board.exception.UserNotMatchingException;
import store.shportfolio.board.security.CustomUserDetails;
import store.shportfolio.board.service.UserService;

@Slf4j
@Controller
public class UserController {

    private final UserService userService;
    private final CustomCacheManager customCacheManager;

    @Autowired
    public UserController(UserService userService, CustomCacheManager customCacheManager) {
        this.userService = userService;
        this.customCacheManager = customCacheManager;
    }
//
//    @GetMapping("/")
//    public String home() {
//        log.debug("home page");
//        return "home";
//    }

//    @GetMapping("/login")
//    public String login(Model model) {
//        model.addAttribute("loginVO", loginVO);
//        log.debug("login page");
//        return "login";
//    }
//
//    @GetMapping("/signup")
//    public String emailCheck() {
//        log.debug("email check");
//        return "signup";
//    }

    @GetMapping("/signup/form")
    public String showSignupForm(Model model, UserCreateCommand userCreateCommand, HttpSession session) {
        String sessionEmail = customCacheManager.getVerifiedEmailWithSession(session.getId());
        if (sessionEmail == null) {
            log.error("이메일 인증을 다시 해야 합니다.");
            return "redirect:/signup";
        }
        model.addAttribute("userCreateCommand", userCreateCommand);
        return "signupForm";
    }

    @PostMapping("/user/created")
    public String createUser(@Valid @ModelAttribute("userCreateCommand") UserCreateCommand userCreateCommand,
                             BindingResult bindingResult, HttpSession session) {
        log.info("userCreateCommand: {}", userCreateCommand);
        if (bindingResult.hasErrors()) {
            log.error("errors is -> {}", bindingResult.getAllErrors());
            // 유효성 검사 오류가 있으면 다시 폼을 렌더링
            return "signupForm";
        }

        String sessionEmail = customCacheManager.getVerifiedEmailWithSession(session.getId());
        if (sessionEmail == null) {
            log.error("이메일 인증을 다시 해야 합니다.");
            return "redirect:/signup";
        }
        userCreateCommand.setEmail(sessionEmail);

        UserCreateResponse user = userService.createUser(userCreateCommand);
        log.info("create user -> {}, email : {}", user.getUsername(), user.getEmail());
        return "redirect:/login";
    }

    @GetMapping("/user/{username}")
    public String retrieveUser(@PathVariable("username") String username, Model model) {
        UserTrackQuery userTrackQuery = new UserTrackQuery(username);
        UserTrackQueryResponse trackQueryResponse = userService.findUserByUsername(userTrackQuery);
        model.addAttribute("user", trackQueryResponse);
        return "user/profile";
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
