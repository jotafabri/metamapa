package ar.edu.utn.frba.dds.metamapa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MetamapaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MetamapaApplication.class, args);
	}

}
