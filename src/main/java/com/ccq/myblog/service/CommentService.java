package com.ccq.myblog.service;

import com.ccq.myblog.domain.Comment;

public interface CommentService {

	Comment getCommentById(Long id);

	void removeComment(Long id);
}
