package com.gobra.sistemagestaodeobras.estudo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gobra.sistemagestaodeobras.model.Obra;
import com.gobra.sistemagestaodeobras.model.Socio;
import com.gobra.sistemagestaodeobras.repository.SocioRepository;

@Service
public class SocioService {

  @Autowired
  private SocioRepository socioRepository;


  public void associarSocioAObra(Integer idSocio, Integer idObra) {
    Socio socio = socioRepository.findById(idSocio).orElse(null);

    Obra obra = socio.getObras().stream()
      .filter(o -> o.getCodigo().equals(idObra))
      .findFirst()
      .orElse(null);

    socio.getObras().add(obra);
    obra.getSocios().add(socio);

    socioRepository.save(socio);
  }
}
