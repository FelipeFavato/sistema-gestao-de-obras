// // DAO: Data Access Object.
// // O padrão DAO é um padrão usado para abstrair e encapsular o acesso aos dados
// // em um banco de dados.
// // Principal finalidade do padrão DAO é separar a lógica de negócios de acesso
// // ao banco de dados. Fornece uma camada de abstração entre a aplicação e o banco.
// // Principais componentes de um padrão DAO no contexto do JDBC:
// //  - Interface DAO: Conjunto de métodos abstratos para acessar dados.
// //  - Implementação DAO: Implementação desses métodos abstratos. Classe responsavel
// //    por interafir diretamente com o banco de dados usando o JDBC.
// package com.gobra.sistemagestaodeobras.estudo.dao;

// import java.util.List;

// import com.gobra.sistemagestaodeobras.estudo.Obra;

// public interface ObraDAO {
  
//   // Create
//   public void save(Obra obra);

//   // Read
//   public Obra getById(int codigo);

//   public List<Obra> getAll();

//   // Update
//   public void update(Obra obra);

//   // Delete
//   public void deleteById(int codigo);

// }
