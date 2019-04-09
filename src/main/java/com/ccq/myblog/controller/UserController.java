package com.ccq.myblog.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;

import com.ccq.myblog.domain.Authority;
import com.ccq.myblog.domain.User;
import com.ccq.myblog.service.AuthorityService;
import com.ccq.myblog.service.UserService;
import com.ccq.myblog.util.ConstraintViolationExceptionHandler;
import com.ccq.myblog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/users")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")  // 指定角色权限才能操作方法
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthorityService authorityService;

	@GetMapping
	public ModelAndView list(@RequestParam(value="async",required=false) boolean async,
			@RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
			@RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
			@RequestParam(value="name",required=false,defaultValue="") String name,
			Model model) {
	 
		Pageable pageable = PageRequest.of(pageIndex, pageSize);
		Page<User> page = userService.listUsersByNameLike(name, pageable);
		List<User> list = page.getContent();
		
		model.addAttribute("page", page);
		model.addAttribute("userList", list);
		return new ModelAndView(async ? "users/list :: #mainContainerReplace" : "users/list", "userModel", model);
	}

	@GetMapping("/add")
	public ModelAndView createForm(Model model) {
		model.addAttribute("user", new User(null, null, null, null));
		return new ModelAndView("users/add", "userModel", model);
	}

	@PostMapping
	public ResponseEntity<Response> create(User user, Long authorityId) {
		List<Authority> authorities = new ArrayList<>();
		authorities.add(authorityService.getAuthorityById(authorityId));
		user.setAuthorities(authorities);
		
		if(user.getId() == null) {
			user.setEncodePassword(user.getPassword());
		}else {
			User originalUser = userService.getUserById(user.getId());
			String rawPassword = originalUser.getPassword();
			PasswordEncoder  encoder = new BCryptPasswordEncoder();
			String encodePassword = encoder.encode(user.getPassword());
			boolean isMatch = encoder.matches(rawPassword, encodePassword);
			if (!isMatch) {
				user.setEncodePassword(user.getPassword());
			}else {
				user.setPassword(user.getPassword());
			}
		}
		
		try {
			userService.saveUser(user);
		}  catch (ConstraintViolationException e)  {
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
		}
		
		return ResponseEntity.ok().body(new Response(true, "处理成功", user));
	}

	@DeleteMapping(value = "/{id}")
    public ResponseEntity<Response> delete(@PathVariable("id") Long id, Model model) {
		try {
			userService.removeUser(id);
		} catch (Exception e) {
			return  ResponseEntity.ok().body( new Response(false, e.getMessage()));
		}
		return  ResponseEntity.ok().body( new Response(true, "处理成功"));
	}

	@GetMapping(value = "edit/{id}")
	public ModelAndView modifyForm(@PathVariable("id") Long id, Model model) {
		User user = userService.getUserById(id);
		model.addAttribute("user", user);
		return new ModelAndView("users/edit", "userModel", model);
	}
}
