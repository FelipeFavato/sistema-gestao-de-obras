// package com.gobra.sistemagestaodeobras.infra.security;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.Customizer;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
// import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
// import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
// import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
// import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
// import org.springframework.security.oauth2.core.oidc.user.OidcUser;
// import org.springframework.security.oauth2.core.user.OAuth2User;
// import org.springframework.security.provisioning.InMemoryUserDetailsManager;
// import org.springframework.security.web.SecurityFilterChain;

// import lombok.extern.log4j.Log4j2;


// @Configuration
// @Log4j2
// public class GoogleSecurityConfig {
//   // Referencia: https://www.youtube.com/watch?v=40BxatEr5aE&ab_channel=kanezi
//   // Parto daqui:

//   // @Bean
//   // @Order(0)
//   // SecurityFilterChain resources (HttpSecurity http) throws Exception {
//   //   return http
//   //           .securityMatcher("/images/**", "/**.css", "/**.js")
//   //           .authorizeHttpRequests(c -> c.anyRequest().permitAll())
//   //           .securityContext(c -> c.disable())
//   //           .sessionManagement(c -> c.disable())
//   //           .requestCache(c -> c.disable())
//   //           .build();
//   // }

//   // @Bean
//   // UserDetailsService inMemoryUsers() {
//   //   InMemoryUserDetailsManager users = new InMemoryUserDetailsManager();
//   // }

//   @Bean
//   SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
//     return http
//             .formLogin(Customizer.withDefaults())
//             .oauth2Login(Customizer.withDefaults())
//             .authorizeHttpRequests(c -> c.anyRequest().authenticated())
//             .build();
//   }

//   private OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2LoginHandler () {
//     return userRequest -> {
//       DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
//       // 
//       return delegate.loadUser(userRequest);
//     };
//   }

//   private OAuth2UserService<OidcUserRequest, OidcUser> oidcLoginHandler () {
//     return userRequest -> {
//       OidcUserService delegate = new OidcUserService();
//       return delegate.loadUser(userRequest);
//     };
//   }

//   @Bean
//   PasswordEncoder passwordEncoder() {
//     return new BCryptPasswordEncoder();
//   }

// }
