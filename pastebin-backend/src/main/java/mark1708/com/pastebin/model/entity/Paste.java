package mark1708.com.pastebin.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pastes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Paste implements Serializable {

  @Id
  private String hash;
  private String title;
  private String author;
  private String contentPath;
  private String expiration;
  private Instant createdAt;
  private Instant expiredAt;

  @ManyToMany(
      fetch = FetchType.LAZY,
      cascade = CascadeType.REMOVE
  )
  @JoinTable(
      name = "pastes_tags",
      joinColumns = @JoinColumn(name = "paste_hash"),
      inverseJoinColumns = @JoinColumn(name = "tag_id")
  )
  private List<Tag> tags = new ArrayList<>();
}
