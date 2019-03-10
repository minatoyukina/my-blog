package com.ccq.myblog.repository;

import com.ccq.myblog.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long>{
 
}
