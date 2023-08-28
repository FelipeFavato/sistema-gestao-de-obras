package com.gobra.sistemagestaodeobras.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gobra.sistemagestaodeobras.Entidades.LocalUso;
import com.gobra.sistemagestaodeobras.Entidades.LocalUsoRepository;


@RestController  // Essa notação indica que essa classe é um controller
@RequestMapping("/localuso")  // Essa notação indica qual endpoint essa classe controla
public class LocalUsoController {

  // Essa notação indica para o spring que é ele quem vai fazer a injeção
  @Autowired  // dessa dependencia na minha classe LocalUsoController
  private LocalUsoRepository repository;  // Instancia de LocalUsoRepository criada

  // Essa notação faz com que quando eu acessar esse endpoint
  // com o metodo GET, é pra executar o metodo abaixo
  @GetMapping
  public List<LocalUso> getAll() {
    List<LocalUso> localUsoList = repository.findAll();
    return localUsoList;
  }
}
