package store.shportfolio.board.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import store.shportfolio.board.vo.SendEmailVO;
import store.shportfolio.board.vo.VerifyCodeVO;

import java.util.Random;

@Slf4j
@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;
    private final CacheManager cacheManager;

    @Autowired
    public MailServiceImpl(JavaMailSender javaMailSender, CacheManager cacheManager) {
        this.javaMailSender = javaMailSender;
        this.cacheManager = cacheManager;
    }

    @Override
    public void sendMail(SendEmailVO sendEmailVO) {
        Random random = new Random();
        String code = "";
        for (int i = 0; i < 6; i++) {
            int number = random.nextInt(888888) + 111111; // 6자리 랜덤 숫자 생성
            code = String.valueOf(number); // 인증번호 설정
        }

        log.info("create email code: {}", code);

        saveCodeToCache(sendEmailVO.getEmail(), code);

        // 인증 메일 발송
        sendActivationEmail(sendEmailVO.getEmail(), code);
    }

    @Override
    public Boolean verifyMail(VerifyCodeVO verifyCodeVOv) {
        log.debug("verifyCodeVOv: {}", verifyCodeVOv);
        // 캐시에서 이메일을 찾고 인증 코드가 일치하는지 확인
        String cachedCode = getCodeFromCache(verifyCodeVOv.getEmail());

        if (cachedCode != null && cachedCode.equals(verifyCodeVOv.getCode())) {
            deleteCodeFromCache(verifyCodeVOv.getEmail());
            return true; // 인증 코드가 일치하면 true 반환
        }
        return false; // 일치하지 않으면 false 반환
    }

    private String saveCodeToCache(String email, String code) {
        Cache cache = cacheManager.getCache("verificationCodes");
        cache.evict(email);
        cache.put(email, code);
        log.info("save email code: {}", code);
        return code;
    }

    private String getCodeFromCache(String email) {
        Cache cache = cacheManager.getCache("verificationCodes");
        String code = cache.get(email, String.class);
        log.info("get email :{},  code: {}", email, code);
        return code;
    }

    private void deleteCodeFromCache(String email) {
        cacheManager.getCache("verificationCodes").evict(email);
    }


    private void sendActivationEmail(String email, String code) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject("Account Activation");
            helper.setText(buildEmailContent(code), true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new MailSendException("Failed to send email", e);
        }
    }

    private String buildEmailContent(String code) {
        return "<div style=\"font-family: Arial, sans-serif; margin: 20px;\">"
                + "<h2 style=\"color: #333;\">Activate Your Account</h2>"
                + "<p>Thank you for registering with our service. Please use the following code to activate your account:</p>"
                + "<h3 style=\"color: #007bff;\">" + code + "</h3>"
                + "<p>If you did not register for this service, please ignore this email.</p>"
                + "<p>Best regards,<br>Your Company</p>"
                + "</div>";
    }
}