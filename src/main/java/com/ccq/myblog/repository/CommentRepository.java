package com.ccq.myblog.repository;

import com.ccq.myblog.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>{
 
}
