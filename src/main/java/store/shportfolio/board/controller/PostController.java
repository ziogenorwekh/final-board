package store.shportfolio.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import store.shportfolio.board.command.post.*;
import store.shportfolio.board.exception.UserNotMatchingException;
import store.shportfolio.board.security.CustomUserDetails;
import store.shportfolio.board.service.PostService;

import java.util.UUID;

@Controller
public class PostController {


    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/create/post")
    public String createPost(@ModelAttribute PostCreateCommand postCreateCommand,
                             @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        if (!customUserDetails.getId().equals(postCreateCommand.getUserId())) {
            throw new UserNotMatchingException("You are not authorized to create this user's post.");
        }
        PostCreateResponse postCreateResponse = postService.createPost(postCreateCommand);
        model.addAttribute("createPost", postCreateResponse);
        return "/posts";
    }

    @GetMapping("/posts")
    public String retrievePosts(Model model) {
        model.addAttribute("posts", postService.findAllPosts());
        return "/posts";
    }

    @GetMapping("/post/{id}")
    public String retrievePost(@PathVariable UUID id, Model model) {
        PostTrackQuery postTrackQuery = new PostTrackQuery(id);
        PostTrackQueryResponse trackQueryResponse = postService.findOnePost(postTrackQuery);
        model.addAttribute("post", trackQueryResponse);
        return "/post";
    }

    @PutMapping("/update/post")
    public String updatePost(@ModelAttribute PostUpdateCommand postUpdateCommand,
                             @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {
        if (!customUserDetails.getId().equals(postUpdateCommand.getUserId())) {
            throw new UserNotMatchingException("You are not authorized to update this user's post.");
        }
        postService.updatePost(postUpdateCommand);
        model.addAttribute("updatePost", postUpdateCommand);
        return "/post";
    }

    @DeleteMapping("/delete/post/{id}")
    public String deletePost(@PathVariable UUID postId,@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        PostDeleteCommand postDeleteCommand = new PostDeleteCommand(customUserDetails.getId(), postId);
        postService.deletePost(postDeleteCommand);
        return "redirect:/posts";
    }
}
