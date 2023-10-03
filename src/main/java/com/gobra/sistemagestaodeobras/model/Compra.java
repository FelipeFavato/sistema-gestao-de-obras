package com.gobra.sistemagestaodeobras.model;

import java.util.Date;

import com.gobra.sistemagestaodeobras.dto.CompraRequestDTO;

// import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;




@Table(name = "compra")
@Entity(name = "compra")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "codigo")
public class Compra {

  @Id
  @Column(name = "codigo")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "compra_seq_generator")
  @SequenceGenerator(name = "compra_seq_generator", sequenceName = "COMPRA_SEQUENCIA", initialValue = 1, allocationSize = 1)
  private Integer codigo;

  // 1.1 ManyToOne: MUITAS compras para UMA obra.
  // 1.2 (cascade = CascadeType.ALL): Indica que qualquer operação de persistência (salvar, atualizar
  //      ou excluir) realizada no objeto atual tambem será aplicada ao objeto relacionado.
  // 2.1 @JoinColumn(): Informa ao JPA que 'id_obra' será uma chave estrangeira.
  // 2.2 name = "id_obra":
  // 2.3 referencedColumnName = "codigo": Especifica o nome da coluna na tabela da entidade relacionada
  //     que será usada como chave estrangeira. Ou seja a chave estrangeira "id_obra" na tabela "compra"
  //     será a coluna "codigo" da tabela "Obra".
  // @ManyToOne(cascade = CascadeType.ALL)
  // @JoinColumn(name = "id_obra", referencedColumnName = "codigo")
  // private Obra obra;

  @ManyToOne //(cascade = CascadeType.ALL)
  @JoinColumn(name = "id_fornecedor", referencedColumnName = "codigo")
  private Fornecedor fornecedor;

  @Column(name = "data_compra", length = 30)
  @Temporal(TemporalType.DATE)
  private Date dataCompra;

  @Column(name = "data_entrega", length = 30)
  @Temporal(TemporalType.DATE)
  private Date dataEntrega;

  @Column(name = "valor_original", length = 30)
  private int valorOriginal;

  @Column(name = "valor_desconto", length = 30)
  private int valorDesconto;

  @Column(name = "valor_final", length = 30)
  private int valorFinal;


  public Compra(CompraRequestDTO data) {
    // this.obra = data.obra();
    this.fornecedor = data.fornecedor();
    this.dataCompra = data.dataCompra();
    this.dataEntrega = data.dataEntrega();
    this.valorOriginal = data.valorOriginal();
    this.valorDesconto = data.valorDesconto();
    this.valorFinal = data.valorFinal();
  }
}
