package peoplehere.peoplehere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PeoplehereApplication {

	public static void main(String[] args) {
		SpringApplication.run(PeoplehereApplication.class, args);
	}

}
