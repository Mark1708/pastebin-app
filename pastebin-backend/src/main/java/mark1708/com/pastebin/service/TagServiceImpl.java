package mark1708.com.pastebin.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mark1708.com.pastebin.domain.Tag;
import mark1708.com.pastebin.repository.TagRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

  private final TagRepository tagRepository;

  @Override
  @Cacheable("tagByName")
  public Optional<Tag> findByName(String name) {
    return tagRepository.findByName(name);
  }

  @Override
  @Cacheable("tagByHash")
  public List<Tag> findAllByPasteHash(String hash) {
    return tagRepository.findAllByPasteHash(hash);
  }

  @Override
  public Tag save(String name) {
    Optional<Tag> tagOptional = findByName(name);
    if (tagOptional.isPresent()) {
      throw new RuntimeException("Tag is exists");
    } else {
      return tagRepository.saveAndFlush(
          new Tag(name)
      );
    }
  }
}
