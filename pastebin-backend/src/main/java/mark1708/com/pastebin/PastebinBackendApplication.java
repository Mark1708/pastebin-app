package mark1708.com.pastebin;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableScheduling
@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Pastebin",
        version = "1.0",
        description = "Description of Pastebin Rest API"
    )
)
public class PastebinBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(PastebinBackendApplication.class, args);
  }

}
