package com.ds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(
		info=@Info(
		title="Banking Application",
		description="Backend Rest APIs for  Bank Application",
		version="v1.0",
		contact=@Contact(
				name="Durga Sai",
				email="kodalisai889@gmail.comm"
				)
		
		),
		externalDocs = @ExternalDocumentation(
				description = "Banking Application Documentations"
				)
)

public class ProjectBankingApplicationCommercialBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectBankingApplicationCommercialBankApplication.class, args);
	}

}
