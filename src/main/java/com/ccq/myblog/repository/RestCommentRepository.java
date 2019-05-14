package com.ccq.myblog.repository;

import com.ccq.myblog.domain.RestComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestCommentRepository extends JpaRepository<RestComment, Long> {
    Page<RestComment> findAllByBlogId(Long blogId, Pageable pageable);
}
