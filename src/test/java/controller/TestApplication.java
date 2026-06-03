package controller;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"application", "infrastructure"})
@EntityScan(basePackages = "infrastructure.persistence")
@EnableJpaRepositories(basePackages = "infrastructure.persistence")
@EnableJpaAuditing
public class TestApplication {
}
