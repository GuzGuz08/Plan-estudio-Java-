import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"application", "domain", "infrastructure"})
@EntityScan(basePackages = "infrastructure.persistence")
@EnableJpaRepositories(basePackages = "infrastructure.persistence")
@EnableJpaAuditing
public class PersonasApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonasApiApplication.class, args);
    }

    
}
