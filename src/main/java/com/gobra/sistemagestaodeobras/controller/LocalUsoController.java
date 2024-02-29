// Controller => Concentra os Requests para captar os
// dados e adicionar novas informações no BD

package com.gobra.sistemagestaodeobras.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gobra.sistemagestaodeobras.dto.LocalUsoRequestDTO;
import com.gobra.sistemagestaodeobras.dto.LocalUsoResponseDTO;
import com.gobra.sistemagestaodeobras.model.LocalUso;
import com.gobra.sistemagestaodeobras.service.LocalUsoService;



@RestController  // Essa notação indica que essa classe é um controller
@RequestMapping("/api/localuso")  // Essa notação indica qual endpoint essa classe controla
public class LocalUsoController {

  @Autowired
  private LocalUsoService localUsoService;


  // Ajuda na conexão com o Front. "*" = permite a conexão de todas as origens
  // @CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")  // trocar origins = "http://localhost/8080" (mais seguro)
  @GetMapping
  public List<LocalUsoResponseDTO> getAll () {
    return localUsoService.getAllLocalUso();
  }

  @PostMapping
  public ResponseEntity<?> saveOne (@RequestBody LocalUsoRequestDTO data) {
    return localUsoService.saveLocalUso(data);
  }

  @PutMapping
  @Transactional  // Método só deve ser executado se todas as transações tiverem sucesso
  public ResponseEntity<LocalUso> updateOne (@RequestBody LocalUsoRequestDTO data){
    return localUsoService.updateLocalUso(data);
  }

  @DeleteMapping
  public ResponseEntity<?> deleteOne (@RequestBody LocalUso localUso) {
    return localUsoService.deletaLocalUso(localUso);
  }

}
