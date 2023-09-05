package mark1708.com.pastebin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableScheduling
@SpringBootApplication
public class PastebinBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(PastebinBackendApplication.class, args);
  }

}
