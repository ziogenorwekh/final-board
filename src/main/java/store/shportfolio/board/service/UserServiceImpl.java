package store.shportfolio.board.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import store.shportfolio.board.command.user.*;
import store.shportfolio.board.domain.Role;
import store.shportfolio.board.domain.User;
import store.shportfolio.board.exception.UserEmailDuplicatedException;
import store.shportfolio.board.exception.UserNotFoundException;
import store.shportfolio.board.jpa.RoleEnum;
import store.shportfolio.board.jpa.UserEntity;
import store.shportfolio.board.jpa.UserRepository;
import store.shportfolio.board.mapper.UserMapper;
import store.shportfolio.board.valueobject.Password;
import store.shportfolio.board.valueobject.UserId;

import java.util.UUID;

@Slf4j
@Service
@Validated
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserCreateResponse createUser(UserCreateCommand userCreateCommand) {
        validateDuplicatedEmail(userCreateCommand.getEmail());
        String encodePassword = passwordEncoder.encode(userCreateCommand.getPassword());
        UUID userId = UUID.randomUUID();
        User user = new User(userId, encodePassword
                , userCreateCommand.getUsername(), userCreateCommand.getEmail());
        log.info("Created user with data is : {}", user);
        Role role = new Role(new UserId(userId), RoleEnum.USER);
        user.addRole(role);
        UserEntity userEntity = userMapper.toUserEntity(user);
        UserEntity saved = userRepository.save(userEntity);
        return new UserCreateResponse(saved.getId(), saved.getUsername(), saved.getEmail(), saved.getCreatedAt());
    }

    @Override
    @Transactional(readOnly = true)
    public UserTrackQueryResponse findUserByUsername(UserTrackQuery userTrackQuery) {

        UserEntity userEntity = userRepository.findByUsername(userTrackQuery.getUsername()).orElseThrow(() ->
                new UserNotFoundException(String.format("User with username %s not found", userTrackQuery.getUsername())));
        User user = new User(userEntity);
        log.info("Found user with data is : {}", user);
        return userMapper.toUserTrackQueryResponse(user);
    }

    @Override
    @Transactional
    public void updateUser(UserUpdateCommand userUpdateCommand) {
        UserEntity userEntity = userRepository.findById(userUpdateCommand.getUserId()).orElseThrow(() ->
                new UserNotFoundException(String.format("User with id %s not found",
                        userUpdateCommand.getUserId())));
        User user = new User(userEntity);
        log.info("Updated user with data is : {}", user);
        if (!passwordEncoder.matches(userUpdateCommand.getCurrentPassword(), user.getPassword().getValue())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        Password newPassword = new Password(passwordEncoder.encode(userUpdateCommand.getNewPassword()));
        user.updatePassword(newPassword);
        UserEntity updatedEntity = userMapper.toUserEntity(user);
        userRepository.save(updatedEntity);
    }

    @Override
    @Transactional
    public void deleteUser(UserDeleteCommand userDeleteCommand) {
        UserEntity userEntity = userRepository.findById(userDeleteCommand.getUserId())
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id %s not found",
                        userDeleteCommand.getUserId())));
        log.info("Deleted user email is : {}", userEntity.getEmail());
        userRepository.delete(userEntity);
    }

    private void validateDuplicatedEmail(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            throw new UserEmailDuplicatedException("User with email " + email + " already exists");
        });
    }

    private void validateDuplicatedUsername(String username) {
        userRepository.findByEmail(username).ifPresent(user -> {
            throw new UserEmailDuplicatedException("User with username " + username + " already exists");
        });
    }
}
