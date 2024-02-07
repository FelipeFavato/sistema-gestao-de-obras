package com.gobra.sistemagestaodeobras.infra.security;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class GoogleSecurityConfig {
  // Referencia: https://www.youtube.com/watch?v=40BxatEr5aE&ab_channel=kanezi
  // Parto daqui:


  private OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2LoginHandler () {
    return null;
  }

  private OAuth2UserService<OidcUserRequest, OidcUser> oidcLoginHandler () {
    return null;
  }
}
