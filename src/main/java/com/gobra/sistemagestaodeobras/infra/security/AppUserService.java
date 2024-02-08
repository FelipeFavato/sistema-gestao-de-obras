package com.gobra.sistemagestaodeobras.infra.security;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import lombok.Value;

@Service
@Value
public class AppUserService implements UserDetailsService {
  
  PasswordEncoder passwordEncoder;

  Map<String, AppUser> users = new HashMap<>();

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return users.get(username);
  }

  @PostConstruct
  private void createHardCodedUsers () {
    var bob = AppUser.builder()
                .username("bob")
                .password(passwordEncoder.encode("1234"))
                .authorities(List.of(new SimpleGrantedAuthority("read")))
                .build();
    
    var bil = AppUser.builder()
                .username("bil")
                .password(passwordEncoder.encode("321"))
                .authorities(List.of(new SimpleGrantedAuthority("read")))
                .build(); 
    
    this.createUser(bob);
    createUser(bil);
  }

  private void createUser(AppUser user) {
    users.putIfAbsent(user.getUsername(), user);
  }


}
