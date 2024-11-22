package store.shportfolio.board.mail;

import store.shportfolio.board.cache.CustomCacheManager;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import store.shportfolio.board.vo.SendEmailVO;
import store.shportfolio.board.vo.VerifyCodeVO;

import java.util.Random;

@Slf4j
@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;
    private final CustomCacheManager customCacheManager;

    @Autowired
    public MailServiceImpl(JavaMailSender javaMailSender, CustomCacheManager customCacheManager) {
        this.javaMailSender = javaMailSender;
        this.customCacheManager = customCacheManager;
    }

    @Override
    public void sendMail(SendEmailVO sendEmailVO) {
        String code = generateRandomCode();

        log.info("Generated email code: {}", code);

        // 인증 코드 저장
        customCacheManager.save(sendEmailVO.getEmail(), code);

        // 인증 메일 발송
        sendActivationEmail(sendEmailVO.getEmail(), code);
    }

    @Override
    public Boolean verifyMail(VerifyCodeVO verifyCodeVO, String sessionId) {
        log.debug("Verify request: {}", verifyCodeVO);

        // 캐시에서 이메일을 찾고 인증 코드가 일치하는지 확인
        String cachedCode = customCacheManager.getCodeWithEmail(verifyCodeVO.getEmail());

        if (cachedCode != null && cachedCode.equals(verifyCodeVO.getCode())) {
            customCacheManager.deleteCode(verifyCodeVO.getEmail());
            customCacheManager.save(sessionId, verifyCodeVO.getEmail());
            return true; // 인증 코드가 일치하면 true 반환
        }
        return false; // 일치하지 않으면 false 반환
    }

    private String generateRandomCode() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(900000) + 100000); // 6자리 랜덤 숫자
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