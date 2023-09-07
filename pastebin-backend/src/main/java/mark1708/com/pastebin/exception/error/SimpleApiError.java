package mark1708.com.pastebin.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Базовый класс для ошибок в API методах.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class SimpleApiError implements ApiError {

  /**
   * Текстовое сообщение об ошибке.
   */
  private String message;
}
