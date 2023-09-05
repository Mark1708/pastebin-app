package mark1708.com.pastebin.repository;

import java.util.List;
import java.util.Optional;
import mark1708.com.pastebin.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

  Optional<Tag> findByName(String name);

  @Query(value = """
          SELECT t.* FROM tags t
          JOIN pastes_tags pt ON t.id = pt.tag_id
          WHERE pt.paste_hash = :hash
          """,
      nativeQuery = true
  )
  List<Tag> findAllByPasteHash(@Param("hash") String hash);
}
