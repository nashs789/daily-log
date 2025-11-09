package com.nashs.daily_log.api.post.controller;

import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import com.nashs.daily_log.domain.common.utils.PageUtils;
import com.nashs.daily_log.domain.post.info.PostInfo;
import com.nashs.daily_log.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(("/post"))
public class PostController {

    private final PostService postService;

    @GetMapping
    public ModelAndView postPage(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            ModelAndView mv
    ) {
        PageUtils<PostInfo> pageUtils = new PageUtils<>(page, size);

        pageUtils.setupContent(postService.findAllPost(pageUtils.getPageable()));

        mv.addObject("postList", pageUtils.getContent());
        mv.addObject("page", pageUtils);

        mv.setViewName("post/post");

        return mv;
    }

    @GetMapping("/myPost")
    public ModelAndView myPostPage(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            ModelAndView mv,
            LifeLogUser lifeLogUser
    ) {
        PageUtils<PostInfo> pageUtils = new PageUtils<>(page, size);

        pageUtils.setupContent(postService.findMyAllPost(lifeLogUser, pageUtils.getPageable()));

        mv.addObject("postList", pageUtils.getContent());
        mv.addObject("page", pageUtils);

        mv.setViewName("post/myPost");

        return mv;
    }

    @GetMapping("/{postId}")
    public ModelAndView postDetailPage(
            @PathVariable Long postId,
            ModelAndView mv
    ) {
        mv.addObject("post", postService.findPostById(postId));

        mv.setViewName("post/postDetail");

        return mv;
    }

    @GetMapping("/postWrite")
    public String writePage() {
        return "post/postWrite";
    }

    @GetMapping("/{postId}/edit")
    public ModelAndView editPage(
            @PathVariable Long postId,
            ModelAndView mv
    ) {
        mv.addObject("post", postService.findPostById(postId));

        mv.setViewName("post/postEdit");

        return mv;
    }
}
