// Informações retiradas de: 
//  https://www.digitalocean.com/community/tutorials/spring-jdbc-example#spring-jdbctemplate-example

package com.gobra.sistemagestaodeobras.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.gobra.sistemagestaodeobras.model.Obra;

public class ObraDAOJDBCImplemented implements ObraDAO {
  
	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

  // 1. @Override: Esta anotação indica que o método está substituindo um método da classe pai ou uma interface.
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
    //    da operação de atualização no banco de dados. Esse valor retornado será o número de
    //    linhas afetadas pela operação de atualização. Possibilita verificar quantas linhas
    //    foram inseridas, atualizadas ou excluidas.
    // 2. jdbcTemplate.update(query, argumentos): Chama o método update da instancia JdbcTemplate
    //    criada para ajudar com as operações do BD. Seus parametros são query(string que
    //    representa a consulta SQL a ser executada) e argumentos(array de objetos que contem os
    //    parametros a serem usados na consulta SQL).
    //    2.1. update: Esse método é usado para consultas SQL de modificação como:
    //         DELETE, INSERT e UPDATE.
    int out = jdbcTemplate.update(query, argumentos);
  
    // 1. if (out != 0): Caso o valor da variavel 'out' seja diferente de 0, significa que
    //    alguma alteração no banco de dados foi feita.
    // 2. Caso 'out' seja de fato diferente de 0. O console irá printar a mensagem de sucesso.
    //    Caso 'out' seja = 0. O console irá printar a mensagem de falha, pois 0 linhas foram
    //    alteradas.
    if (out != 0) {
			System.out.println("Obra salva com sucesso! Código="+obra.getCodigo());
		} else System.out.println("Falha na criação da Obra. Código="+obra.getCodigo());
  }

  @Override
  public Obra getById(int codigo) {
    // 1. String query = Seleciona tudo de um registro que tenha o codigo enviado como parametro
    String query = "select * from Obra where codigo = ?";

    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    // O método queryForObject esta depreciado e precisa ser alterado por outro. Links:
    // https://www.baeldung.com/spring-boot-replace-deprecated-jdbctemplate-queryforobject-query
    // https://github.com/eugenp/tutorials/blob/master/persistence-modules/spring-jdbc/src/main
    //  /java/com/baeldung/spring/jdbc/replacedeprecated/StudentDaoWithPreferredJdbcTemplateMethods.java
    // Resolvendo a depreciação: Trocar de lugar os parametros 'new Object[] {codigo}' e 'new RowMapper<Obra>'.
    // Trocar o parametro 'new Object[] {codigo}' por apenas 'codigo'.

    // 1. queryForObject:
    //    1.1 Obra obra: Declara e inicializa uma variável que terá o resultado da consulta após a execução.
    //    1.2 jdbcTemplate.queryForObject(): Método utilizado para executar uma consulta SQL e esperar
    //        exatamente 1 (um) resultado. Espera 3 argumento.
    //    1.3 query: Consulta a ser executada. Nesse caso procura por um registro da tabela 'Obra'.
    //    1.4 new Object[]{codigo}: Aqui vão os valores dos parametros da consulta. Passando o 'codigo'
    //        para substituir a interrogação ('?').
    //    1.5 new RowMapper<Obra> () { ... }: Um objeto RowMapper que é usado para mapear cada linha do
    //        resultado da consulta para um objeto 'Obra'. Dentro deste objeto Anonimo esta sendo implementado
    //        o método mapRow que é responsavel por criar uma instância de Obra e preenchê-la com os valores
    //        das colunas do resultado.
    // 2. public Obra mapRow(ResultSet resultSet, int rowNum) throws SQLException { ... }: Implementação
    //    do método mapRow dentro do objeto RowMapper.
    //    2.1 Método mapRow é chamado para cada linha do resultado na consulta SQL.
    //    2.2 ResultSet: Contem os valores da linha atual.
    //    2.3 rowNum: Indica o número da linha atual (geralmente usado quando se precisa acompanhar a
    //        posição da linha).
    //    2.4 throws SQLException:
    // 3. Obra obra = new Obra(): Cria uma nova instância da classe Obra. Essa instância será preenchida
    //    com os valores das colunas da linha atual do resultado.
    // 4. obra.setCodigo(resultSet.getInt("codigo")): Obtem o valor da coluna 'codigo' da linha atual do
    //    resultado, usando o metodo 'getInt' do 'ResultSet' e em seguida atribui esse valor ao campo 'codigo'
    //    da instancia 'obra' usando o método 'setCodigo'.
    // 5. obra.setNome(resultSet.getString("nome")): Igual ao passo 4, com as devidas tipagens.
    // 6. obra.setEndereco(resultSet.getString("endereco")): Igual ao passo 4, com as devidas tipagens.
    // 7. obra.setDataInicio(resultSet.getDate("dataInicio")): Igual ao passo 4, com as devidas tipagens.
    // 8. obra.setDataPrevistaFim(resultSet.getDate("dataPrevistaFim")): Igual ao passo 4, com as devidas tipagens.
    // 9. obra.setDataRealFim(resultSet.getDate("dataRealFim")): Igual ao passo 4, com as devidas tipagens.
    // 10. obra.setCustoPrevisto(resultSet.getInt("custoPrevisto")): Igual ao passo 4, com as devidas tipagens.
    // 11. return Obra: Após preencher todos os campos da instância 'obra' com os valores da linha atual do
    //     'ResultSet', o método 'mapRow' retorna essa instância 'obra'.

    Obra obra = jdbcTemplate.queryForObject(query,  new Object[]{codigo}, new RowMapper<Obra>() {
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
    // String query = "select codigo, nome, endereco, dataInicio, dataPrevistaFim, dataRealFim, custoPrevisto from Obra";
    String query = "select * from Obra";
  
    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
  
    // 1. List<Obra>: Tipa a variável ObraList como uma lista só que pode conter objetos
    //    do tipo 'Obra'.
    // 2. ObraList: Variável que representa a lista.
    // 3. new ArrayList<Obra>: Cria uma nova instância de uma lista do tipo ArrayList (lista dinâmica
    //    que pode adicionar ou remover elementos facilmente). A lista deve conter apenas objetos do tipo
    //    Obra.
    List<Obra> ObraList = new ArrayList<Obra>(null);
  
    // 1. List<Map<String, Object>> ObraRows: Declara uma variável chamada ObraRows, que é uma lista
    //    do tipo List<Map<String, Object>>, que contem objetos de mapa, em que as chaves são STRINGS,
    //    e os valores são OBJECT. Cada MAP representa uma linha de resultados da consulta.
    // 2. queryForList(query): Chama o método 'queryForList' da instância 'jdbcTemplate' passando a
    //    query como parâmetro. Esse método recupera os resultados como uma lista de MAPas.
    // 3. RETORNO: Cada elemento da lista é um mapa onde as chaves são nomes de colunas na tabela,
    //    e os valores são objetos correspondentes a essas colunas.
    List<Map<String, Object>> ObraRows = jdbcTemplate.queryForList(query);
  
    // 1. SimpleDateFormat: Classe Java que faz parte do pacote 'java.text' usada para formatar
    //    datas e horas de acordo com um padrão especifico.
    // 2. dateFormat: Variável que instancia a classe SimpleDateFormat.
    // 3. new SimpleDateFormat("yyyy-MM-dd"): 'yyyy-MM-dd' é o argumento passado para o construtor
    //    da classe. Fazendo com que formate a data no formato ANO-MES-DIA.
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // 1. for (Map<String, Object> ObraRow : ObraRows): Inicia um loop 'for-each' que itera sobre
    //    cada MAPA na lista 'ObraRows'. Cada 'obraRow' é uma linha da consulta.
    // 2. Obra obra = new Obra(): Cria uma nova instância da classe Obra a cada iteração.
    // 3. obra.setCodigo(Integer.parseInt(String.valueOf(obraRow.get("codigo")))):
    //    3.1 obraRow.get("codigo"): Extrai o valor associado à chave 'codigo' deste mapa (resultado/Objeto).
    //         O campo 'codigo' é originalmente um objeto 'Object', devido a consulta.
    //    3.2 String.valueOf(): Converte o valor (Object) obtido do passo acima para uma representação de STRING.
    //    3.3 Integer.parseInt(): Converte o valor (String) obtido do passo acima para um número inteiro (INT),
    //         isso é necessário pois o campo 'codigo' é uma representação númerica.
    //    3.4 obra.setCodigo(): Atribui o valor (int) resultante ao campo 'codigo' do objeto 'obra'.
    // 4. obra.setNome(String.valueOf(obraRow.get("nome"))): Converte o valor associado à chave 'nome' em
    //    'obraRow' para uma STRING e em seguida define esse valor como o nome da obra usando o metodo
    //    'setNome()'. Atualiza a propriedade 'nome' do objeto Obra criado no passo 2 com o valor 'nome'
    //    extraido de 'obraRow'.
    // 5. obra.setEndereco(String.valueOf(obraRow.get("endereco"))): Igualzinho o passo 4. Atualiza a propriedade
    //    'endereco' do objeto Obra criado no passo 2 com o valor 'endereco' extraido de 'obraRow'.
    // 6. Lógica para extrair formatos de DATE:
    //    6.1 String dataInicioStr = String.valueOf(obraRow.get("dataInicio")): Converte o valor associado à chave
    //         'dataInicio' em 'obraRow' para uma STRING e armazena na variavel 'dataInicioStr'.
    //    6.2 Date dataInicio = null: Declara a variavel 'dataInicio' com valor 'null' do tipo DATE. Será usada para
    //         armazenar a data de inicio da obra após a conversão.
    //    6.3 try { dataInicio = dateFormat.parse(dataInicioStr); }: Analisa a data que esta em formato de STRING e
    //         converte-a para formato de DATE.
    //    6.4 catch (ParseException e) { e.printStackTrace(); }: Caso alguma exceção/erro aconteça, captura e exibe
    //         o erro.
    //    6.5 obra.setDataInicio(dataInicio): Atualiza a dataInicio da objeto instanciado no passo 2.
    // 7. Igual ao passo 6, só que para o atributo 'dataPrevistaFim'.
    // 8. Igual ao passo 6, só que para o atributo 'dataRealFim'.
    // 9. obra.setCustoPrevisto(Integer.parseInt(String.valueOf(obraRow.get("custoPrevisto")))): Mesma ideia do
    //    passo 3. Só que para a propriedade 'custoPrevisto'.
    // 10. ObraList.add(obra): Pega o objeto instanciado no passo 2 e com seus valores atualizados nos passos
    //     3 a 9 e adiciona na lista de obras que será o retorno desse método.

    for (Map<String, Object> obraRow : ObraRows) {
      Obra obra = new Obra();
      obra.setCodigo(Integer.parseInt(String.valueOf(obraRow.get("codigo"))));
      obra.setNome(String.valueOf(obraRow.get("nome")));
      obra.setEndereco(String.valueOf(obraRow.get("endereco")));

      String dataInicioStr = String.valueOf(obraRow.get("dataInicio"));
      Date dataInicio = null;
      try {
          dataInicio = dateFormat.parse(dataInicioStr);
      } catch (ParseException e) {
          e.printStackTrace();
      }
      obra.setDataInicio(dataInicio);

      String dataPrevistaFimStr = String.valueOf(obraRow.get("dataPrevistaFim"));
      Date dataPrevistaFim = null;
      try {
          dataPrevistaFim = dateFormat.parse(dataPrevistaFimStr);
      } catch (ParseException e) {
          e.printStackTrace();
      }
      obra.setDataPrevistaFim(dataPrevistaFim);

      String dataRealFimStr = String.valueOf(obraRow.get("dataRealFim"));
      Date dataRealFim = null;
      try {
          dataRealFim = dateFormat.parse(dataRealFimStr);
      } catch (ParseException e) {
          e.printStackTrace();
      }
      obra.setDataRealFim(dataRealFim);

      obra.setCustoPrevisto(Integer.parseInt(String.valueOf(obraRow.get("custoPrevisto"))));
      ObraList.add(obra);
    }

    // 1. return ObraList: Retorna a lista de Obras.
    return ObraList;
  }

  @Override
  public void update(Obra obra) {
    String query = "update Obra set nome=?, endereco=?, dataInicio=?, dataPrevistaFim=?, dataRealFim=?, custoPrevisto=? where codigo=?";

    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    // 1. Object[] args: Cria um array de objetos chamado 'args'
    // 2. new Object[] {obra.getCodigo(), ...}: Cria um novo array de objetos e inicializa
    //    esses objetos com os valores das propriedades do objeto obra.
    Object[] args = new Object[] {
      obra.getNome(),
      obra.getEndereco(),
      obra.getDataInicio(),
      obra.getDataPrevistaFim(),
      obra.getDataRealFim(),
      obra.getCustoPrevisto()
    };

    int out = jdbcTemplate.update(query, args);

    if (out != 0) {
			System.out.println("Obra aterada com sucesso! Código="+obra.getCodigo());
		} else System.out.println("Falha na alteração da Obra. Código="+obra.getCodigo());
  }

  @Override
  public void deleteById(int codigo) {
    // 1. query: Query utilizada para encontrar tudo de um registro por meio de um 'codigo'.
    String query = "delete from Obra where codigo=?";

    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

    int out = jdbcTemplate.update(query, codigo);

    if (out != 0) {
			System.out.println("Obra deletada com sucesso! Código="+codigo);
		} else System.out.println("Falha na deleção da Obra. Código="+codigo);
  }
}
