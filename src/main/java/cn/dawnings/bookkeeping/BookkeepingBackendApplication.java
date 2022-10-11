package cn.dawnings.bookkeeping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class BookkeepingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookkeepingBackendApplication.class, args);
    }

}
