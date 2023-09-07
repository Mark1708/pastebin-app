package mark1708.com.pastebin.scheduler;

import lombok.RequiredArgsConstructor;
import mark1708.com.pastebin.service.PasteService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasteScheduler {

  private final PasteService pasteService;

  @Scheduled(fixedDelay = 5 * 60 * 1000)
  public void cleanExpiredPastes() {
    pasteService.deleteExpiredPastes();
  }
}
