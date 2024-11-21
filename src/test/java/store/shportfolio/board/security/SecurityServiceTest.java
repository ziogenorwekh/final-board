package store.shportfolio.board.security;//package store.shportfolio.board.security;
//
//import store.shportfolio.board.domain.User;
//import store.shportfolio.board.vo.LoginResponseVO;
//import store.shportfolio.board.vo.LoginVO;
//import store.shportfolio.board.vo.VerifyCodeVO;
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.core.Authentication;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.time.Duration;
//import java.util.UUID;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.Future;
//
//@ActiveProfiles("test")
//@ExtendWith(MockitoExtension.class)
//public class SecurityServiceTest {
//
//    @InjectMocks
//    private AuthServiceImpl authService;
//
//    @Mock
//    private JavaMailSender javaMailSender;
//
//
//
//    private final UUID userId = UUID.randomUUID();
//
//    @BeforeEach
//    public void setUp() {
//    }
//
//    @AfterEach
//    public void tearDown() {
//    }
//
//    @Test
//    @DisplayName("로그인 메서드 테스트")
//    public void userLogin() {
////        // given
////        String token = "token";
//        LoginVO loginVO = new LoginVO("username", "password");
//        Authentication authentication = Mockito.mock(Authentication.class);
////        CustomUserDetails customUserDetails = Mockito.mock(CustomUserDetails.class);
////
////        Mockito.when(authenticationManager.authenticate(Mockito.any(Authentication.class))).thenReturn(authentication);
////        Mockito.when(authentication.getPrincipal()).thenReturn(customUserDetails);
////        Mockito.when(customUserDetails.getId()).thenReturn(userId);
////        Mockito.when(jwtProvider.createToken(customUserDetails)).thenReturn(token);
////
////        LoginResponseVO expectedResponse = new LoginResponseVO(userId, token,"username");
////        // when
////        LoginResponseVO actualResponse = authService.login(loginVO);
////
////        // then
////        Assertions.assertNotNull(actualResponse);
////        Assertions.assertEquals(expectedResponse, actualResponse);
//    }
//
//    @Test
//    @DisplayName("메일 보내기 테스트")
//    public void mailSendTests() throws MessagingException, ExecutionException, InterruptedException {
////        // given
////        UUID userId = UUID.randomUUID();
////        User user = new User(userId, "password", "username", "ziogenorwekh@gmail.com");
////
////        Mockito.when(redisTemplate.delete(Mockito.anyString())).thenReturn(true);
////
////        ValueOperations<String, String> valueOperations = Mockito.mock(ValueOperations.class);
////        Mockito.when(redisTemplate.opsForValue()).thenReturn(valueOperations);
////        Mockito.doNothing().when(valueOperations).set(Mockito.anyString(), Mockito.anyString(), Mockito.any(Duration.class));
////
////        MimeMessage mimeMessage = Mockito.mock(MimeMessage.class);
////        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
////        Mockito.when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
////        Mockito.doNothing().when(javaMailSender).send(Mockito.any(MimeMessage.class));
////
////        // when
////        Future<String> futureCode = authService.sendMail(user);
////        String code = futureCode.get();
////
////        // then
////        Mockito.verify(redisTemplate).delete(user.getEmail());
////        Mockito.verify(valueOperations).set(user.getEmail(), code, Duration.ofMinutes(5L));
////        Mockito.verify(javaMailSender).send(Mockito.any(MimeMessage.class));
////
////        Assertions.assertNotNull(code);
//    }
//
//    @Test
//    @DisplayName("이메일로부터 온 값을 받아 유저 인증하기")
//    public void verifyCodeUserActivateTest() {
////        // given
////        VerifyCodeVO verifyCodeVO = new VerifyCodeVO("ziogenorwekh@gmail.com", "123456");
////        ValueOperations<String, String> valueOperations = Mockito.mock(ValueOperations.class);
////        Mockito.when(redisTemplate.opsForValue()).thenReturn(valueOperations);
////        Mockito.when(redisTemplate.opsForValue().get("ziogenorwekh@gmail.com")).thenReturn("123456");
////        // when
////        Boolean aBoolean = authService.verifyEmail(verifyCodeVO);
////        // then
////        Assertions.assertTrue(aBoolean);
//    }
//}
