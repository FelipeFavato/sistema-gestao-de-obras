package com.gobra.sistemagestaodeobras.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gobra.sistemagestaodeobras.model.Obra;
import com.gobra.sistemagestaodeobras.model.Socio;
import com.gobra.sistemagestaodeobras.repository.ObraRepository;
import com.gobra.sistemagestaodeobras.repository.SocioRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ObraService {

  @Autowired
  private ObraRepository obraRepository;

  @Autowired
  private SocioRepository socioRepository;


  public void associarSocioAObra(Integer idSocio, Integer idObra) {
    Socio socio = socioRepository.findById(idSocio).orElse(null);

    Obra obra = obraRepository.findById(idObra).orElse(null);

    socio.getObras().add(obra);
    obra.getSocios().add(socio);

    obraRepository.save(obra);
  }

  public void desassociarSocioDeObra(Integer idSocio, Integer idObra) {
    Socio socio = socioRepository.findById(idSocio)
            .orElseThrow(() -> new EntityNotFoundException("Sócio não encontrado com ID: " + idSocio));

    Obra obra = obraRepository.findById(idObra)
            .orElseThrow(() -> new EntityNotFoundException("Obra não encontrada com ID: " + idObra));

    socio.getObras().remove(obra);
    obra.getSocios().remove(socio);

    obraRepository.save(obra);
  }

}