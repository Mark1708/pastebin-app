package mark1708.com.pastebin.service;

import java.util.List;
import java.util.Optional;
import mark1708.com.pastebin.model.entity.Tag;

public interface TagService {

  Optional<Tag> findByName(String name);

  List<Tag> findAllByPasteHash(String hash);

  Tag save(String name);
}
