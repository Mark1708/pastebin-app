package mark1708.com.pastebin.exception.error;

import java.util.Collection;
import lombok.Getter;
import mark1708.com.pastebin.model.enums.QueryType;
import mark1708.com.pastebin.model.enums.ResourceType;

/**
 * Ошибка отсутствия ресурса.
 *
 * @param <K> тип идентификатора
 */
@Getter
public class NotFoundError<K> extends SimpleApiError {

  private final ResourceType resourceType;
  private final QueryType queryType;
  private final Collection<K> ids;

  public NotFoundError(ResourceType resourceType, QueryType queryType, Collection<K> identifiers, String message) {
    super(message);
    this.resourceType = resourceType;
    this.queryType = queryType;
    this.ids = identifiers;
  }
}
