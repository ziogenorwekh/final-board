package store.shportfolio.board.usertest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import store.shportfolio.board.cache.CustomCacheManager;
import store.shportfolio.board.command.user.UserCreateCommand;
import store.shportfolio.board.command.user.UserCreateResponse;
import store.shportfolio.board.controller.UserController;
import store.shportfolio.board.jpa.UserRepository;
import store.shportfolio.board.mail.MailService;
import store.shportfolio.board.service.UserService;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = {UserController.class})
public class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private CustomCacheManager customCacheManager;

    @MockBean
    private MailService mailService;


    private final UUID userId = UUID.randomUUID();

    private final String username = "testuser";
    private final String password = "testpassword";
    private final String email = "test@test.com";
    private final LocalDateTime created = LocalDateTime.now();

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


}