package com.ccq.myblog.service;

import com.ccq.myblog.domain.Blog;
import com.ccq.myblog.domain.Film;
import com.ccq.myblog.domain.Music;
import com.ccq.myblog.repository.BlogRepository;
import com.ccq.myblog.repository.FilmRepository;
import com.ccq.myblog.repository.MusicRepository;
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

    @Autowired
    public RestService(BlogRepository blogRepository, FilmRepository filmRepository, MusicRepository musicRepository) {
        this.blogRepository = blogRepository;
        this.filmRepository = filmRepository;
        this.musicRepository = musicRepository;
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
}
