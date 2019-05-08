package com.ccq.myblog.controller;

import com.ccq.myblog.domain.EsBlog;
import com.ccq.myblog.domain.Film;
import com.ccq.myblog.domain.Blog;
import com.ccq.myblog.domain.Music;
import com.ccq.myblog.service.EsBlogService;
import com.ccq.myblog.service.RestService;
import com.ccq.myblog.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restapi")
public class RestBlogController {
    private final RestService restService;
    private final EsBlogService esBlogService;

    @Autowired
    public RestBlogController(RestService blogService, EsBlogService esBlogService) {
        this.restService = blogService;
        this.esBlogService = esBlogService;
    }

    @GetMapping
    public PageVO<EsBlog> listBlogs(@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
                                    @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
                                    @RequestParam(value = "pageSize", required = false, defaultValue = "5") int pageSize) {
        PageVO<EsBlog> pageVO = new PageVO<>();
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);
        Page<EsBlog> blogs = esBlogService.listEsBlogs(pageable);
//        blogs.getContent().forEach(x->System.out.println(x.getUser().getName()));
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

    @GetMapping("/{id}")
    public Blog getBlog(@PathVariable("id") Long id) {
        return restService.getBlog(id);
    }

    @GetMapping("/timeline")
    public List<Blog> getTimeLine() {
        return restService.listTimeLine();
    }

    @GetMapping("/film")
    public PageVO<Film> getFilms(@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
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
    public List<Music> getMusic(@RequestParam(value = "type", defaultValue = "WEEK") String type) {
        return restService.listMusic(Music.Type.valueOf(type));
    }
}
