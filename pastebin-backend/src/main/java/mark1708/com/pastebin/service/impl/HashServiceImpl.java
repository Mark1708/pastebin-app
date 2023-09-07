package mark1708.com.pastebin.service;

import com.google.common.hash.Hashing;
import com.hazelcast.collection.IQueue;
import com.hazelcast.core.HazelcastInstance;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mark1708.com.pastebin.repository.PasteRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HashServiceImpl implements HashService {

  private final IQueue<String> hashQueue;
  private final PasteRepository pasteRepository;

  @Override
  public String getUniqueHash() {
    if (hashQueue.isEmpty()) {
      generate();
    }
    return hashQueue.poll();
  }

  @Override
  public void generate() {
    String hash;
    do {
      int seed = new Random().nextInt();
      hash = Hashing.sha512()
          .newHasher()
          .putLong(seed)
          .hash()
          .toString()
          .substring(0, 6);
    } while (isExistByHash(hash));
    hashQueue.add(hash);
  }

  @Override
  public boolean isExistByHash(String hash) {
    return pasteRepository.existsById(hash);
  }

  @Override
  public boolean hasPrepared(int minCount) {
    return hashQueue.size() >= minCount;
  }

}
