package com.ccq.myblog.controller;

import com.ccq.myblog.domain.*;
import com.ccq.myblog.service.BlogService;
import com.ccq.myblog.service.EsBlogService;
import com.ccq.myblog.service.RestService;
import com.ccq.myblog.vo.PageVO;
import com.ccq.myblog.vo.TagVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restapi")
public class RestBlogController {
    private final RestService restService;
    private final EsBlogService esBlogService;
    private final BlogService blogService;

    private final ObjectMapper objectMapper;

    @Autowired
    public RestBlogController(RestService restService, EsBlogService esBlogService, BlogService blogService, ObjectMapper objectMapper) {
        this.restService = restService;
        this.esBlogService = esBlogService;
        this.blogService = blogService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public PageVO<Blog> listBlogs(@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
                                  @RequestParam(value = "pageSize", required = false, defaultValue = "5") int pageSize) {
        PageVO<Blog> pageVO = new PageVO<>();
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
        Page<Blog> blogs = restService.listBlogs(pageable);
        long totalElements = blogs.getTotalElements();
        pageVO.setTotalElements(totalElements);
        int totalPages = blogs.getTotalPages();
        pageVO.setTotalPages(totalPages);
        pageVO.setList(blogs.getContent());
        return pageVO;
    }

    @GetMapping("/search")
    public PageVO<EsBlog> listEsBlogs(@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
                                      @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                      @RequestParam(value = "pageSize", required = false, defaultValue = "5") int pageSize) {
        PageVO<EsBlog> pageVO = new PageVO<>();
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
        Page<EsBlog> blogs = esBlogService.listEsBlogs(pageable);
        if (!keyword.equals("")) {
            sort = new Sort(Sort.Direction.DESC, "readSize", "commentSize", "voteSize", "createTime");
            pageable = PageRequest.of(pageIndex, pageSize, sort);
            blogs = esBlogService.listHottestEsBlogs(keyword, pageable);
        }
        long totalElements = blogs.getTotalElements();
        pageVO.setTotalElements(totalElements);
        int totalPages = blogs.getTotalPages();
        pageVO.setTotalPages(totalPages);
        pageVO.setList(blogs.getContent());
        return pageVO;
    }

    @GetMapping("/hottest")
    public List<EsBlog> listHottestEsBlogs() {
        return esBlogService.listTop5HottestEsBlogs();
    }

    @GetMapping("/{id}")
    public Blog getBlog(@PathVariable("id") Long id) {
        blogService.readingIncrease(id);
        return restService.getBlog(id);
    }

    @GetMapping("/timeline")
    public List<Blog> listTimeLine() {
        return restService.listTimeLine();
    }

    @GetMapping("/film")
    public PageVO<Film> listFilms(@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
                                  @RequestParam(value = "pageSize", required = false, defaultValue = "5") int pageSize) {
        PageVO<Film> pageVO = new PageVO<>();
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
        Page<Film> films = restService.listFilms(pageable);
        long totalElements = films.getTotalElements();
        pageVO.setTotalElements(totalElements);
        int totalPages = films.getTotalPages();
        pageVO.setTotalPages(totalPages);
        pageVO.setList(films.getContent());
        return pageVO;
    }

    @GetMapping("/music")
    public List<Music> listMusic(@RequestParam(value = "type", defaultValue = "WEEK") String type) {
        return restService.listMusic(Music.Type.valueOf(type));
    }

    @GetMapping("/tag")
    public List<TagVO> list30Tags() {
        return esBlogService.listTop30Tags();
    }

    @GetMapping("/comment")
    public PageVO<RestComment> listRestComments(@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
                                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                                                @RequestParam("blogId") Long blogId) {
        PageVO<RestComment> pageVO = new PageVO<>();
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
        Page<RestComment> restComments = restService.listRestComments(blogId, pageable);
        long totalElements = restComments.getTotalElements();
        pageVO.setTotalElements(totalElements);
        int totalPages = restComments.getTotalPages();
        pageVO.setTotalPages(totalPages);
        pageVO.setList(restComments.getContent());
        return pageVO;
    }

    @GetMapping("/board")
    public PageVO<RestBoard> listRestBoard(@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
                                           @RequestParam(value = "pageSize", required = false, defaultValue = "30") int pageSize) {
        PageVO<RestBoard> pageVO = new PageVO<>();
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
        Page<RestBoard> restBoards = restService.listRestBoard(pageable);
        long totalElements = restBoards.getTotalElements();
        pageVO.setTotalElements(totalElements);
        int totalPages = restBoards.getTotalPages();
        pageVO.setTotalPages(totalPages);
        pageVO.setList(restBoards.getContent());
        return pageVO;
    }

    @PostMapping("/comment")
    public RestComment saveRestComment(@RequestParam("blogId") Long blogId, @RequestBody RestComment restComment) {
        restComment.setBlogId(blogId);
        return restService.saveRestComment(restComment);
    }

    @PostMapping("/reply")
    public RestCommentReply saveRestCommentReply(@RequestParam("commentId") Long commentId, @RequestBody RestCommentReply restCommentReply) {
        restCommentReply.setRestComment(restService.findRestCommentById(commentId));
        return restService.saveRestCommentReply(restCommentReply);
    }

    @PostMapping("/board")
    public RestBoard saveRestBoard(@RequestBody RestBoard restBoard) {
        System.out.println(restBoard);
        return restService.saveRestBoard(restBoard);
    }

    @PostMapping("/login")
    public ObjectNode login(String username, String password) {
        System.out.println(username + password);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        ObjectNode jsonObject = objectMapper.createObjectNode();
        if (username.equals("admin")) {
            String encode = restService.checkPassword(username);
            System.out.println(encode);
            if (passwordEncoder.matches(password, encode)) {
                jsonObject.put("username", username);
                jsonObject.put("role", "admin");
            }
        }
        return jsonObject;
    }
}
