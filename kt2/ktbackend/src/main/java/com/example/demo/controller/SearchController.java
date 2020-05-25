package com.example.demo.controller;

import com.example.demo.dto.emailDTO;
import com.example.demo.service.UserService;
import com.example.demo.view.UserLoginView;
import com.example.demo.view.UserTokenState;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class SearchController {

    @Autowired
    private UserService userService;

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/search")
    public ResponseEntity<?> search(@Valid @RequestBody emailDTO email) throws Exception {
        return new ResponseEntity<>(this.userService.safeFindOneByEmail(email), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/prevent")
    public ResponseEntity<?> prevent(@Valid @RequestBody emailDTO email) throws Exception {
        return new ResponseEntity<>(this.userService.xssPrevent(email), HttpStatus.OK);
    }
}
