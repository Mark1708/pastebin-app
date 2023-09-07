package mark1708.com.pastebin.dto;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PasteDto extends BaseDto {

  private String hash;
  private String title;
  private String author;
  private List<String> tags;
  private String content;
  private String expiration;

}
