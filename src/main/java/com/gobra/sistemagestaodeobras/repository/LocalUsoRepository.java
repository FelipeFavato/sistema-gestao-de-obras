package com.gobra.sistemagestaodeobras.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gobra.sistemagestaodeobras.model.LocalUsoModel;


// Ao extender a JpaRepository => Ja tem acesso a todos os metodos de manipulação
// de banco de dados. JpaRepository<Entidade de consulta, tipo do identificador unico(id)> 
public interface LocalUsoRepository extends JpaRepository<LocalUsoModel, Integer> {

}
