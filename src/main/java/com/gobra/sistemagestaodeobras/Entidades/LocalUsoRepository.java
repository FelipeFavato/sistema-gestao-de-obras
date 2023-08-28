package com.gobra.sistemagestaodeobras.Entidades;

import org.springframework.data.jpa.repository.JpaRepository;


// Ao extender a JpaRepository => Ja tem acesso a todos os metodos de manipulação
// de banco de dados. JpaRepository<Entidade de consulta, tipo do identificador unico(id)> 
public interface LocalUsoRepository extends JpaRepository<LocalUso, Integer> {

}
