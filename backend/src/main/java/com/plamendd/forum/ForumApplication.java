package com.plamendd.forum;

import com.plamendd.forum.config.SwaggerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@Import(SwaggerConfiguration.class)
public class ForumApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForumApplication.class, args);
	}

}
