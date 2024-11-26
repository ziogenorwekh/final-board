package store.shportfolio.board.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.shportfolio.board.command.post.*;
import store.shportfolio.board.domain.Post;
import store.shportfolio.board.exception.PostNotFoundException;
import store.shportfolio.board.exception.PostNotOwnerException;
import store.shportfolio.board.jpa.PostEntity;
import store.shportfolio.board.jpa.PostRepository;
import store.shportfolio.board.mapper.PostMapper;
import store.shportfolio.board.valueobject.Contents;
import store.shportfolio.board.valueobject.Title;
import store.shportfolio.board.valueobject.UserId;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public PostServiceImpl(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    @Override
    @Transactional
    public PostCreateResponse createPost(PostCreateCommand postCreateCommand) {

        Post post = Post.initialize(postCreateCommand);
        PostEntity postEntity = postMapper.toPostEntity(post);
        PostEntity savedPost = postRepository.save(postEntity);
        log.info("Created post: {}", post);
        return new PostCreateResponse(savedPost.getPostId(), savedPost.getCreatedAt());
    }

    @Override
    @Transactional(readOnly = true)
    public PostTrackQueryResponse findOnePost(PostTrackQuery postTrackQuery) {
        PostEntity postEntity = postRepository.findById(postTrackQuery.getPostId())
                .orElseThrow(() -> new PostNotFoundException(String.
                        format("Post with id %s not found", postTrackQuery.getPostId())));
        log.info("Found postId : {}", postEntity.getPostId());
        return postMapper.toPostTrackQueryResponse(postEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostTrackQueryResponse> findAllPosts() {
        List<PostTrackQueryResponse> postTrackQueryResponses = new ArrayList<>();
        postRepository.findAll().stream().forEach(postEntity -> postTrackQueryResponses
                .add(postMapper.toPostTrackQueryResponse(postEntity)));
        return postTrackQueryResponses;
    }

    @Override
    @Transactional
    public void updatePost(PostUpdateCommand postUpdateCommand) {
        PostEntity postEntity = postRepository.findById(postUpdateCommand.getPostId()).orElseThrow(() ->
                new PostNotFoundException(String.
                format("Post with id %s not found", postUpdateCommand.getPostId())));
        if (!postUpdateCommand.getUserId().equals(postEntity.getUser().getId())) {
            throw new PostNotOwnerException("you're not post owner.");
        }

        log.info("Updated postId : {}", postEntity.getPostId());
        Post post = new Post(postEntity);
        post.updateTitle(new Title(postUpdateCommand.getTitle()), new UserId(postUpdateCommand.getUserId()));
        post.updateContents(new Contents(postUpdateCommand.getContent()), new UserId(postUpdateCommand.getUserId()));

        PostEntity updatedPostEntity = postMapper.toPostEntity(post);

        postRepository.save(updatedPostEntity);
    }

    @Override
    @Transactional
    public void deletePost(PostDeleteCommand postDeleteCommand) {

        PostEntity postEntity = postRepository
                .findById(postDeleteCommand.getPostId()).orElseThrow(() -> new PostNotFoundException(String.
                        format("Post with id %s not found", postDeleteCommand.getPostId())));
        log.info("Deleted postId : {}", postEntity.getPostId());
        Post post = new Post(postEntity);
        post.delete(new UserId(postDeleteCommand.getUserId()));
        postRepository.delete(postEntity);
    }
}
