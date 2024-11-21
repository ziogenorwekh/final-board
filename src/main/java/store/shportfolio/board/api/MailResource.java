package store.shportfolio.board.api;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import store.shportfolio.board.mail.MailService;
import store.shportfolio.board.vo.SendEmailVO;
import store.shportfolio.board.vo.VerifyCodeVO;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping(path = "/api")
public class MailResource {

    private final MailService mailService;

    @Autowired
    public MailResource(MailService mailService) {
        this.mailService = mailService;
    }

    @GetMapping("/hi")
    public String hi() {
        return "hi";
    }

    @RequestMapping(value = "/mail/send", produces ="application/json", method = RequestMethod.POST)
    public ResponseEntity<Map> sendEmailCode(@Valid @RequestBody SendEmailVO sendEmailVO) {
        log.debug("send email, sendEmailVO: {}", sendEmailVO);
        try {
            mailService.sendMail(sendEmailVO);
            Map<String, String> response = new HashMap<>();
            response.put("message", "메일이 성공적으로 전송되었습니다.");
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            log.error("메일 전송 실패: {}", e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "메일 전송에 실패했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @RequestMapping(value = "/mail/verify", produces ="application/json", method = RequestMethod.POST)
    public ResponseEntity<String> verifyEmailAuthenticationCode(@Valid @RequestBody VerifyCodeVO verifyCodeVO) {
        log.info("verify email: {}", verifyCodeVO);
        Boolean isVerify = mailService.verifyMail(verifyCodeVO);
        log.info("isVerify: {}", isVerify);
        if (!isVerify) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("email authentication code is incorrect.");
        }
        return ResponseEntity.ok().build();
    }
}
