package com.auth.user.Controller;

import com.auth.user.DTO.LoginRequestDTO;
import com.auth.user.DTO.SignuRequestDTO;
import com.auth.user.Models.Token;
import com.auth.user.Models.User;
import com.auth.user.services.Userservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {//sign up

    @Autowired
    Userservice userservice;

    @PostMapping("/signup")
    public User signup(@RequestBody SignuRequestDTO signuRequestDTO)
    {
        String email= signuRequestDTO.getEmail();
     String password=   signuRequestDTO.getPassword();
     String name=signuRequestDTO.getName();

     return userservice.signup(name,email,password);
    }

    @PostMapping("/login")
    public Token login(@RequestBody LoginRequestDTO loginRequestDTO)
    {
        String email= loginRequestDTO.getEmail();
        String password=   loginRequestDTO.getPassword();
        return userservice.login(email,password);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestParam("token") String token)
    {
        userservice.logout(token);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/validate/{token}")
    public Boolean validatetoken(@PathVariable("token") String token){

        return userservice.validateToken(token);
    }


}
