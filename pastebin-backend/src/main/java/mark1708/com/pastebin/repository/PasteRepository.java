package mark1708.com.pastebin.repository;

import mark1708.com.pastebin.model.entity.Paste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PasteRepository extends JpaRepository<Paste, String> {

  @Modifying
  @Query(value = """
      DELETE FROM Paste p WHERE p.expiredAt > NOW()
    """)
  void deleteExpiredPastes();
}
