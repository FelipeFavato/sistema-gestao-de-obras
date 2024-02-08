package com.gobra.sistemagestaodeobras.infra.security;


import java.util.UUID;

import org.jfree.util.Log;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
// @Log4j2
public class GoogleSecurityConfig {
  // Referencia: https://www.youtube.com/watch?v=40BxatEr5aE&ab_channel=kanezi
  // Parto daqui:

  // @Bean
  // @Order(0)
  // SecurityFilterChain resources (HttpSecurity http) throws Exception {
  //   return http
  //           .securityMatcher("/images/**", "/**.css", "/**.js")
  //           .authorizeHttpRequests(c -> c.anyRequest().permitAll())
  //           .securityContext(c -> c.disable())
  //           .sessionManagement(c -> c.disable())
  //           .requestCache(c -> c.disable())
  //           .build();
  // }


  @Bean
  SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
    return http
            .formLogin(Customizer.withDefaults())
            .oauth2Login(oc -> oc.userInfoEndpoint(ui -> ui.userService(oauth2LoginHandler()).oidcUserService(oidcLoginHandler())))
            .authorizeHttpRequests(c -> c.anyRequest().authenticated())
            .build();
  }

  private OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2LoginHandler () {
    return userRequest -> {
      LoginProvider provider = LoginProvider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
      DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

      OAuth2User oAuth2User = delegate.loadUser(userRequest);
      return AppUser
              .builder()
              .provider(provider)
              .username(oAuth2User.getAttribute("login"))
              .name(oAuth2User.getAttribute("login"))
              .password(passwordEncoder().encode(UUID.randomUUID().toString()))
              .userId(oAuth2User.getName())
              .imageUrl(oAuth2User.getAttribute("avatar_url"))
              .attributes(oAuth2User.getAttributes())
              .authorities(oAuth2User.getAuthorities())
              .build();
    };
  }

  private OAuth2UserService<OidcUserRequest, OidcUser> oidcLoginHandler () {
    return userRequest -> {
      LoginProvider provider = LoginProvider.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
      OidcUserService delegate = new OidcUserService();
      //
      OidcUser oidcUser = delegate.loadUser(userRequest);
      return AppUser
              .builder()
              .provider(provider)
              .username(oidcUser.getEmail())
              .name(oidcUser.getFullName())
              .email(oidcUser.getEmail())
              .userId(oidcUser.getName())
              .imageUrl(oidcUser.getAttribute("picture"))
              .password(passwordEncoder().encode(UUID.randomUUID().toString()))
              .attributes(oidcUser.getAttributes())
              .authorities(oidcUser.getAuthorities())
              .build();
    };
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  ApplicationListener<AuthenticationSuccessEvent> successLogger () {
    return event -> {
      Log.info(event.getAuthentication());
    };
  }


}
