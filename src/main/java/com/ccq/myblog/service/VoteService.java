package com.ccq.myblog.service;

import com.ccq.myblog.domain.Vote;

public interface VoteService {

	Vote getVoteById(Long id);

	void removeVote(Long id);
}
