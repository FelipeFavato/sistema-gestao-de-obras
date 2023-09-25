package com.gobra.sistemagestaodeobras.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.gobra.sistemagestaodeobras.model.Obra;

public class ObraDAOJDBCImplemented implements ObraDAO{
  
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

  @Override
  public void save(Obra obra) {
    // Preciso passar o ID (codigo) na query? Teoricamente o banco vai gerar um automaticamente.
    // 1. String query: Query a, em formato de String, a ser realizada quando o serviço for acionado.
    String query = "insert into Obra (codigo, nome, endereco, dataInicio, dataPrevistaFim, dataRealFim, custoPrevisto) values(?, ?, ?, ?, ?, ?, ?)";

    // 1. JdbcTemplate: Classe fornecida pelo Spring Framework que simplifica a execução
    //    de operações no banco de dados. Abstrai muitas das complexidades do JDBC,
    //    como a abertura e fechamento de conexões, a criação e execução de consultas SQL.
    // 2. jdbcTemplate: Instância de classe JdbcTemplate. Posso usar essa variavel para
    //    chamar metodos que executam operações no banco de dados
    // 3. new JdbcTemplate(dataSource): Quando é criada um nova instancia de JdbcTemplate,
    //    ela precisa saber como se conectar ao banco de dados, e essas informações de
    //    configuração e conexão com o BD são fornecidas pelo dataSource.
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    // 1. Object[] args: Cria um array de objetos chamado 'args'
    // 2. new Object[] {obra.getCodigo(), ...}: Cria um novo array de objetos e inicializa
    //    esses objetos com os valores das propriedades do objeto obra.
    Object[] argumentos = new Object[] {
      obra.getCodigo(),
      obra.getNome(),
      obra.getEndereco(),
      obra.getDataInicio(),
      obra.getDataPrevistaFim(),
      obra.getDataRealFim(),
      obra.getCustoPrevisto()
    };

    // 1. int out: Declara uma variável 'out' do tipo 'int'. Usada para armazenar o resultado
    //    da operação de atualização no banco de dados. Esse valor retornada será o número de
    //    linhas afetadas pela operação de atualização. Possibilita verificar quantas linhas
    //    foram inseridas, atualizadas ou excluidas.
    // 2. jdbcTemplate.update(query, argumentos): Chama o método update da instancia JdbcTemplate
    //    criada para ajudar com as operações do BD. Seus parametros são query(string que
    //    representa a consulta SQL a ser executada) e argumentos(array de objetos que contem os
    //    parametros a serem usados na consulta SQL).
    int out = jdbcTemplate.update(query, argumentos);
  
    // 1. if (out != 0): Caso o valor da variavel 'out' seja diferente de 0, significa que
    //    alguma alteração no banco de dados foi feita.
    // 2. Caso 'out' seja de fato diferente de 0. O console irá printar a mensagem de sucesso.
    //    Caso 'out' seja = 0. O console irá printar a mensagem de falha.
    if (out != 0) {
			System.out.println("Obra salva com sucesso! Código="+obra.getCodigo());
		} else System.out.println("Falha na criação da Obra. Código="+obra.getCodigo());
  }

  @Override
  public Obra getById(int codigo) {
    // 1. String query = Seleciona tudo de um registro que tenha o codigo enviado como parametro
    String query = "select * from Obra where codigo = ?";

    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    // https://www.digitalocean.com/community/tutorials/spring-jdbc-example#spring-jdbctemplate-example
    // O método queryForObject esta depreciado e precisa ser alterado por outro
    // https://www.baeldung.com/spring-boot-replace-deprecated-jdbctemplate-queryforobject-query
    Obra obra = jdbcTemplate.queryForObject(query, new Object[]{codigo}, new RowMapper<Obra>() {
      @Override
      public Obra mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Obra obra = new Obra();
        obra.setCodigo(resultSet.getInt("codigo"));
        obra.setNome(resultSet.getString("nome"));
        obra.setEndereco(resultSet.getString("endereco"));
        obra.setDataInicio(resultSet.getDate("dataInicio"));
        obra.setDataPrevistaFim(resultSet.getDate("dataPrevistaFim"));
        obra.setDataRealFim(resultSet.getDate("dataRealFim"));
        obra.setCustoPrevisto(resultSet.getInt("custoPrevisto"));

        return obra;
      }
    });

    return obra;
  }

  @Override
  public List<Obra> getAll() {
    
  }

  @Override
  public void update(Obra obra) {

  }

  @Override
  public void deleteById(int codigo) {

  }
}
