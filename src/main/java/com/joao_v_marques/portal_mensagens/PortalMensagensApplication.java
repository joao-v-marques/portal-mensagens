package com.joao_v_marques.portal_mensagens;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class PortalMensagensApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortalMensagensApplication.class, args);
	}

}
