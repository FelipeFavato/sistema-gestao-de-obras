// package com.gobra.sistemagestaodeobras.estudo;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.ResponseEntity;
// import org.springframework.transaction.annotation.Transactional;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.gobra.sistemagestaodeobras.estudo.dao.ObraDAOJDBCImplemented;
// import com.gobra.sistemagestaodeobras.estudo.obraDTO.ObraReqResDTO;
// import com.gobra.sistemagestaodeobras.model.Obra;


// @RestController
// @RequestMapping("/api/obra")
// public class ObrasController {

//   @Autowired
//   private ObraDAOJDBCImplemented obraDAOJDBC;

  
//   @PostMapping
//   public void saveObra(@RequestBody ObraReqResDTO data) {
//     Obra obraData = new Obra();
//     obraData.setCodigo(data.codigo());
//     obraData.setNome(data.nome());
//     obraData.setEndereco(data.endereco());
//     obraData.setDataInicio(data.dataInicio());
//     obraData.setDataPrevistaFim(data.dataPrevistaFim());
//     obraData.setDataRealFim(data.dataRealFim());
//     obraData.setCustoPrevisto(data.custoPrevisto());
//     obraData.setCompras(data.compras());
//     obraData.setSocios(data.socios());
//     obraDAOJDBC.save(obraData);
//   }

//   @GetMapping
//   public List<ObraReqResDTO> getAll() {
//     List<ObraReqResDTO> obraList = obraDAOJDBC.getAll().stream().map(ObraReqResDTO::new).toList();
//     return obraList;
//   }

//   @PutMapping
//   @Transactional
//   public ResponseEntity<Obra> updateObra(@RequestBody ObraReqResDTO data) {
//     Obra returnedObra = obraDAOJDBC.getById(data.codigo());

//     // Obra updatedObra = returnedObra;
//     returnedObra.setCodigo(data.codigo());
//     returnedObra.setNome(data.nome());
//     returnedObra.setEndereco(data.endereco());
//     returnedObra.setDataInicio(data.dataInicio());
//     returnedObra.setDataPrevistaFim(data.dataPrevistaFim());
//     returnedObra.setDataRealFim(data.dataRealFim());
//     returnedObra.setCustoPrevisto(data.custoPrevisto());
//     returnedObra.setCompras(data.compras());
//     returnedObra.setSocios(data.socios());

//     obraDAOJDBC.update(returnedObra);

//     return ResponseEntity.ok(returnedObra);
//   }

//   @DeleteMapping
//   public void deletaObra(@RequestBody Obra data) {
//     obraDAOJDBC.deleteById(data.getCodigo());
//   }
// }



// EXEMPLOS 
// // Component BookingCommandLineRunner implementa a interface CommandLineRunner que tem
// // um método run
// @Component
// class BookingCommandLineRunner implements CommandLineRunner {

// 	// Usando o método de sobrecarga para reescrever um método da super()
// 	@Override
// 	public void run(String... args) throws Exception {

// 		// for (Booking b : this.bookingRepository.findAll())
// 		// 	System.out.println(b.toString());
// 	}

// 	// @Autowired BookingRepository bookingRepository;
// }


// // Interface BookingRepository extende a interface JpaRepository que recebe uma classe e um ID
// // A classe JpaRepository extende de outras interfaces (classes abstratas) que possuem Crud e 
// // getters elaborados com sorting, etc
// interface BookingRepository extends JpaRepository<Booking, Long> {

// 	// Collection é uma interface com diversos metodos: add, remove, size, removeAll, etc...
// 	Collection<Booking> findByBookingName(String bookingName);

// }


// // Entidade básica
// @Entity
// class Booking {

// 	@Id @GeneratedValue
//   // primary key
// 	// Long => Tipo de dado que pode guardar numeros inteiros
// 	// (de -9223372036854775808 até 9223372036854775808)
// 	private Long id;
// 	private String bookingName;

// 	// Essa função recebe uma string com o nome da reserva e seta
// 	// o valor do objeto.bookingName instanciado
// 	// Chama o método da superclasse
// 	public Booking(String bookingName) {
// 		super();
// 		this.bookingName = bookingName;
// 	}

// 	public Booking() {

// 	}

// 	// Método publico para retornar o id
// 	public Long getId() {
// 		return id;
// 	}

// 	// Método público para retornar o nome da reserva
// 	public String getBookingName() {
// 		return bookingName;
// 	}
// }


// // Configuração pom.xml
// // <!-- <dependency>
// //   <groupId>com.h2database</groupId>
// //   <artifactId>h2</artifactId>
// // </dependency> -->
