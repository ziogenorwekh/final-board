package store.shportfolio.board.mapper;

import org.springframework.stereotype.Component;
import store.shportfolio.board.command.post.PostTrackQueryResponse;
import store.shportfolio.board.domain.Post;
import store.shportfolio.board.jpa.PostEntity;
import store.shportfolio.board.jpa.UserEntity;
import store.shportfolio.board.valueobject.UserId;

@Component
public class PostMapper {


    public PostEntity toPostEntity(Post post) {

        return PostEntity.builder()
                .postId(post.getPostId().getValue())
                .user(toUserEntity(post.getUserId()))
                .title(post.getTitle().getValue())
                .contents(post.getContents().getValue())
                .build();
    }

    public PostTrackQueryResponse toPostTrackQueryResponse(PostEntity postEntity) {
        return PostTrackQueryResponse.builder()
                .postId(postEntity.getPostId())
                .contents(postEntity.getContents())
                .title(postEntity.getTitle())
                .poster(postEntity.getUser().getUsername())
                .createdAt(postEntity.getCreatedAt())
                .updatedAt(postEntity.getUpdatedAt())
                .userId(postEntity.getUser().getId())
                .build();
    }

    private UserEntity toUserEntity(UserId userId) {
        return UserEntity.builder().id(userId.getValue()).build();
    }
}
