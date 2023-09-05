package mark1708.com.pastebin.repository;

import mark1708.com.pastebin.domain.Paste;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasteRepository extends JpaRepository<Paste, String> {

}
