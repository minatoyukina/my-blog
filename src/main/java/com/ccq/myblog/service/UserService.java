package com.ccq.myblog.service;

import java.util.Collection;
import java.util.List;

import com.ccq.myblog.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface UserService {

	User saveUser(User user);

	void removeUser(Long id);

	void removeUsersInBatch(List<User> users);

	User updateUser(User user);

	User getUserById(Long id);

	List<User> listUsers();

	Page<User> listUsersByNameLike(String name, Pageable pageable);

	List<User> listUsersByUsernames(Collection<String> usernames);
}
