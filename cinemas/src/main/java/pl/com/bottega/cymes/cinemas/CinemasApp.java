package pl.com.bottega.cymes.cinemas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CinemasApp {
    public static void main(String[] args) {
        SpringApplication.run(CinemasApp.class, args);
    }
}
