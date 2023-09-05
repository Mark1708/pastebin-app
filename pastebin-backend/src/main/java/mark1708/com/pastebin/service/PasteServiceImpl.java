package mark1708.com.pastebin.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mark1708.com.pastebin.domain.Paste;
import mark1708.com.pastebin.domain.Tag;
import mark1708.com.pastebin.dto.CreatePasteDto;
import mark1708.com.pastebin.repository.PasteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasteServiceImpl implements PasteService {

  private final TagService tagService;
  private final HashService hashService;
  private final PasteRepository pasteRepository;
  private final ContentStorageService contentStorageService;

  @Override
  public Paste getPasteByHash(String hash) {
    return pasteRepository.findById(hash).orElseThrow(() -> new RuntimeException("Такого нет"));
  }

  @Override
  @Transactional
  public Paste create(CreatePasteDto createPasteDto) {
    List<Tag> tags = createPasteDto.getTags()
        .stream()
        .map(tagName -> tagService.findByName(tagName)
            .orElseGet(() -> tagService.save(tagName))).toList();

    Instant createdAt = Instant.now();
    String hash = hashService.getUniqueHash();
    String contentPath = contentStorageService.uploadPasteContent(hash, createPasteDto.getText());
    return pasteRepository.saveAndFlush(
        Paste.builder()
            .hash(hash)
            .title(createPasteDto.getTitle())
            .author(createPasteDto.getAuthor())
            .contentPath(contentPath)
            .createdAt(createdAt)
            .expiredAt(getExpirationDateTime(createdAt, createPasteDto.getExpiration()))
            .tags(tags)
            .build()
    );
  }

  private Instant getExpirationDateTime(Instant createdAt, String expiration) {
    Instant expirationAt = createdAt;
    switch (expiration) {
      case "N", "1Y" -> expirationAt.plus(1, ChronoUnit.YEARS);
      case "10m" -> expirationAt.plus(10, ChronoUnit.MINUTES);
      case "1H" -> expirationAt.plus(1, ChronoUnit.HOURS);
      case "1D" -> expirationAt.plus(1, ChronoUnit.DAYS);
      case "1W" -> expirationAt.plus(1, ChronoUnit.WEEKS);
      case "2W" -> expirationAt.plus(2, ChronoUnit.WEEKS);
      case "1M" -> expirationAt.atZone(ZoneId.systemDefault()).plusMonths(1).toInstant();
      case "6M" -> expirationAt.plus(6, ChronoUnit.MONTHS);
      default -> {
        SimpleDateFormat validExpiresFormatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
          expirationAt = validExpiresFormatter
              .parse(expiration).toInstant();
        } catch (ParseException e) {
          throw new RuntimeException(e);
        }
      }
    }

    return expirationAt;
  }
}
