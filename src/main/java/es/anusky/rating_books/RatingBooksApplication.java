package es.anusky.rating_books;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;

@SpringBootApplication
public class RatingBooksApplication {

	@Bean
	CommandLineRunner runner(ApplicationContext context) {
		return args -> {
			System.out.println("=== Registered UserDetailsService beans ===");
			String[] beanNames = context.getBeanNamesForType(UserDetailsService.class);
			for (String name : beanNames) {
				System.out.println("🔍 " + name + " -> " + context.getBean(name).getClass().getName());
			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(RatingBooksApplication.class, args);
	}

}
