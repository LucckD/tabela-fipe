package com.github.lucckd.tabela_fipe;

import com.github.lucckd.tabela_fipe.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TabelaFipeApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(TabelaFipeApplication.class, args);
	}

	public void run (String... args) throws Exception {

		Principal principal = new Principal();
		principal.menu();

	}
}
