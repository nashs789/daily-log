package com.nashs.daily_log.api.post.controller;

import com.nashs.daily_log.domain.auth.info.LifeLogUser;
import com.nashs.daily_log.domain.post.service.PostService;
import com.nashs.daily_log.domain.template.service.TemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(("/post"))
public class PostController {

    private final TemplateService templateService;
    private final PostService postService;

    @GetMapping
    public ModelAndView postPage(ModelAndView mv) {
        mv.addObject("postList", postService.findAllPost());

        mv.setViewName("post/post");

        return mv;
    }

    @GetMapping("/write")
    public String writePage(ModelAndView mv) {
        return "post/write";
    }
}
