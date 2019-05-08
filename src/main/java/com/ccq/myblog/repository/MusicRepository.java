package com.ccq.myblog.repository;

import com.ccq.myblog.domain.Music;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MusicRepository extends JpaRepository<Music, Long> {
    List<Music> findAllByType(Music.Type type);
}
