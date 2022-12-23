package org.daehagnawa.batch.daehagnawabatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class DaehagnawaBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(DaehagnawaBatchApplication.class, args);
    }

}
