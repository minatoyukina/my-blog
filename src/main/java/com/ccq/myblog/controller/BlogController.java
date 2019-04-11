package com.ccq.myblog.controller;

import com.ccq.myblog.domain.EsBlog;
import com.ccq.myblog.domain.User;
import com.ccq.myblog.service.EsBlogService;
import com.ccq.myblog.vo.TagVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    private EsBlogService esBlogService;

    @GetMapping
    public String listEsBlogs(
            @RequestParam(value = "order", required = false, defaultValue = "new") String order,
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(value = "async", required = false) boolean async,
            @RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            Model model) {

        Page<EsBlog> page = null;
        List<EsBlog> list = null;
        boolean isEmpty = true;
        try {
            if (order.equals("hot")) {
                Pageable pageable = PageRequest.of(pageIndex, pageSize);
                page = esBlogService.listHottestEsBlogs(keyword, pageable);

            } else if (order.equals("new")) {
                Pageable pageable = PageRequest.of(pageIndex, pageSize);
                page = esBlogService.listNewestEsBlogs(keyword, pageable);
            }

            isEmpty = false;
        } catch (Exception e) {
            Pageable pageable = PageRequest.of(pageIndex, pageSize);
            page = esBlogService.listEsBlogs(pageable);
        }

        if (page != null) {
            list = page.getContent();
        }


        model.addAttribute("order", order);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("blogList", list);

        if (!async && !isEmpty) {
            List<EsBlog> newest = esBlogService.listTop5NewestEsBlogs();
            model.addAttribute("newest", newest);
            List<EsBlog> hottest = esBlogService.listTop5HottestEsBlogs();
            model.addAttribute("hottest", hottest);
            List<TagVO> tags = esBlogService.listTop30Tags();
            model.addAttribute("tags", tags);
            List<User> users = esBlogService.listTop12Users();
            model.addAttribute("users", users);
        }

        return (async ? "index :: #mainContainerReplace" : "index");
    }

    @GetMapping("/newest")
    public String listNewestEsBlogs(Model model) {
        List<EsBlog> newest = esBlogService.listTop5NewestEsBlogs();
        model.addAttribute("newest", newest);
        return "newest";
    }

    @GetMapping("/hottest")
    public String listHottestEsBlogs(Model model) {
        List<EsBlog> hottest = esBlogService.listTop5HottestEsBlogs();
        model.addAttribute("hottest", hottest);
        return "hottest";
    }


}
