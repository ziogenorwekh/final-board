package store.shportfolio.board.service;

import jakarta.validation.Valid;
import store.shportfolio.board.command.user.*;

public interface UserService {

    UserCreateResponse createUser(@Valid UserCreateCommand userCreateCommand);

    UserTrackQueryResponse findUserById(@Valid UserTrackQuery userTrackQuery);

    void updateUser(@Valid UserUpdateCommand userUpdateCommand);

    void deleteUser(@Valid UserDeleteCommand userDeleteCommand);

}
