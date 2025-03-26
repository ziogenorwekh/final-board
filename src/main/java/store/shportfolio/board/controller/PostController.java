package store.shportfolio.board.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import store.shportfolio.board.command.post.*;
import store.shportfolio.board.exception.UserNotMatchingException;
import store.shportfolio.board.security.CustomUserDetails;
import store.shportfolio.board.service.PostService;

import java.util.UUID;

@Slf4j
@Controller
public class PostController {


    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public String retrievePosts(Model model) {
        model.addAttribute("posts", postService.findAllPosts());
        return "home";
    }

    @GetMapping("/post/create")
    public String showCreatePostForm(@AuthenticationPrincipal CustomUserDetails customUserDetails, Model model,
                                     PostCreateCommand postCreateCommand) {
        if (customUserDetails == null) {
            return "redirect:/";
        }

        model.addAttribute("postCreateCommand", postCreateCommand);
        return "post/createPost";
    }


    @PostMapping("/post/create")
    public String createPost(@Valid @ModelAttribute("postCreateCommand") PostCreateCommand postCreateCommand,
                             @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("errors is -> {}", bindingResult.getAllErrors());
            return "post/createPost";
        }

        postCreateCommand.setUserId(customUserDetails.getId());
        PostCreateResponse postCreateResponse = postService.createPost(postCreateCommand);
        model.addAttribute("createPost", postCreateResponse);
        return "redirect:/";
    }

    @GetMapping("/post/{id}")
    public String retrievePost(@PathVariable UUID id, Model model) {
        PostTrackQuery postTrackQuery = new PostTrackQuery(id);
        PostTrackQueryResponse trackQueryResponse = postService.findOnePost(postTrackQuery);
        model.addAttribute("post", trackQueryResponse);
        return "post/postDetail";
    }

    @GetMapping("post/update/{id}")
    public String updatePostForm(@PathVariable UUID id, Model model,
                                 @AuthenticationPrincipal CustomUserDetails customUserDetails
            , PostUpdateCommand postUpdateCommand) {
        if (customUserDetails == null) {
            return "redirect:/";
        }
        PostTrackQuery postTrackQuery = new PostTrackQuery(id);
        PostTrackQueryResponse trackQueryResponse = postService.findOnePost(postTrackQuery);
        model.addAttribute("postInformation", trackQueryResponse);
        model.addAttribute("postUpdateCommand", postUpdateCommand);
        return "post/edit";
    }

    @PostMapping("post/update")
    public String updatePost(@Valid @ModelAttribute("postUpdateCommand") PostUpdateCommand postUpdateCommand,
                             @AuthenticationPrincipal CustomUserDetails customUserDetails, Model model) {



        postUpdateCommand.setUserId(customUserDetails.getId());
        postService.updatePost(postUpdateCommand);
        model.addAttribute("updatePost", postUpdateCommand);


        PostTrackQuery postTrackQuery = new PostTrackQuery(postUpdateCommand.getPostId());
        PostTrackQueryResponse trackQueryResponse = postService.findOnePost(postTrackQuery);
        model.addAttribute("post", trackQueryResponse);
        return "/post/postDetail";
    }

    @PostMapping("/post/delete/{id}")
    public String deletePost(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                             @PathVariable UUID id) {
        log.info("postId -> {}", id);
        PostDeleteCommand postDeleteCommand = new PostDeleteCommand(customUserDetails.getId(), id);
        postService.deletePost(postDeleteCommand);
        return "redirect:/";
    }
}
