package com.ccq.myblog.controller;

import java.util.List;

import javax.validation.ConstraintViolationException;

import com.ccq.myblog.domain.Catalog;
import com.ccq.myblog.domain.User;
import com.ccq.myblog.service.CatalogService;
import com.ccq.myblog.service.UserService;
import com.ccq.myblog.util.ConstraintViolationExceptionHandler;
import com.ccq.myblog.vo.CatalogVO;
import com.ccq.myblog.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/catalogs")
public class CatalogController {

	@Autowired
	private CatalogService catalogService;
	
	@Autowired
    private UserService userService;

	@GetMapping
	public String listComments(@RequestParam(value = "username") String username, Model model) {
        User user = (User) userService.loadUserByUsername(username);
		List<Catalog> catalogs = catalogService.listCatalogs(user);

		boolean isOwner = false;
		
		if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				 &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
			if (principal !=null && user.getUsername().equals(principal.getUsername())) {
				isOwner = true;
			} 
		} 
		
		model.addAttribute("isCatalogsOwner", isOwner);
		model.addAttribute("catalogs", catalogs);
		return "userspace/u :: #catalogRepleace";
	}

	@PostMapping
	@PreAuthorize("authentication.name.equals(#catalogVO.username)")
	public ResponseEntity<Response> create(@RequestBody CatalogVO catalogVO) {
		String username = catalogVO.getUsername();
		Catalog catalog = catalogVO.getCatalog();

        User user = (User) userService.loadUserByUsername(username);
		
		try {
			catalog.setUser(user);
			catalogService.saveCatalog(catalog);
		} catch (ConstraintViolationException e)  {
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		return ResponseEntity.ok().body(new Response(true, "处理成功!", null));
	}

	@DeleteMapping("/{id}")
	@PreAuthorize("authentication.name.equals(#username)")
	public ResponseEntity<Response> delete(String username, @PathVariable("id") Long id) {
		try {
			catalogService.removeCatalog(id);
		} catch (ConstraintViolationException e)  {
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		
		return ResponseEntity.ok().body(new Response(true, "处理成功", null));
	}

	@GetMapping("/edit")
	public String getCatalogEdit(Model model) {
		Catalog catalog = new Catalog(null, null);
		model.addAttribute("catalog",catalog);
		return "userspace/catalogedit";
	}

	@GetMapping("/edit/{id}")
	public String getCatalogById(@PathVariable("id") Long id, Model model) {
		Catalog catalog = catalogService.getCatalogById(id);
		model.addAttribute("catalog",catalog);
		return "userspace/catalogedit";
	}
	
}
