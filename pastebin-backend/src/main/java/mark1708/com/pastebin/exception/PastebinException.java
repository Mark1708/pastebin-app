package mark1708.com.pastebin.exception;

public class PastebinException extends RuntimeException {

  public PastebinException() {
  }

  public PastebinException(String message) {
    super(message);
  }

  public PastebinException(String message, Throwable cause) {
    super(message, cause);
  }

  public PastebinException(Throwable cause) {
    super(cause);
  }

  public PastebinException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
