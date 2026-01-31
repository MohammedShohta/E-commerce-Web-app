package com.shehta.ecommerce.controller;


import com.shehta.ecommerce.model.Users;

import com.shehta.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


 
@RestController
public class UserController {
    @Autowired
    private  UserService userService;

    @PostMapping("/register")
    public Users register(@RequestBody  Users user)
    {
        return userService.register(user);
    }
    @GetMapping("/users")
    public List<Users> getUsers()
    {
        return userService.getUsers();
    }

    @PostMapping("/login")
    public String login(@RequestBody Users user)
    {
        return userService.verify(user);
    }
}
