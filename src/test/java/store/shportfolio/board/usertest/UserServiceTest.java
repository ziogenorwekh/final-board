package store.shportfolio.board.usertest;

import store.shportfolio.board.command.user.*;
import store.shportfolio.board.domain.User;
import store.shportfolio.board.jpa.RoleEntity;
import store.shportfolio.board.jpa.RoleEnum;
import store.shportfolio.board.jpa.UserEntity;
import store.shportfolio.board.jpa.UserRepository;
import store.shportfolio.board.mapper.UserMapper;
import store.shportfolio.board.service.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    private UUID userId;
    private UserEntity userEntity;

    @BeforeEach
    public void setUp() {
        userId = UUID.randomUUID();
        userEntity = UserEntity.builder()
                .enabled(true)
                .username("username")
                .password("password")
                .id(userId)
                .role(new ArrayList<>())
                .build();

        RoleEntity roleEntity = RoleEntity.builder().roleName(RoleEnum.USER).user(userEntity).build();
        userEntity.getRole().add(roleEntity);
    }


    @Test
    @DisplayName("유저 생성")
    public void createUserTest() {
        UserCreateCommand userCreateCommand = Mockito.mock(UserCreateCommand.class);
        Mockito.when(userCreateCommand.getEmail()).thenReturn("test@example.com");
        Mockito.when(userCreateCommand.getPassword()).thenReturn("password");
        Mockito.when(userCreateCommand.getUsername()).thenReturn("testuser");

        UserEntity mockUserEntity = UserEntity.builder()
                .id(userId)
                .email("test@example.com")
                .username("testuser")
                .password("password")
                .enabled(true)
                .build();

        Mockito.when(userMapper.toUserEntity(Mockito.any(User.class))).thenReturn(mockUserEntity);
        Mockito.when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());


        Mockito.when(userRepository.save(Mockito.any(UserEntity.class))).thenReturn(mockUserEntity);


        // when
        UserCreateResponse response = userService.createUser(userCreateCommand);

        // then
        Assertions.assertNotNull(response);
        Assertions.assertEquals("testuser", response.getUsername());
        Assertions.assertEquals("test@example.com", response.getEmail());
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(UserEntity.class));
        Mockito.verify(userMapper, Mockito.times(1)).toUserEntity(Mockito.any(User.class));
    }

    @Test
    @DisplayName("유저 조회")
    public void userUpdateTest() {
        // given
        UserTrackQuery userTrackQuery = new UserTrackQuery(userId);
        UserEntity mockUserEntity = Mockito.mock(UserEntity.class);

        Mockito.when(userRepository.findById(Mockito.eq(userId))).thenReturn(Optional.of(mockUserEntity));

        Mockito.when(userMapper.toUserTrackQueryResponse(Mockito.any(User.class)))
                .thenReturn(UserTrackQueryResponse.builder()
                        .userId(userId)
                        .username("testuser")
                        .email("testuser@example.com")
                        .createdAt(LocalDateTime.now())
                        .build());

        // when
        UserTrackQueryResponse response = userService.findUserById(userTrackQuery);

        // then
        Assertions.assertNotNull(response);
        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.eq(userId));
        Mockito.verify(userMapper, Mockito.times(1)).toUserTrackQueryResponse(Mockito.any(User.class));
    }

    @Test
    @DisplayName("유저 비밀번호 업데이트")
    public void userUpdateTests() {
        // given
        UserEntity userEntity = UserEntity.builder()
                .enabled(true)
                .username("username")
                .password("password")
                .id(userId)
                .role(new ArrayList<>())
                .posts(new ArrayList<>())
                .build();

        RoleEntity roleEntity = RoleEntity.builder().roleName(RoleEnum.USER).user(userEntity).build();
        userEntity.getRole().add(roleEntity);

        Mockito.when(userRepository.findById(Mockito.eq(userId))).thenReturn(Optional.of(userEntity));
        Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString())).thenReturn(true);

        UserUpdateCommand userUpdateCommand = new UserUpdateCommand( "password", "newpassword");
        userUpdateCommand.setUserId(userId);
        UserEntity updatedEntity = UserEntity.builder()
                .enabled(true)
                .username("username")
                .password("newpassword")
                .id(userId)
                .role(new ArrayList<>())
                .posts(new ArrayList<>())
                .build();
        Mockito.when(userMapper.toUserEntity(Mockito.any(User.class))).thenReturn(updatedEntity);

        // when
        userService.updateUser(userUpdateCommand);

        // then
        Mockito.verify(userMapper, Mockito.times(1)).toUserEntity(Mockito.any(User.class));

        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.eq(userId));
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(UserEntity.class));

        // 캡처된 UserEntity를 검증
        ArgumentCaptor<UserEntity> userEntityCaptor = ArgumentCaptor.forClass(UserEntity.class);
        Mockito.verify(userRepository).save(userEntityCaptor.capture());
        UserEntity capturedUserEntity = userEntityCaptor.getValue();

        // 새로운 비밀번호로 업데이트되었는지 확인
        Assertions.assertEquals("newpassword", capturedUserEntity.getPassword());
    }

    @Test
    @DisplayName("유저 삭제")
    public void userDeleteTests() {

        // given
        UserDeleteCommand userDeleteCommand = new UserDeleteCommand(userId);

        UserEntity mockUserEntity = Mockito.mock(UserEntity.class);
        Mockito.when(userRepository.findById(Mockito.eq(userId))).thenReturn(Optional.of(mockUserEntity));
        Mockito.doNothing().when(userRepository).delete(mockUserEntity);

        // when
        userService.deleteUser(userDeleteCommand);

        // then
        Mockito.verify(userRepository, Mockito.times(1)).findById(Mockito.eq(userId));
        Mockito.verify(userRepository, Mockito.times(1)).delete(Mockito.eq(mockUserEntity));
    }
}
