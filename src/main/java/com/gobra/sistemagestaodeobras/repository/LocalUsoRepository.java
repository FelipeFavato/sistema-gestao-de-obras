// A classe Repository é uma classe de interface que oferece um conjunto de métodos
// abstratros para manipular/gerenciar os dados e persistir esses dados.
// Esses métodos abstratos vão definir as operações CRUD


package com.gobra.sistemagestaodeobras.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.gobra.sistemagestaodeobras.model.LocalUso;
import java.util.List;
import java.util.Optional;


// Ao extender a JpaRepository => Ja tem acesso a todos os metodos de manipulação
// de banco de dados. JpaRepository<Entidade de consulta, tipo do identificador unico(id)> 
public interface LocalUsoRepository extends JpaRepository<LocalUso, Integer> {

  Optional<List<LocalUso>> findByNomeLocalUsoObra(String nomeLocalUsoObra);

}
