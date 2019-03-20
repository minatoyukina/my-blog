package com.ccq.myblog.controller;

import com.ccq.myblog.domain.Blog;
import com.ccq.myblog.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EditController {
    @Autowired
    private BlogRepository blogRepository;

    @GetMapping("/edit")
    public String editor() {
        return "editor";
    }

    @RequestMapping("/submit")
    @ResponseBody
    public void submit(Blog blog) {
        System.out.println(blog.getContent());
        System.out.println(blog.getHtmlContent());
        blogRepository.save(blog);
    }
}
