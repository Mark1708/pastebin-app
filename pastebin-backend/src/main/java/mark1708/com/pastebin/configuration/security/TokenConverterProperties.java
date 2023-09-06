package mark1708.com.pastebin.configuration.security;

import java.util.Optional;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "token.converter")
public class TokenConverterProperties {

  private String resourceId;
  private String principalAttribute;

  public String getResourceId() {
    return resourceId;
  }

  public Optional<String> getPrincipalAttribute() {
    return Optional.ofNullable(principalAttribute);
  }

  public void setResourceId(String resourceId) {
    this.resourceId = resourceId;
  }

  public void setPrincipalAttribute(String principalAttribute) {
    this.principalAttribute = principalAttribute;
  }
}
