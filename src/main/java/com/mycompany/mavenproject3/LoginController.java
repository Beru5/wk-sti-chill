package com.mycompany.mavenproject3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private loginService loginService;

    @PostMapping
    public String login(@RequestBody Admin loginRequest) {
        boolean success = loginService.cekLogin(loginRequest.getUsername(), loginRequest.getPassword());
        return success ? "Login successful" : "Invalid username or password";
    }
}