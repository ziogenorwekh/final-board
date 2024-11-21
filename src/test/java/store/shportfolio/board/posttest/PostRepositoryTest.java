package store.shportfolio.board.posttest;

import store.shportfolio.board.domain.Post;
import store.shportfolio.board.jpa.PostEntity;
import store.shportfolio.board.jpa.PostRepository;
import store.shportfolio.board.jpa.UserEntity;
import store.shportfolio.board.jpa.UserRepository;
import store.shportfolio.board.mapper.PostMapper;
import store.shportfolio.board.valueobject.Contents;
import store.shportfolio.board.valueobject.Title;
import store.shportfolio.board.valueobject.UserId;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.UUID;

@DataJpaTest(showSql = false)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2,
        replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
public class PostRepositoryTest {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private PostMapper postMapper;

    private UserEntity user;
    private PostEntity postEntity;

    private final UUID userId = UUID.randomUUID();
    private final UUID postId = UUID.randomUUID();

    @BeforeEach
    public void setUp() {
        postMapper = new PostMapper();
        user = UserEntity.builder().id(userId).posts(new ArrayList<>()).build();
        userRepository.save(user);
        postEntity = PostEntity.builder()
                .title("title")
                .postId(postId)
                .contents("contents")
                .user(user)
                .build();
    }

    @AfterEach
    public void tearDown() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글 생성 및 삭제")
    public void createPostsAndDeleteTests() {

        // given
        postRepository.save(postEntity);

        // when

        postEntity = postRepository.findById(postId).get();

        // then
        Assertions.assertNotNull(postEntity.getCreatedAt());
        Assertions.assertNotNull(postEntity.getUpdatedAt());
        Assertions.assertEquals(postEntity.getCreatedAt(), postEntity.getUpdatedAt());

        // when
        postRepository.delete(postEntity);

        // then
        Assertions.assertEquals(0, postRepository.findAll().size());
    }

    @Test
    @DisplayName("게시글 수정")
    public void postUpdateTests() {
        // given
        postRepository.save(postEntity);

        PostEntity findPostEntity = postRepository.findById(postId).get();
        Post post = new Post(findPostEntity);

        // when
        post.updateTitle(new Title("newTitle"), new UserId(userId));
        post.updateContents(new Contents("newContents"), new UserId(userId));

        PostEntity updatedEntity = postMapper.toPostEntity(post);
        postRepository.save(updatedEntity);

        PostEntity newPostEntity = postRepository.findById(postId).get();
        // then
        Assertions.assertEquals("newTitle", newPostEntity.getTitle());
        Assertions.assertEquals("newContents", newPostEntity.getContents());
    }


    @Test
    @DisplayName("게시글 삭제")
    public void postDeleteTests() {
        // given
        postRepository.save(postEntity);
        PostEntity findPostEntity = postRepository.findById(postId).get();


        Post post = new Post(findPostEntity);

        // when

        RuntimeException runtimeException = Assertions.assertThrows(RuntimeException.class, () -> {
            post.delete(new UserId(UUID.randomUUID()));
        });

        post.delete(new UserId(userId));
        postRepository.delete(postEntity);

        // then
        Assertions.assertEquals("You are not allowed to delete a post", runtimeException.getMessage());
        Assertions.assertEquals(0, postRepository.findAll().size());
    }
}
