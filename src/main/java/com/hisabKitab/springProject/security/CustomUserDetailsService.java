package com.hisabKitab.springProject.security;

import com.hisabKitab.springProject.entity.UserEntity;
import com.hisabKitab.springProject.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private  UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) {
        UserEntity user = userRepository.getByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found"));

        System.out.println("Loaded user: " + user.getEmail() + ", Role: " + user.getRole()); // Debug

        return new CustomUserDetails(user);
    }

}
