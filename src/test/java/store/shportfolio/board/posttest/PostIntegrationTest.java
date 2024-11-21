package store.shportfolio.board.posttest;//package store.shportfolio.board.posttest;
//
//import store.shportfolio.board.api.PostResource;
//import store.shportfolio.board.command.post.*;
//import store.shportfolio.board.security.CustomUserDetails;
//import store.shportfolio.board.service.PostService;
//import jakarta.validation.ConstraintViolation;
//import jakarta.validation.Validation;
//import jakarta.validation.Validator;
//import jakarta.validation.ValidatorFactory;
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
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//import java.time.LocalDateTime;
//import java.util.Set;
//import java.util.UUID;
//
//@ExtendWith(MockitoExtension.class)
//public class PostIntegrationTest {
//
//
//    @Mock
//    private PostService postService;
//    @InjectMocks
//    private PostResource postResource;
//
//    private static ValidatorFactory factory;
//    private static Validator validator;
//
//    @BeforeEach
//    void setUp() {
//        factory = Validation.buildDefaultValidatorFactory();
//        validator = factory.getValidator();
//    }
//
//    @Test
//    @DisplayName("게시글 생성 API")
//    public void createPostTest() {
//
//        // given
//        UUID userId = UUID.randomUUID();
//        UUID postId = UUID.randomUUID();
//        PostCreateCommand postCreateCommand = new PostCreateCommand("title", "contents", userId);
//        PostCreateResponse postCreateResponse = new PostCreateResponse(postId, LocalDateTime.now());
//        Mockito.when(postService.createPost(postCreateCommand)).thenReturn(postCreateResponse);
//        Authentication authentication = Mockito.mock(Authentication.class);
//        CustomUserDetails customUserDetails = Mockito.mock(CustomUserDetails.class);
//        Mockito.when(authentication.getPrincipal()).thenReturn(customUserDetails);
//        Mockito.when(customUserDetails.getId()).thenReturn(userId);
//
//        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
//        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
//        SecurityContextHolder.setContext(securityContext);
//
//        // when
//        ResponseEntity<PostCreateResponse> responseEntity = postResource.createPost(postCreateCommand);
//        // then
//        PostCreateResponse createResponse = responseEntity.getBody();
//        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
//        Assertions.assertNotNull(createResponse);
//        Assertions.assertEquals(postId, createResponse.getPostId());
//    }
//
//    @Test
//    @DisplayName("게시글 수정 API")
//    public void postUpdateTest() {
//        // given
//        UUID userId = UUID.randomUUID();
//        UUID postId = UUID.randomUUID();
//        PostUpdateCommand postUpdateCommand = new PostUpdateCommand("newTitle", "newContents", userId);
//        postUpdateCommand.setPostId(postId);
//        Mockito.doNothing().when(postService).updatePost(postUpdateCommand);
//        Authentication authentication = Mockito.mock(Authentication.class);
//        CustomUserDetails customUserDetails = Mockito.mock(CustomUserDetails.class);
//        Mockito.when(authentication.getPrincipal()).thenReturn(customUserDetails);
//        Mockito.when(customUserDetails.getId()).thenReturn(userId);
//
//        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
//        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
//        SecurityContextHolder.setContext(securityContext);
//
//        // when
//        ResponseEntity<Void> response = postResource.updatePost(postId, postUpdateCommand);
//        // then
//        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
//        Mockito.verify(postService).updatePost(postUpdateCommand);
//    }
//
//    @Test
//    @DisplayName("게시글 Validation 테스트")
//    public void postValidationTest() {
//        // given
//        PostCreateCommand postCreateCommand = new PostCreateCommand("title", "", UUID.randomUUID());
//        PostUpdateCommand postUpdateCommand = new PostUpdateCommand("", "newContents", UUID.randomUUID());
//        postUpdateCommand.setPostId(UUID.randomUUID());
//        // when
//        Set<ConstraintViolation<PostCreateCommand>> constraintViolations = factory.getValidator().validate(postCreateCommand);
//        Set<ConstraintViolation<PostUpdateCommand>> validated = factory.getValidator().validate(postUpdateCommand);
//        // then
//        constraintViolations.forEach(constraintViolation -> {
//           Assertions.assertEquals("Content must not be blank", constraintViolation.getMessage());
//        });
//        validated.forEach(constraintViolation -> {
//            Assertions.assertEquals("Title must not be blank", constraintViolation.getMessage());
//        });
//    }
//
//    @Test
//    @DisplayName("게시글 조회 API")
//    public void postFindOneTest() {
//        // given
//        UUID postId = UUID.randomUUID();
//        UUID userId = UUID.randomUUID();
//        PostTrackQueryResponse trackQueryResponse = new PostTrackQueryResponse(postId,userId,"username","title"
//        ,"contents",LocalDateTime.now(),LocalDateTime.now());
//
//        Mockito.when(postService.findOnePost(Mockito.any(PostTrackQuery.class))).thenReturn(trackQueryResponse);
//        // when
//        ResponseEntity<PostTrackQueryResponse> responseEntity = postResource.retrievePost(postId);
//        PostTrackQueryResponse queryResponse = responseEntity.getBody();
//        // then
//        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        Assertions.assertNotNull(queryResponse);
//        Assertions.assertEquals(trackQueryResponse.getPostId(), queryResponse.getPostId());
//        Assertions.assertEquals(trackQueryResponse.getUserId(), queryResponse.getUserId());
//        Assertions.assertEquals(trackQueryResponse.getTitle(), queryResponse.getTitle());
//        Assertions.assertEquals(trackQueryResponse.getContents(), queryResponse.getContents());
//    }
//
//    @Test
//    @DisplayName("게시글 삭제 API")
//    public void postDeleteTest() {
//        // given
//        UUID postId = UUID.randomUUID();
//        UUID userId = UUID.randomUUID();
//        PostDeleteCommand postDeleteCommand = new PostDeleteCommand(userId);
//        postDeleteCommand.setPostId(postId);
//        Mockito.doNothing().when(postService).deletePost(Mockito.any(PostDeleteCommand.class));
//        Authentication authentication = Mockito.mock(Authentication.class);
//        CustomUserDetails customUserDetails = Mockito.mock(CustomUserDetails.class);
//        Mockito.when(authentication.getPrincipal()).thenReturn(customUserDetails);
//        Mockito.when(customUserDetails.getId()).thenReturn(userId);
//
//        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
//        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
//        SecurityContextHolder.setContext(securityContext);
//        // when
//        ResponseEntity<Void> response = postResource.deletePost(postId, new PostDeleteCommand(userId));
//        // then
//        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
//        Mockito.verify(postService).deletePost(Mockito.any(PostDeleteCommand.class));
//    }
//}
