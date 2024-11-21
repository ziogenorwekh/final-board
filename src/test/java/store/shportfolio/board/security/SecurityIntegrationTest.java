package store.shportfolio.board.security;//package store.shportfolio.board.security;
//
//import store.shportfolio.board.api.AuthenticationResource;
//import store.shportfolio.board.vo.LoginResponseVO;
//import store.shportfolio.board.vo.LoginVO;
//import store.shportfolio.board.vo.SendEmailVO;
//import store.shportfolio.board.vo.VerifyCodeVO;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import java.util.UUID;
//
//@ExtendWith(MockitoExtension.class)
//public class SecurityIntegrationTest {
//
//
//    @InjectMocks
//    private AuthenticationResource authenticationResource;
//
//    @Mock
//    private CustomUserDetailsService userDetailsService;
//
//    @Mock
//    private AuthService authService;
//
//    @BeforeEach
//    void setUp() {
//    }
//
//    @Test
//    @DisplayName("로그인 테스트")
//    public void loginTest() {
//
//        // given
//        UUID userId = UUID.randomUUID();
//        String token = "token";
//        LoginVO loginVO = new LoginVO("username", "password");
//        LoginResponseVO loginResponseVO = new LoginResponseVO(userId, token,"username");
//        Mockito.when(authService.login(loginVO)).thenReturn(loginResponseVO);
//
//        // when
//        ResponseEntity<LoginResponseVO> login = authenticationResource.login(loginVO);
//
//        // then
//        LoginResponseVO responseVO = login.getBody();
//        Assertions.assertEquals(HttpStatus.OK, login.getStatusCode());
//        Assertions.assertEquals(token, responseVO.getToken());
//        Assertions.assertEquals(userId, responseVO.getUserId());
//        Mockito.verify(authService).login(loginVO);
//    }
//
//    @Test
//    @DisplayName("메일 전송 API 테스트")
//    public void mailSendTest() {
//        // given
//        SendEmailVO sendEmailVO = new SendEmailVO("test@gmail.com");
//        // when
//        ResponseEntity<Void> response = authenticationResource.mailSend(sendEmailVO);
//        // then
//        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
//    }
//
//    @Test
//    @DisplayName("메일 코드 확인 및 유저 권한 업데이트 API 테스트")
//    public void mailVerifyTest() {
//        // given
//        VerifyCodeVO verifyCodeVO = new VerifyCodeVO("test@test.com", "123456");
//        Mockito.when(authService.verifyEmail(Mockito.eq(verifyCodeVO))).thenReturn(Boolean.TRUE);
//        // when
//        ResponseEntity<Void> response = authenticationResource.verifyMail(verifyCodeVO);
//
//        // then
//        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
//        Mockito.verify(userDetailsService, Mockito.times(1)).activateUser(verifyCodeVO.getEmail());
//    }
//}
