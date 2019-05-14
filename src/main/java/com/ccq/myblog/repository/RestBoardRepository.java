package com.ccq.myblog.repository;

import com.ccq.myblog.domain.RestBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestBoardRepository extends JpaRepository<RestBoard, Long> {

}
