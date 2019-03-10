package com.ccq.myblog.repository;

import java.util.Collection;
import java.util.List;

import com.ccq.myblog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>{

	Page<User> findByNameLike(String name, Pageable pageable);

	User findByUsername(String username);

	List<User> findByUsernameIn(Collection<String> usernames);
}
