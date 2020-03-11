package com.repository.lite;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableMapRepositories(basePackages = "com.study")
public class LiteRepositoryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LiteRepositoryApplication.class, args);
    }


    @Component
    protected static class Test implements CommandLineRunner {

        private final TesteRepository testeRepository;
        private final StringRepository stringRepository;

        public Test(TesteRepository testeRepository, StringRepository stringRepository) {
            this.testeRepository = testeRepository;
            this.stringRepository = stringRepository;
        }

        @Override
        public void run(String... args) throws Exception {
            testeRepository.save(new Teste(), "1");

            Teste teste = testeRepository.find("1");
            System.out.println(teste);

            stringRepository.save("Fabio", "nome");

            String nome = stringRepository.find("nome");
            System.out.println(nome);
        }
    }
}
