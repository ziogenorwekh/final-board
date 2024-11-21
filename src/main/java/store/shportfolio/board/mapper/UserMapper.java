package store.shportfolio.board.mapper;

import org.springframework.stereotype.Component;
import store.shportfolio.board.command.user.UserTrackQueryResponse;
import store.shportfolio.board.domain.Post;
import store.shportfolio.board.domain.Role;
import store.shportfolio.board.domain.User;
import store.shportfolio.board.jpa.PostEntity;
import store.shportfolio.board.jpa.RoleEntity;
import store.shportfolio.board.jpa.UserEntity;

@Component
public class UserMapper {

    public UserEntity toUserEntity(User user) {
        return UserEntity.builder()
                .id(user.getId().getValue())
                .username(user.getUsername())
                .password(user.getPassword().getValue())
                .email(user.getEmail())
                .enabled(user.getEnabled())
                .posts(user.getPosts().stream().map(this::toPostEntity).toList())
                .role(user.getRoles().stream().map(this::toRoleEntity).toList())
                .build();
    }

    public UserTrackQueryResponse toUserTrackQueryResponse(User user) {
        return UserTrackQueryResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .userId(user.getId().getValue())
                .createdAt(user.getCreatedAt())
                .build();
    }

    private PostEntity toPostEntity(Post post) {

        return PostEntity.builder()
                .postId(post.getPostId().getValue())
                .title(post.getTitle().getValue())
                .contents(post.getContents().getValue())
                .user(UserEntity.builder().id(post.getUserId().getValue()).build())
                .build();
    }


    private RoleEntity toRoleEntity(Role role) {
        return RoleEntity.builder()
                .user(UserEntity.builder().id(role.getUserId().getValue()).build())
                .roleName(role.getRole())
                .build();
    }
}
