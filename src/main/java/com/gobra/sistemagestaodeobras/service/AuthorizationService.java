package com.gobra.sistemagestaodeobras.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;

// Esse serviço vai ser chamada automaticamente pelo Spring Security toda vez
// que um usuário precisar se autenticar. Alguns métodos serão definidos para
// ajudar o Spring Security a fazer essa autenticação.
// Sessão stateFull: O servidor armazena informações de todas as sessões ativas.
// Sessão stateLess: Faz autenticações via token.

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gobra.sistemagestaodeobras.repository.UsuarioRepository;

// 1. @Service: Essa notação permite que o Spring Security identifique que essa classe
//    é uma classe de Service.
// 2. implements UserDetailsService: Ajuda o Spring identificar que essa classe é o
//    AuthorizationService (sendo chamada de forma automatica).
@Service
public class AuthorizationService implements UserDetailsService {

  // Vou precisar que essa classe tenha uma instância do meu UsuarioRepository, para que
  // eu consiga fazer a consulta no banco de dados.
  @Autowired
  UsuarioRepository repository;

  // Toda vez que alguem tentar se autenticar na nossa aplicação, o Spring Security precisa
  // de uma forma para consultar esses usuários.
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    // Faz a consulta no banco de dados pelo email.
    return repository.findByEmail(username);
  }
  
}
