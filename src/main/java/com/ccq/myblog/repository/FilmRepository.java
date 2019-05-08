package com.ccq.myblog.repository;

import com.ccq.myblog.domain.Film;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilmRepository extends JpaRepository<Film, Long> {
}
