// Arquivo de start da aplicação
package com.gobra.sistemagestaodeobras;

// import java.util.Collection;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.stereotype.Component;

// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.Id;


// Essa notação indica que essa classe é o começo de tudo
@SpringBootApplication
public class SistemaGestaoDeObrasApplication {

	// metodo main = ponto de start da aplicação
	public static void main(String[] args) {
		// Chama a SpringApplication e da um run na aplicação
		SpringApplication.run(SistemaGestaoDeObrasApplication.class, args);
	}

}


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