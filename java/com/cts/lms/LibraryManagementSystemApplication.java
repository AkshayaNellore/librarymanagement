package com.cts.lms;

import java.util.Base64;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@SpringBootApplication
public class LibraryManagementSystemApplication {

	public static void main(String[] args) {
		
		String secret = Base64.getEncoder().encodeToString(Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded());
		System.out.println(secret);

		SpringApplication.run(LibraryManagementSystemApplication.class, args);
	}

}
