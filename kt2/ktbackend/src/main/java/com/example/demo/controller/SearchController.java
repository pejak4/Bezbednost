package com.example.demo.controller;

import com.example.demo.dto.emailDTO;
import com.example.demo.model.Users;
import com.example.demo.service.UserService;
import com.example.demo.view.UserLoginView;
import com.example.demo.view.UserTokenState;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.example.demo.model.UserRole.ADMIN;

@RestController
public class SearchController {

    @Autowired
    private UserService userService;

    @CrossOrigin(origins = "http://localhost:3000")
//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/search")
    public ResponseEntity<?> search(@Valid @RequestBody emailDTO email) throws Exception {
        Users loggedUser = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(ADMIN.equals(loggedUser.getRole()))
            return new ResponseEntity<>(this.userService.safeFindOneByEmail(email), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @CrossOrigin(origins = "http://localhost:3000")
//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/prevent")
    public ResponseEntity<?> prevent(@Valid @RequestBody emailDTO email) throws Exception {
        Users loggedUser = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(ADMIN.equals(loggedUser.getRole()))
            return new ResponseEntity<>(this.userService.xssPrevent(email), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
