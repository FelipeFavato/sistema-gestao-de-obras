// https://www.youtube.com/watch?v=5w-YCcOjPD0&t=302s&ab_channel=FernandaKipper%7CDev
// Voltar no tempo 00:35:00 e reouvir a explicação

package com.gobra.sistemagestaodeobras.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gobra.sistemagestaodeobras.dto.LoginResponseDTO;
import com.gobra.sistemagestaodeobras.dto.AuthenticationDTO;
import com.gobra.sistemagestaodeobras.dto.RegistroDTO;
import com.gobra.sistemagestaodeobras.infra.security.TokenService;
import com.gobra.sistemagestaodeobras.model.Usuario;
import com.gobra.sistemagestaodeobras.repository.UsuarioRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

  // Indica da onde essa classe pega a 'AuthenticationManager' no arquivo de configuração
  // 'SecurityConfig.java'.
  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UsuarioRepository usuarioRepository;

  @Autowired
  private TokenService tokenService;

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDTO> login (@RequestBody AuthenticationDTO data) {
    // UsernamePasswordAuthenticationToken(): Essa classe vem do Spring Security e
    //  recebe como parametro (loginUsuario(), senha()).
    var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());

    // .authenticate(): Recebe como parametro (UsernamePasswordAuthenticationToken) que acabou
    //  de ser criado na linha acima.
    var auth = this.authenticationManager.authenticate(usernamePassword);

    var token = tokenService.generateToken((Usuario) auth.getPrincipal());

    return ResponseEntity.ok(new LoginResponseDTO(token));
    // return ResponseEntity.ok().build();
  }

  @PostMapping("/registrar")
  public ResponseEntity<Usuario> register (@RequestBody RegistroDTO data) {
    if (this.usuarioRepository.findByEmail(data.email()) != null) return ResponseEntity.badRequest().build();

    String encryptedSenha = new BCryptPasswordEncoder().encode(data.senha());
    Usuario newUsuario = new Usuario(data.nome(), data.email(), encryptedSenha, data.role());

    this.usuarioRepository.save(newUsuario);

    return ResponseEntity.ok(newUsuario);
    // return ResponseEntity.ok().build();
  }
}
