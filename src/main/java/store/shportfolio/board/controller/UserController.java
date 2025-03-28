package store.shportfolio.board.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import store.shportfolio.board.cache.CustomCacheManager;
import store.shportfolio.board.command.user.*;
import store.shportfolio.board.exception.UserNotFoundException;
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

    @GetMapping("/user/update")
    public String updateUser(Model model, UserUpdateCommand userUpdateCommand,
                             @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (customUserDetails == null) {
            return "redirect:/login";
        }
        model.addAttribute("userUpdateCommand", userUpdateCommand);
        return "user/update";
    }


    @GetMapping("/signup/form")
    public String showSignupForm(Model model, UserCreateCommand userCreateCommand, HttpSession session) {
        String sessionEmail = customCacheManager.getVerifiedEmailWithSession(session.getId());
        if (sessionEmail == null) {
            log.error("이메일 인증을 해야 합니다.");
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

    @GetMapping("/user/profile/{username}")
    public String retrieveUser(@PathVariable("username") String username, Model model) {
        UserTrackQuery userTrackQuery = new UserTrackQuery(username);
        UserTrackQueryResponse trackQueryResponse = userService.findUserByUsername(userTrackQuery);
        model.addAttribute("user", trackQueryResponse);
        return "user/profile";
    }

    @PostMapping("/user/update")
    public String updateUser(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                             @Valid @ModelAttribute("userUpdateCommand") UserUpdateCommand userUpdateCommand,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("errors is -> {}", bindingResult.getAllErrors());
            return "user/update";
        }

        userUpdateCommand.setUserId(customUserDetails.getId());
        userService.updateUser(userUpdateCommand);
        redirectAttributes.addFlashAttribute("updatedUsername", customUserDetails.getUsername());
        redirectAttributes.addFlashAttribute("successPwdUpdateMessage", "비밀번호가 변경되었습니다.");
        return "redirect:/user/profile/" + customUserDetails.getUsername();
    }

    @PostMapping("/user/delete")
    public String deleteUser(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                             HttpServletRequest request,RedirectAttributes redirectAttributes) {
        if (customUserDetails == null) {
            throw new UserNotFoundException("you are not log-in");
        }
        UserDeleteCommand userDeleteCommand = new UserDeleteCommand(customUserDetails.getId());
        userService.deleteUser(userDeleteCommand);
        // 인증 정보 제거
        SecurityContextHolder.clearContext();
        // 세션 무효화
        request.getSession().invalidate();

        redirectAttributes.addFlashAttribute("successfulDeleteUser", customUserDetails.getUsername());
        return "redirect:/user/profile/" + customUserDetails.getUsername();
    }


}
