package com.shehta.ecommerce.service;

import com.shehta.ecommerce.model.Users;
import com.shehta.ecommerce.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private  JWTService jwtService;

    @Autowired
    private AuthenticationManager authManger;
    private BCryptPasswordEncoder encoder=new BCryptPasswordEncoder(5);
    private final UserRepo repo;
    public UserService(UserRepo repo) {

        this.repo = repo;
    }
    public Users register(Users user)
    {
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

    public List<Users> getUsers() {

        return repo.findAll();
    }

    public  String verify(Users user)
    {
        Authentication authentication=
                authManger.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
        if(authentication.isAuthenticated())
        {
            return jwtService.generateToken(user.getUsername());
        }
        return "faild";
    }
}
