package com.ccq.myblog.repository;

import com.ccq.myblog.domain.RestComment;
import com.ccq.myblog.domain.RestCommentReply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestCommentReplyRepository extends JpaRepository<RestCommentReply, Long> {

}
