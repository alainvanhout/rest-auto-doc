package rest.auto.doc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import rest.auto.doc.libs.EnableDocumentation;

@SpringBootApplication
@EnableDocumentation
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
