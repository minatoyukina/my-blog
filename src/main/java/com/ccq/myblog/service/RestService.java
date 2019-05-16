package com.ccq.myblog.service;

import com.ccq.myblog.domain.*;
import com.ccq.myblog.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestService {
    private final FilmRepository filmRepository;
    private final BlogRepository blogRepository;
    private final MusicRepository musicRepository;

    private final RestCommentRepository restCommentRepository;
    private final RestCommentReplyRepository restCommentReplyRepository;
    private final RestBoardRepository restBoardRepository;

    private final UserRepository userRepository;

    @Autowired
    public RestService(BlogRepository blogRepository, FilmRepository filmRepository, MusicRepository musicRepository, RestCommentRepository restCommentRepository, RestCommentReplyRepository restCommentReplyRepository, RestBoardRepository restBoardRepository, UserRepository userRepository) {
        this.blogRepository = blogRepository;
        this.filmRepository = filmRepository;
        this.musicRepository = musicRepository;
        this.restCommentRepository = restCommentRepository;
        this.restCommentReplyRepository = restCommentReplyRepository;
        this.restBoardRepository = restBoardRepository;
        this.userRepository = userRepository;
    }

    public Blog getBlog(Long id) {
        return blogRepository.findById(id).orElse(null);
    }

    public Page<Blog> listBlogs(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    public List<Blog> listTimeLine() {
        return blogRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public Page<Film> listFilms(Pageable pageable) {
        return filmRepository.findAll(pageable);
    }

    public List<Music> listMusic(Music.Type type) {
        return musicRepository.findAllByType(type);
    }

    public RestComment saveRestComment(RestComment restComment) {
        return restCommentRepository.save(restComment);
    }

    public Page<RestComment> listRestComments(Long blogId, Pageable pageable) {
        return restCommentRepository.findAllByBlogId(blogId, pageable);
    }

    public RestCommentReply saveRestCommentReply(RestCommentReply restCommentReply) {
        return restCommentReplyRepository.save(restCommentReply);
    }

    public RestBoard saveRestBoard(RestBoard restBoard) {
        return restBoardRepository.save(restBoard);
    }

    public Page<RestBoard> listRestBoard(Pageable pageable) {
        return restBoardRepository.findAll(pageable);
    }

    public RestComment findRestCommentById(Long commentId) {
        return restCommentRepository.getOne(commentId);
    }

    public RestBoard findRestBoardById(Long id) {
        return restBoardRepository.getOne(id);
    }

    public String checkPassword(String username) {
        return userRepository.findByUsername(username).getPassword();
    }

    public String[][] listCatalog() {
        return blogRepository.countByCatalog();
    }
}
