package mark1708.com.pastebin.model.enums;

import lombok.Getter;

@Getter
public enum QueryType {
  ID("ids"),
  HASH("hashes"),
  NAME("names"),
  PATH("paths");

  private final String queryName;
  QueryType(String queryName) {
    this.queryName = queryName;
  }
}
