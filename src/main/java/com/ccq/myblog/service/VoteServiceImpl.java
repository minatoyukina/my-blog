package com.ccq.myblog.service;

import javax.transaction.Transactional;

import com.ccq.myblog.domain.Vote;
import com.ccq.myblog.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VoteServiceImpl implements VoteService {

	@Autowired
	private VoteRepository voteRepository;

	@Override
	@Transactional
	public void removeVote(Long id) {
		voteRepository.deleteById(id);
	}
	@Override
	public Vote getVoteById(Long id) {
		return voteRepository.findById(id).orElse(null);
	}

}
