package ar.edu.utn.frba.dds.metamapa.ratelimit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;

@Service
public class RateLimitService {

  private final Cache<String, Bucket> bucketCache;

  public RateLimitService() {
    this.bucketCache = Caffeine.newBuilder()
        .expireAfterAccess(1, TimeUnit.HOURS)
        .maximumSize(10000)
        .build();
  }

  public boolean allowRequest(String key, int maxRequests, int durationSeconds) {
    Bucket bucket = bucketCache.get(key, k -> createBucket(maxRequests, durationSeconds));
    return bucket.tryConsume(1);
  }

  private Bucket createBucket(int maxRequests, int durationSeconds) {
    Bandwidth limit = Bandwidth.classic(
        maxRequests,
        Refill.intervally(maxRequests, Duration.ofSeconds(durationSeconds))
    );
    return Bucket.builder()
        .addLimit(limit)
        .build();
  }

  public void resetBucket(String key) {
    bucketCache.invalidate(key);
  }

  public void resetAllBuckets() {
    bucketCache.invalidateAll();
  }
}
