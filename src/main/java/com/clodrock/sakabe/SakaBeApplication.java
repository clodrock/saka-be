package com.clodrock.sakabe;

import com.clodrock.sakabe.model.RegisterRequest;
import com.clodrock.sakabe.service.AuthenticationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import static com.clodrock.sakabe.enums.Role.ADMIN;
import static com.clodrock.sakabe.enums.Role.MANAGER;

@SpringBootApplication
public class SakaBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SakaBeApplication.class, args);
	}
/*
	@Bean
	public CommandLineRunner commandLineRunner(
			AuthenticationService service
	) {
		return args -> {
			var admin = RegisterRequest.builder()
					.firstname("Samet")
					.lastname("Topakkaya")
					.email("admin@saka.com")
					.password("password")
					.role(ADMIN)
					.build();
			System.out.println("Admin token: " + service.register(admin).getAccessToken());

			var manager = RegisterRequest.builder()
					.firstname("Kaan")
					.lastname("Celik")
					.email("manager@saka.com")
					.password("password")
					.role(MANAGER)
					.build();
			System.out.println("Manager token: " + service.register(manager).getAccessToken());

		};
	}*/
}
