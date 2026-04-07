import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"controller", "service", "repository", "dto"})
@EntityScan(basePackages = "domain")
@EnableJpaRepositories(basePackages = "repository")
@EnableJpaAuditing
public class PersonasApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonasApiApplication.class, args);
    }

    
}
