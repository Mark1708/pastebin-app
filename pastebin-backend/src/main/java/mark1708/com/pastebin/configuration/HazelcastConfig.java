package mark1708.com.pastebin.configuration;

import com.hazelcast.collection.IQueue;
import com.hazelcast.config.Config;
import com.hazelcast.config.ManagementCenterConfig;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfig {

  public static final String HZ = "hazelcastInstance";

  public static final String HASH_QUEUE = "hashQueue";

  @Bean
  CacheManager cacheManager(@Qualifier(HZ) HazelcastInstance hazelcast) {
    return new HazelcastCacheManager(hazelcast);
  }

  @Bean
  public IQueue<String> hashQueue(@Qualifier(HZ) HazelcastInstance hazelcast) {
    return hazelcast.getQueue(HASH_QUEUE);
  }
}
