package mark1708.com.pastebin.model.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreatePasteDto extends BaseDto {

  @NotEmpty
  private String title;
  @NotEmpty
  private String author;
  @NotEmpty
  private List<String> tags;
  @NotEmpty
  private String text;
  @NotEmpty
  private String expiration;

}
