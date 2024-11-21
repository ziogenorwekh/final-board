package store.shportfolio.board.domain;

import lombok.Builder;
import lombok.Getter;
import store.shportfolio.board.command.post.PostCreateCommand;
import store.shportfolio.board.exception.NotOwnerException;
import store.shportfolio.board.jpa.PostEntity;
import store.shportfolio.board.valueobject.Contents;
import store.shportfolio.board.valueobject.PostId;
import store.shportfolio.board.valueobject.Title;
import store.shportfolio.board.valueobject.UserId;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Post {

    private final PostId postId;

    private Title title;

    private Contents contents;
    private final UserId userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Post(PostEntity postEntity) {
        this.postId = new PostId(postEntity.getPostId());
        this.title = new Title(postEntity.getTitle());
        this.contents = new Contents(postEntity.getContents());
        this.userId = new UserId(postEntity.getUser().getId());
        this.createdAt = postEntity.getCreatedAt();
        this.updatedAt = postEntity.getUpdatedAt();
    }

    @Builder
    public Post(PostId postId, Title title, Contents contents, UserId userId) {
        this.postId = postId;
        this.title = title;
        this.contents = contents;
        this.userId = userId;
    }

    public static Post initialize(PostCreateCommand postCreateCommand) {
        UUID postId = UUID.randomUUID();
        return Post.builder()
                .postId(new PostId(postId))
                .title(new Title(postCreateCommand.getTitle()))
                .contents(new Contents(postCreateCommand.getContent()))
                .userId(new UserId(postCreateCommand.getUserId()))
                .build();
    }

    public void updateTitle(Title title, UserId userId) {
        this.validatePostOwner(userId);
        if (title.getValue().isEmpty()) {
            return;
        }
        this.title = title;

    }

    public void updateContents(Contents contents, UserId userId) {
        this.validatePostOwner(userId);
        if (contents.getValue().isEmpty()) {
            return;
        }
        this.contents = contents;
    }


    public void delete(UserId userId) {
        if (!this.userId.equals(userId)) {
            throw new NotOwnerException("You are not allowed to delete a post");
        }
    }


    private void validatePostOwner(UserId userId) {
        if (!this.userId.equals(userId)) {
            throw new NotOwnerException("You are not allowed to update a post");
        }
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", title=" + title +
                ", contents=" + contents +
                ", userId=" + userId +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
