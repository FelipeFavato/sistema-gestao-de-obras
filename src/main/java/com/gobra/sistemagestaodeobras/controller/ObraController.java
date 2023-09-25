package com.gobra.sistemagestaodeobras.controller;

import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.gobra.sistemagestaodeobras.dao.ObraDAO;
// import com.gobra.sistemagestaodeobras.dao.ObraDAOJDBCImplemented;
import com.gobra.sistemagestaodeobras.dto.ObraReqResDTO;
import com.gobra.sistemagestaodeobras.model.Obra;

@RestController
@RequestMapping("/api/obra")
public class ObraController {

  ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");

  ObraDAO obraDAO = ctx.getBean("obraDAOJDBCImplemented", ObraDAO.class);

  // @Autowired
  // private ObraDAOJDBCImplemented obraDAOJDBC;

  @PostMapping
  public void saveObra(@RequestBody ObraReqResDTO data) {
    Obra obraData = new Obra();
    // obraData.setCodigo(data.codigo());
    obraData.setNome(data.nome());
    obraData.setEndereco(data.endereco());
    obraData.setDataInicio(data.dataInicio());
    obraData.setDataPrevistaFim(data.dataPrevistaFim());
    obraData.setDataRealFim(data.dataRealFim());
    obraData.setCustoPrevisto(data.custoPrevisto());
    obraDAO.save(obraData);
    ctx.close();
  }

  @GetMapping
  public List<ObraReqResDTO> getAll() {
    List<ObraReqResDTO> obraList = obraDAO.getAll().stream().map(ObraReqResDTO::new).toList();
    return obraList;
  }

}
