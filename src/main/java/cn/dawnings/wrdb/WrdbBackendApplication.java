package cn.dawnings.wrdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
@EnableR2dbcAuditing
public class WrdbBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(WrdbBackendApplication.class, args);
    }

}
