-- DEFINIÇÕES =>
-- 1. ENGINE=InnoDB: O InnoDB é um mecanismo de armazenamento popular que
-- fornece recursos de transação e conformidade com ACID.
-- 2. ACID: Atomicidade, Consistência, Isolamento e Durabilidade.
-- 3. DEFAULT CHARSET=utf8: Define o conjunto de caracteres padrão da tabela
-- como UTF-8. É um conjunto de caracteres que suporta diversos caracteres,
-- incluindo caracteres especiais e não latinos.


-- COMANDOS =>
-- 1. Criar uma tabela:
--    CREATE TABLE nome_da_tabela (
--      NOME_COLUNA TIPO_DADO
--    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 2. C ---> Inserir um único registro em uma tabela:
--    INSERT INTO nome_da_tabela (coluna1, coluna2, ...) VALUES (valor1, valor2, ...);
--    INSERT INTO nomes (primeiro_nome, ultimo_nome, idade) VALUES ('João', 'Silva', 30);

-- 3. C ---> Inserir multiplos registros em uma tabela:
--    INSERT INTO nome_da_tabela (coluna1, coluna2)
--    VALUES ('valor1', 5.00),
--           ('valor2', 10.00),
--           ('valor3', 15.00);

-- 4. C ---> Inserir registros em todas as colunas da tabela:
--    INSERT INTO clientes
--    VALUES (1, 'Maria', 'Silva', 'maria@email.com');

-- 5. R ---> Recuperar todos os registros de uma tabela:
--    SELECT * FROM nome_da_tabela;

-- 6. R ---> Recuperar colunas especificas de todos os registros:
--    SELECT coluna1, coluna2 FROM nome_da_tabela;

-- 7. R ---> Recuperar registros que atendam a uma condição especifica:
--    SELECT coluna1, coluna2 FROM nome_da_tabela WHERE condição;

-- 8. U ---> Atualizar um único registro com base em um critério especifico:
--    UPDATE nome_da_tabela SET coluna1 = novo_valor WHERE coluna2 = valor_procurado;
--    UPDATE clientes SET nome = 'Novo Nome' WHERE id = 123;

-- 9. D ---> Deletar todos os registros de uma tabela:
--    DELETE FROM nome_da_tabela;

-- 10. D ---> Deletar registros com base em uma condição especifica:
--    DELETE FROM nome_da_tabela WHERE coluna = valor;

-- 11. Toda vez que iniciar a aplicação, vai dropar a tabela. Não serve para o ambiente de produção:
--    DROP TABLE IF EXISTS nome_da_tabela


-- TIPOS DE DADOS => Letras maiusculas, minusculas ou combinação de ambas são aceitas. 
-- 1. BIGSERIAL: Tipo de dado usado para criar uma coluna que armazena 
-- numeros inteiros grandes(BIGINT) que é automaticamente preenchida
-- com valores sequenciais(SERIAL).
-- 2. PRIMARY KEY: Usada para garantir a unicidade dos valores em uma coluna.
-- 3. NOT NULL: A coluna não pode conter valores nulos.
-- 4. VARCHAR(quantidade): Tipo de dado usado para definir uma coluna que
-- armazena strings de caracteres variáveis. Ocupando apenas o espaço
-- da string armazenada.
-- 5. CHAR(quantidade): Tipo de dados usado para definir uma coluna que
-- armazena strings de caracteres com um comprimento fixo, sempre
-- ira armazenar a quantidade estipulada no parametro.
-- 6. DATE: Armazenda dados no formato "YYYY-MM-DD".
-- 7. DEFAULT NULL: Se nenhum valor for especificado ao inserir um novo
-- registrado, o valor padrão sera NULL.
-- 8. INT(quantidade): Dado do tipo INTEIRO com a largura = quantidade.
-- 9. UNSIGNED: Aceitas apenas valores não negativos (positivos ou zero).
-- 10. SMALLINT: Aceita numeros dentro de um intervalo de -32.768 a 32.767.
-- 11. INTEGER: Aceita numeros dentro de um intervalo de -2.147.483.648 a 2.147.483.647.
-- 12. BIGINT: Intervalo de -9.223.372.036.854.775.808 a 9.223.372.036.854.775.807.
-- 13. MONEY: Usado para valores monetários.
-- 14. TIMESTAMP: 'YYYY-MM-DD HH:MI:SS.ssssss'. 'sssssss' representa as frações de segundos.


-- schema.sql => Convenção para procura de arquivos

-- DROP TABLE IF EXISTS obra;

CREATE TABLE obra (
  CODIGO BIGSERIAL PRIMARY KEY NOT NULL,
  NOME VARCHAR(30) NOT NULL,
  ENDERECO VARCHAR(40) NOT NULL,
  DATAINICIO DATE NOT NULL,
  DATAPREVISTAFIM DATE,
  DATAREALFIM DATE,
  CUSTOPREVISTO INTEGER
)