package pl.javastart.di;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Scanner;

@SpringBootApplication
class LinguApp {
    public static void main(String[] args) {
        ConfigurableApplicationContext context= SpringApplication.run(LinguApp.class,args);
        LinguController controller = context.getBean( LinguController.class );
        controller.mainLoop();
    }

    @Bean
    Scanner scanner() {
        return new Scanner(System.in);
    }

}