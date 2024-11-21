package store.shportfolio.board.service;

import store.shportfolio.board.command.post.*;

import java.util.List;

public interface PostService {

    PostCreateResponse createPost(PostCreateCommand postCreateCommand);

    PostTrackQueryResponse findOnePost(PostTrackQuery postTrackQuery);

    List<PostTrackQueryResponse> findAllPosts();

    void updatePost(PostUpdateCommand postUpdateCommand);

    void deletePost(PostDeleteCommand postDeleteCommand);
}
