package mark1708.com.pastebin.dto;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreatePasteDto extends BaseDto {

  private String title;
  private String author;
  private List<String> tags;
  private String text;
  private String expiration;

}
