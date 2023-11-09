package com.gobra.sistemagestaodeobras.controller;

import java.util.List;
import java.util.Optional;

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

import com.gobra.sistemagestaodeobras.dto.ObraRequestDTO;
import com.gobra.sistemagestaodeobras.dto.ObraResponseDTO;
import com.gobra.sistemagestaodeobras.model.Obra;
import com.gobra.sistemagestaodeobras.repository.ObraRepository;

import jakarta.persistence.EntityNotFoundException;



@RestController
@RequestMapping("/api/obra")
public class ObraController {
  
  @Autowired
  private ObraRepository repository;

  @PostMapping
  public void saveObra(@RequestBody ObraRequestDTO data) {
    Obra obraData = new Obra(data);
    
    repository.save(obraData);
  }


  @GetMapping
  public List<ObraResponseDTO> getAll() {
    List<ObraResponseDTO> obrasList = repository.findAll().stream().map(ObraResponseDTO::new).toList();
    return obrasList;
  }

  @PutMapping
  @Transactional
  public ResponseEntity<Obra> updateObra(@RequestBody ObraRequestDTO data) {
    Optional<Obra> optionalObra = repository.findById(data.codigo());

    if (optionalObra.isPresent()) {
      Obra obra = optionalObra.get();
      obra.setNome(data.nome());
      obra.setEndereco(data.endereco());
      obra.setDataInicio(data.dataInicio());
      obra.setDataPrevistaFim(data.dataPrevistaFim());
      obra.setDataRealFim(data.dataRealFim());
      obra.setCustoMaoDeObra(data.custoMaoDeObra());
      obra.setCustoPrevisto(data.custoPrevisto());
      obra.setCompras(data.compras());
      obra.setSocios(data.socios());

      return ResponseEntity.ok(obra);
    } else {
      throw new EntityNotFoundException();
    }
  }

  @DeleteMapping
  public void deletaCompra(@RequestBody Obra obra) {
    repository.delete(obra);
  }
}
