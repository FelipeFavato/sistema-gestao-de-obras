package com.gobra.sistemagestaodeobras.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


// 1. @Configuration: Indica para o Spring que essa é uma classe de configuração.
// 2. @EnableWebSecurity: Essa notação indica que é pra habilitar a configuração
//    webSecurity, que eu vou configurar ela aqui dentro dessa classe. E que a
//    config padrão é pra deixar de usar.
@Configuration
@EnableWebSecurity
public class SecurityConfig {
  // https://www.youtube.com/watch?v=us0VjFiHogo&t=1s&ab_channel=DanVega

  @Autowired
  SecurityFilter securityFilter;

  @Value("${spring.security.oauth2.client.registration.google.client-id}")
  private String CLIENTID;

  @Value("${spring.security.oauth2.client.registration.google.client-secret}")
  private String CLIENTSECRET;
  
  // Método de 'CorrenteDeFiltroDeSegurança': Corrente de filtros que eu vou aplicar
  // minha requisição para fazer a segurança da minha aplicação. Os filtros seriam
  // diversos métodos que vou realizar quando o usuario realizar a requisição para verificar a autenticação.
  // 1. @Bean: Essa notação permite que o Spring instancie a classe.
  // 2. .csrf(csrf -> csrf.disable()): Recebo um objeto 'csrf' e desabilito ele, desligando essa configuração.
  // 2.1. .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)):
  //     Declara que a politica de sessão é STATELESS.
  // 2.2. build(): Cria o objeto securityFilterChain.
  // 2.3. authorizeHttpRequests(authorize -> authorize): Declarando quais são as requisições HTTP que precisam
  //      ser autorizadas/autenticadas.
  // 2.4. .requestMatchers(HttpMethod.POST, "/api/fornecedor").hasRole("Gestor"): Apenas usuários com o perfil
  //      'Gestor' consegue realizar requisições POST na rota "/api/fornecedor".
  // 2.5. .anyRequest().authenticated(): Demais requisições estão autorizadas por qualquer tipo de perfil, desde
  //      que seja um usuário autenticado.
  @Bean
  public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception {
    return  httpSecurity
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
              .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
              .requestMatchers(HttpMethod.POST, "/api/auth/registrar").hasRole("ADMIN")
              .requestMatchers(HttpMethod.GET, "/api/usuario").hasRole("ADMIN")
              .requestMatchers("/api/**").authenticated()
              .anyRequest().permitAll()
            )
            .oauth2Login(Customizer.withDefaults())
            .formLogin(Customizer.withDefaults())
            .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
  }

  @Bean
  public AuthenticationManager authenticationManager (AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder () {
    return new BCryptPasswordEncoder();
  }

  // @Bean
  // public ClientRegistrationRepository clientRegistrationRepository() {
  //   return new InMemoryClientRegistrationRepository(
  //     ClientRegistration.withRegistrationId("google")
  //       .clientId(CLIENTID)
  //       .clientSecret(CLIENTSECRET)
  //       .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
  //       .redirectUriTemplate("{baseUrl}/login/oauth2/code/{registrationId}")
  //       .scope("openid", "profile", "email")
  //       .authorizationUri("https://accounts.google.com/o/oauth2/auth")
  //       .tokenUri("https://oauth2.googleapis.com/token")
  //       .userInfoUri("https://openidconnect.googleapis.com/v1/userinfo")
  //       .userNameAttributeName(IdTokenClaimNames.SUB)
  //       .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
  //       .clientName("Google")
  //       .build()
  //   );
  // }

}
