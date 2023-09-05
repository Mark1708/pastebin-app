package mark1708.com.pastebin.scheduler;

import lombok.RequiredArgsConstructor;
import mark1708.com.pastebin.service.HashService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HashScheduler {

  private final HashService hashService;

  @Scheduled(fixedDelay = 1000)
  public void prepareHashes() {
    while (!hashService.hasPrepared(10)) {
      hashService.generate();
    }
  }
}
