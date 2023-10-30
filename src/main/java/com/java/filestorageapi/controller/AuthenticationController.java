package com.java.filestorageapi.controller;


import com.java.filestorageapi.model.AuthenticationRequest;
import com.java.filestorageapi.model.AuthenticationResponse;
import com.java.filestorageapi.model.RegisterRequest;
import com.java.filestorageapi.service.AuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/authenticate")
@RequiredArgsConstructor
@Api(value = "User Api documentation")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ApiOperation(value = "New User registering method")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request){

        return ResponseEntity.ok(authenticationService.register(request));

    }

    @PostMapping("/authenticate")
    @ApiOperation(value = "User authenticating method")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody AuthenticationRequest request){

        return ResponseEntity.ok(authenticationService.authenticate(request));

    }
}
