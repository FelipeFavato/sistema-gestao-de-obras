// package com.gobra.sistemagestaodeobras.controller;

// import java.util.List;
// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// // import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.gobra.sistemagestaodeobras.dto.UsuarioRequestDTO;
// import com.gobra.sistemagestaodeobras.dto.UsuarioResponseDTO;
// import com.gobra.sistemagestaodeobras.model.Usuario;
// import com.gobra.sistemagestaodeobras.repository.UsuarioRepository;

// import jakarta.persistence.EntityNotFoundException;
// import jakarta.transaction.Transactional;

// @RestController
// @RequestMapping("/api/usuario")
// public class UsuarioController {
  
//   @Autowired
//   private UsuarioRepository repository;

//   // @PostMapping
//   // public void saveUsuario(@RequestBody UsuarioRequestDTO data) {
//   //   Usuario usuarioData = new Usuario(data);
//   //   repository.save(usuarioData);
//   // }

//   @GetMapping
//   public List<UsuarioResponseDTO> getAll() {
//     List<UsuarioResponseDTO> usuarioList = repository.findAll().stream().map(UsuarioResponseDTO::new).toList();
//     return usuarioList;
//   }

//   @PutMapping
//   @Transactional
//   public ResponseEntity<Usuario> updateUsuario(@RequestBody UsuarioRequestDTO data) {
//     Optional<Usuario> optionalUsuario = repository.findById(data.codigo());

//     if (optionalUsuario.isPresent()) {
//       Usuario usuario = optionalUsuario.get();
//       usuario.setNome(data.nome());
//       usuario.setEmail(data.email());
//       usuario.setSenha(data.senha());
//       usuario.setTipoPerfil(data.tipoPerfil());
//       // usuario.setPerfil(data.perfil());
    
//       return ResponseEntity.ok(usuario);
//     } else {
//       throw new EntityNotFoundException();
//     }
//   }

//   @DeleteMapping
//   public void deletaUsuario(@RequestBody Usuario usuario) {
//     repository.delete(usuario);
//   }
// }
