package mark1708.com.pastebin.exception.http;

import mark1708.com.pastebin.exception.PastebinException;

public class BadRequestException extends PastebinException {

  public BadRequestException(String message) {
    super(message);
  }

  public BadRequestException(Throwable cause) {
    super(cause);
  }

  public BadRequestException(String message, Throwable cause) {
    super(message, cause);
  }
}
