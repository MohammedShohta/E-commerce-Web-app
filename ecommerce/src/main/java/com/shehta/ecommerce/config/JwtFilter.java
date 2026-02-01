package com.shehta.ecommerce.config;

import com.shehta.ecommerce.service.JWTService;
import com.shehta.ecommerce.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtFilter;


    private final MyUserDetailsService myUserDetailsService;

    public JwtFilter(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }


    @Override
    public void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader=request.getHeader("Authorization");
        String token=null;
        String username=null;
        if(authHeader!=null &&authHeader.startsWith("Bearer "))
        {
            token=authHeader.substring(7);
            username=jwtFilter.extractUserName(token);
        }
        if(username !=null && SecurityContextHolder.getContext().getAuthentication()==null)
        {
            UserDetails userDetails=myUserDetailsService.loadUserByUsername(username);

            //true or false
            if(jwtFilter.validateToken(token,userDetails))
            {
                //create auth object and fill out the data
                UsernamePasswordAuthenticationToken authToken=
                        new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                //add extra info in request  like ip address or session id
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //added user in auth container to login
                SecurityContextHolder.getContext().setAuthentication(authToken);

            }

        }
        filterChain.doFilter(request,response);

    }
}
