package store.shportfolio.board.posttest;

import store.shportfolio.board.command.post.*;
import store.shportfolio.board.domain.Post;
import store.shportfolio.board.jpa.PostEntity;
import store.shportfolio.board.jpa.PostRepository;
import store.shportfolio.board.jpa.UserEntity;
import store.shportfolio.board.mapper.PostMapper;
import store.shportfolio.board.service.PostServiceImpl;
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

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @InjectMocks
    private PostServiceImpl postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostMapper postMapper;


    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
    }

    @Test
    @DisplayName("게시글 생성")
    public void createPostTests() {

        // given
        PostCreateCommand postCreateCommand = new PostCreateCommand("title", "contents", userId);

        PostEntity postEntity = Mockito.mock(PostEntity.class);

        Mockito.when(postMapper.toPostEntity(Mockito.any(Post.class))).thenReturn(postEntity);
        Mockito.when(postRepository.save(Mockito.any(PostEntity.class))).thenReturn(postEntity);
        Mockito.when(postEntity.getCreatedAt()).thenReturn(LocalDateTime.now());
        // when

        PostCreateResponse postCreateResponse = postService.createPost(postCreateCommand);

        // then
        Assertions.assertNotNull(postCreateResponse);
        Assertions.assertEquals(postCreateCommand.getTitle(), "title");
        Assertions.assertNotNull(postCreateResponse.getCreatedAt());
    }


    @Test
    @DisplayName("게시글 수정")
    public void postUpdateTests() {
        // given
        UUID postId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        PostUpdateCommand postUpdateCommand = new PostUpdateCommand("updateTitle", "updateContents", userId);
        postUpdateCommand.setPostId(postId);
        PostEntity postEntity = Mockito.mock(PostEntity.class);
        UserEntity userEntity = Mockito.mock(UserEntity.class);
        PostEntity updatedPostEntity = Mockito.mock(PostEntity.class);
        Mockito.when(updatedPostEntity.getTitle()).thenReturn("updateTitle");
        Mockito.when(updatedPostEntity.getContents()).thenReturn("updateContents");

        Mockito.when(postEntity.getUser()).thenReturn(userEntity);
        Mockito.when(userEntity.getId()).thenReturn(userId);

        Mockito.when(postRepository.findById(postId)).thenReturn(Optional.of(postEntity));
        Mockito.when(postMapper.toPostEntity(Mockito.any(Post.class))).thenReturn(updatedPostEntity);

        // when
        postService.updatePost(postUpdateCommand);

        // then
        Mockito.verify(postRepository, Mockito.times(1)).findById(postId);

        ArgumentCaptor<PostEntity> postEntityArgumentCaptor = ArgumentCaptor.forClass(PostEntity.class);
        Mockito.verify(postRepository).save(postEntityArgumentCaptor.capture());
        PostEntity capturedPostEntity = postEntityArgumentCaptor.getValue();

        Assertions.assertNotNull(capturedPostEntity);
        Assertions.assertEquals(postUpdateCommand.getTitle(), capturedPostEntity.getTitle());
        Assertions.assertEquals(postUpdateCommand.getContent(), capturedPostEntity.getContents());
    }

    @Test
    @DisplayName("게시글 조회")
    public void findPostByIdTest() {
        // given
        UUID postId = UUID.randomUUID();
        PostTrackQuery postTrackQuery = new PostTrackQuery(postId);
        PostTrackQueryResponse queryResponse = PostTrackQueryResponse.builder().postId(postId).build();
        PostEntity postEntity = Mockito.mock(PostEntity.class);
        Mockito.when(postRepository.findById(postId)).thenReturn(Optional.of(postEntity));
        Mockito.when(postMapper.toPostTrackQueryResponse(postEntity)).thenReturn(queryResponse);

        // when
        PostTrackQueryResponse response = postService.findOnePost(postTrackQuery);

        // then
        Mockito.verify(postRepository, Mockito.times(1)).findById(postId);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(postTrackQuery.getPostId(), response.getPostId());
    }


    @Test
    @DisplayName("게시글 삭제")
    public void postDeleteTests() {
        // given
        UUID postId = UUID.randomUUID();
        PostDeleteCommand postDeleteCommand = new PostDeleteCommand(userId,postId);

        PostEntity postEntity = Mockito.mock(PostEntity.class);
        Mockito.when(postRepository.findById(postId)).thenReturn(Optional.of(postEntity));
        UserEntity userEntity = Mockito.mock(UserEntity.class);
        Mockito.when(userEntity.getId()).thenReturn(userId);
        Mockito.when(postEntity.getUser()).thenReturn(userEntity);

        // when
        postService.deletePost(postDeleteCommand);

        // then
        Mockito.verify(postRepository, Mockito.times(1)).findById(postId);
        Mockito.verify(postRepository, Mockito.times(1)).delete(postEntity);
    }
}
