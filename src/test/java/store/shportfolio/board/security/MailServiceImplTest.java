package store.shportfolio.board.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ActiveProfiles;
import store.shportfolio.board.mail.MailServiceImpl;
import store.shportfolio.board.vo.SendEmailVO;
import store.shportfolio.board.vo.VerifyCodeVO;


@ActiveProfiles("test")
@SpringBootTest
public class MailServiceImplTest {

    @Autowired
    private MailServiceImpl mailService; // 테스트할 실제 서비스 객체

    @Autowired
    private CacheManager cacheManager;

    private final String email = "ziogenorwekh@gmail.com";

    @BeforeEach
    void setUp() {
    }


//    @Test
    void testSendMail() {
        // Given
        SendEmailVO sendEmailVO = new SendEmailVO(email);

        // When
        mailService.sendMail(sendEmailVO);

        // Then
        String verificationCodes = cacheManager.getCache("verificationCodes").get(email, String.class);
        Assertions.assertNotNull(verificationCodes);
    }

//    @Test
    void testVerifyCode() {

        // given
        String code = "123456";
        Cache cache = cacheManager.getCache("verificationCodes");
        cache.put(email, code);
        VerifyCodeVO verifyCodeVO = new VerifyCodeVO(email, code);
        String sessionId = "sessionId";
        // when
        Boolean verified = mailService.verifyMail(verifyCodeVO,sessionId);

        // then
        Assertions.assertTrue(verified);
        // 이메일 인증이 완료되면 캐쉬값이 없어야 한다.
        Assertions.assertEquals(false, mailService.verifyMail(verifyCodeVO,sessionId));
    }

}