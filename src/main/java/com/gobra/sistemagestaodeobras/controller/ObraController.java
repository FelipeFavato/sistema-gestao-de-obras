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

import com.gobra.sistemagestaodeobras.dao.ObraDAOJDBCImplemented;
import com.gobra.sistemagestaodeobras.dto.ObraReqResDTO;
import com.gobra.sistemagestaodeobras.model.Obra;


@RestController
@RequestMapping("/api/obra")
public class ObraController {

  @Autowired
  private ObraDAOJDBCImplemented obraDAOJDBC;

  
  @PostMapping
  public void saveObra(@RequestBody ObraReqResDTO data) {
    Obra obraData = new Obra();
    obraData.setCodigo(data.codigo());
    obraData.setNome(data.nome());
    obraData.setEndereco(data.endereco());
    obraData.setDataInicio(data.dataInicio());
    obraData.setDataPrevistaFim(data.dataPrevistaFim());
    obraData.setDataRealFim(data.dataRealFim());
    obraData.setCustoPrevisto(data.custoPrevisto());
    obraData.setCompras(data.compras());
    obraData.setSocios(data.socios());
    obraDAOJDBC.save(obraData);
  }

  @GetMapping
  public List<ObraReqResDTO> getAll() {
    List<ObraReqResDTO> obraList = obraDAOJDBC.getAll().stream().map(ObraReqResDTO::new).toList();
    return obraList;
  }

  @PutMapping
  @Transactional
  public ResponseEntity<Obra> updateObra(@RequestBody ObraReqResDTO data) {
    Obra returnedObra = obraDAOJDBC.getById(data.codigo());

    // Obra updatedObra = returnedObra;
    returnedObra.setCodigo(data.codigo());
    returnedObra.setNome(data.nome());
    returnedObra.setEndereco(data.endereco());
    returnedObra.setDataInicio(data.dataInicio());
    returnedObra.setDataPrevistaFim(data.dataPrevistaFim());
    returnedObra.setDataRealFim(data.dataRealFim());
    returnedObra.setCustoPrevisto(data.custoPrevisto());
    returnedObra.setCompras(data.compras());
    returnedObra.setSocios(data.socios());

    obraDAOJDBC.update(returnedObra);

    return ResponseEntity.ok(returnedObra);
  }

  @DeleteMapping
  public void deletaObra(@RequestBody Obra data) {
    obraDAOJDBC.deleteById(data.getCodigo());
  }
}
