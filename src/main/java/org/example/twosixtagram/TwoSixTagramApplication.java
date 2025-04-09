package org.example.twosixtagram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class TwoSixTagramApplication {

    public static void main(String[] args) {
        SpringApplication.run(TwoSixTagramApplication.class, args);
    }

}
